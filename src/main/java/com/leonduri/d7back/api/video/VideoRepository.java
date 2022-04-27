package com.leonduri.d7back.api.video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Override
    List<Video> findAll();

    @Override
    Optional<Video> findById(Long aLong);

    @Query(value = "SELECT * FROM Video join User on Video.posted_by = User.id and Video.posted_by = :userId " +
            "join Category on Video.category_id = Category.id and Video.category_id = :categoryId " +
            "order by Video.show_id limit :count offset :page", nativeQuery = true)
    List<Video> getVideoList(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("page")Long page, @Param("count") Long count);
}