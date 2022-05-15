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

//    비디오 조회수 증가 API
    @Modifying
    @Transactional
    @Query(value = "UPDATE Video set Video.hit = Video.hit + 1 where Video.id = :videoId", nativeQuery = true)
    int upHit(@Param("videoId") Long videoId);

//    비디오 삭제 API
    @Override
    void deleteById(Long aLong);

    //    비디오 리스트 조회 API
    @Modifying
    @Transactional
    @Query(value = "UPDATE Video set Video.like_cnt = :likeCount where Video.id = :videoId", nativeQuery = true)
    int changeLikeCnt(@Param("videoId") Long videoId, @Param("likeCount") Long likeCount);

//    TODO showId 정렬하고 lastId 기준으로 count개 가져오기
    @Query(value = "SELECT * FROM Video join User on Video.posted_by = User.id and Video.posted_by = :userId " +
            "where Video.show_id >= :showId order by Video.show_id, Video.id desc limit :count", nativeQuery = true)
    List<Video> getMyVideoList(@Param("userId") Long userId, @Param("showId")Long showId, @Param("count") Integer count);

    @Query(value = "SELECT * FROM Video join User on Video.posted_by = User.id " +
            "join Category on Video.category_id = Category.id and Video.category_id = :categoryId " +
            "where Video.show_id >= :showId order by Video.show_id, Video.id desc limit :count", nativeQuery = true)
    List<Video> getMainVideoList(@Param("categoryId") Long categoryId, @Param("showId")Long showId, @Param("count") Integer count);

    @Query(value = "SELECT * FROM Video " +
            "join Category on Video.category_id = Category.id and Video.category_id = :categoryId " +
            "where Video.show_id >= :showId order by Video.show_id, Video.id desc limit :count", nativeQuery = true)
    List<Video> getAnonymousUserMain(@Param("categoryId") Long categoryId, @Param("showId")Long showId, @Param("count") Integer count);

    @Query(value = "SELECT * FROM Video join User " +
            "join Likes on Likes.liked_by = :userId and likes.liked_on = Video.id and User.id = :userId " +
            "where Video.show_id >= :showId order by Video.show_id, Video.id desc limit :count", nativeQuery = true)
    List<Video> getLikedVideoList(@Param("userId") Long userId, @Param("showId")Long showId, @Param("count") Integer count);

    //Admin API
    @Modifying
    @Transactional
    @Query(value = "delete from video where video.posted_by = :userId", nativeQuery = true)
    void deleteVideosByPostedBy(@Param("userId") Long postedBy);

    @Query(value = "SELECT * FROM Video join User on Video.posted_by = User.id and Video.posted_by = :userId " +
            "order by Video.id limit :count offset :page", nativeQuery = true)
    List<Video> getAdminUserVideoList(@Param("userId") Long userId, @Param("page")Long page, @Param("count") Integer count);

    @Query(value = "select count(id) from Video where Video.posted_by = :userId", nativeQuery = true)
    Long countUserVideo(@Param("userId") Long userId);

    @Query(value = "select count(id) from Video", nativeQuery = true)
    Long countAllVideo();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Video set Video.show_id = :newShowId where Video.id = :videoId", nativeQuery = true)
    int changedShowId(@Param("newShowId") Long newShowId, @Param("videoId") Long videoId);
}