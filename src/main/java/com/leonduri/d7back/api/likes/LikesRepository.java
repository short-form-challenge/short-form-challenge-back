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
    @Query(value = "DELETE FROM likes where liked_on = :videoId and liked_by = :userId", nativeQuery = true)
    int deleteLikes(@Param("videoId") Long videoId, @Param("userId") Long userId);

    @Query(value = "select count(liked_on) from likes where liked_on = :videoId", nativeQuery = true)
    Long countLikes(@Param("videoId") Long videoId);
}
