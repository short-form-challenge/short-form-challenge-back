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
    @Query(value = "UPDATE video set video.hit = video.hit + 1 where video.id = :videoId", nativeQuery = true)
    int upHit(@Param("videoId") Long videoId);

//    비디오 삭제 API
    @Override
    void deleteById(Long aLong);

    //    비디오 리스트 조회 API
    @Modifying
    @Transactional
    @Query(value = "UPDATE video set video.like_cnt = :likeCount where video.id = :videoId", nativeQuery = true)
    int changeLikeCnt(@Param("videoId") Long videoId, @Param("likeCount") Long likeCount);

//    TODO showId 정렬하고 lastId 기준으로 count개 가져오기
    @Query(value = "SELECT * FROM video join user on video.posted_by = user.id and video.posted_by = :userId " +
            "where video.show_id >= :showId order by video.show_id, video.id desc limit :count", nativeQuery = true)
    List<Video> getMyVideoList(@Param("userId") Long userId, @Param("showId")Long showId, @Param("count") Integer count);

    @Query(value = "SELECT * FROM video join User on video.posted_by = user.id " +
            "join category on video.category_id = category.id and video.category_id = :categoryId " +
            "where video.show_id >= :showId order by video.show_id, video.id desc limit :count", nativeQuery = true)
    List<Video> getMainVideoList(@Param("categoryId") Long categoryId, @Param("showId")Long showId, @Param("count") Integer count);

    @Query(value = "SELECT * FROM video " +
            "join category on video.category_id = category.id and video.category_id = :categoryId " +
            "where video.show_id >= :showId order by video.show_id, video.id desc limit :count", nativeQuery = true)
    List<Video> getAnonymousUserMain(@Param("categoryId") Long categoryId, @Param("showId")Long showId, @Param("count") Integer count);

    @Query(value = "SELECT * FROM video join User " +
            "join likes on likes.liked_by = :userId and likes.liked_on = video.id and user.id = :userId " +
            "where video.show_id >= :showId order by video.show_id, video.id desc limit :count", nativeQuery = true)
    List<Video> getLikedVideoList(@Param("userId") Long userId, @Param("showId")Long showId, @Param("count") Integer count);

    //Admin API
    @Modifying
    @Transactional
    @Query(value = "delete from video where video.posted_by = :userId" , nativeQuery = true)
    void deleteVideosByPostedBy(@Param("userId") Long postedBy);

    @Query(value = "SELECT * FROM video join User on video.posted_by = user.id and video.posted_by = :userId " +
            "order by video.id limit :count offset :page", nativeQuery = true)
    List<Video> getAdminUserVideoList(@Param("userId") Long userId, @Param("page")Long page, @Param("count") Integer count);

    @Query(value = "select count(id) from video where video.posted_by = :userId", nativeQuery = true)
    Long countUserVideo(@Param("userId") Long userId);

    @Query(value = "select count(id) from video", nativeQuery = true)
    Long countAllVideo();

    @Modifying
    @Transactional
    @Query(value = "UPDATE video set video.show_id = :newShowId where video.id = :videoId", nativeQuery = true)
    int changedShowId(@Param("newShowId") Long newShowId, @Param("videoId") Long videoId);
}