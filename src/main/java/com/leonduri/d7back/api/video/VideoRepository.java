package com.leonduri.d7back.api.video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Override
    List<Video> findAll();

    @Override
    Optional<Video> findById(Long aLong);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Video set Video.hit = Video.hit + 1 where Video.id = :videoId", nativeQuery = true)
    int upHit(@Param("videoId") Long videoId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Video set Video.like_cnt = Video.like_cnt + 1 where Video.id = :videoId", nativeQuery = true)
    int upLikeCnt(@Param("videoId") Long videoId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Video set Video.like_cnt = Video.like_cnt - 1 where Video.id = :videoId", nativeQuery = true)
    int downLikeCnt(@Param("videoId") Long videoId);

    @Query(value = "SELECT * FROM Video join User on Video.posted_by = User.id and Video.posted_by = :userId " +
            "join Category on Video.category_id = Category.id and Video.category_id = :categoryId " +
            "order by Video.show_id, Video.id limit :count offset :page", nativeQuery = true)
    List<Video> getMyVideoList(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("page")Long page, @Param("count") Long count);

    @Query(value = "SELECT * FROM Video join User on Video.posted_by = User.id " +
            "join Category on Video.category_id = Category.id and Video.category_id = :categoryId " +
            "order by Video.show_id, Video.id limit :count offset :page", nativeQuery = true)
    List<Video> getMainVideoList(@Param("categoryId") Long categoryId, @Param("page")Long page, @Param("count") Long count);

    @Query(value = "SELECT * FROM Video " +
            "join Category on Video.category_id = Category.id and Video.category_id = :categoryId " +
            "order by Video.show_id, Video.id limit :count offset :page", nativeQuery = true)
    List<Video> getAnonymousUserMain(@Param("categoryId") Long categoryId, @Param("page")Long page, @Param("count") Long count);

    @Query(value = "SELECT * FROM Video join User " +
            "join Likes on Likes.liked_by = :userId and likes.liked_on = Video.id and User.id = :userId " +
            "join Category on Video.category_id = Category.id and Video.category_id = :categoryId " +
            "order by Video.show_id, Video.id limit :count offset :page", nativeQuery = true)
    List<Video> getLikedVideoList(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("page")Long page, @Param("count") Long count);
}