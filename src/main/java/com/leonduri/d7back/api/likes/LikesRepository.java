package com.leonduri.d7back.api.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Override
    List<Likes> findAll();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Likes where liked_on = :videoId and liked_by = :userId", nativeQuery = true)
    int deleteLikes(@Param("videoId") Long videoId, @Param("userId") Long userId);
}
