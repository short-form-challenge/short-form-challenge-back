package com.leonduri.d7back.api.video;

import com.leonduri.d7back.api.likes.Likes;
import com.leonduri.d7back.api.likes.LikesRepository;
import com.leonduri.d7back.api.video.dto.*;
import com.leonduri.d7back.utils.SingleApiResponse;
import com.leonduri.d7back.utils.VideoListApiResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = {"2. Video"})
@RestController
@RequiredArgsConstructor
public class VideoController {
//    private final VideoRepository videoRepository;
    private final VideoService videoService;
    private final LikesRepository likesRepository;


//    @ApiOperation(value = "비디오 조회", notes = "모든 비디오를 조회한다.")
//    @GetMapping(value = "/videos")
//    public List<Video> videoList() {
//        return videoRepository.findAll();
//    }
//
//    @ApiOperation(value = "비디오 한개 조회", notes = "id에 맞는 비디오 하나를 조회한다.")
//    @GetMapping(value = "/videos/{videoId}")
//    public Optional<Video> findById(@ApiParam(name = "비디오 ID", required = true) @PathVariable Long videoId) {
//        return videoRepository.findById(videoId);
//    }

    //    userId 임시
    @ApiOperation(value = "비디오 디테일 조회", notes = "비디오 하나의 디테일을 조회한다.")
    @GetMapping(value = "/videos/{videoId}/{userId}")
    public SingleApiResponse<VideoDetailResponseDto> getVideoById(
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId,
            @PathVariable @ApiParam(value = "유저 Id", required = true) long userId) throws Exception {
        videoService.upHit(videoId);
        return SingleApiResponse.success(videoService.findVideoById(videoId, userId));
    }

    //TODO    파일 삭제 추가 필요
    @ApiOperation(value = "비디오 삭제", notes = "비디오 id를 기준으로 비디오 하나를 삭제한다.")
    @DeleteMapping(value = "videos/{videoId}")
    public void deleteVideoById(
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId) throws Exception {
        videoService.deleteVideoById(videoId);
    }

    //    userId 임시
    @ApiOperation(value = "like 증가", notes = "해당 비디오의 like를 증가시킨다.")
    @PostMapping(value = "/videos/upLikes/{videoId}/{userId}")
    public SingleApiResponse<VideoLikesResponseDto> updateUpVideoLikes(
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId,
            @PathVariable @ApiParam(value = "유저 Id", required = true)long userId) throws Exception {
        videoService.upLikeCnt(videoId, userId);
        return SingleApiResponse.success(videoService.findById(videoId, userId));
    }

    @ApiOperation(value = "like 감소", notes = "해당 비디오의 like를 감소시킨다.")
    @PostMapping(value = "/videos/downLikes/{videoId}/{userId}")
    public SingleApiResponse<VideoLikesResponseDto> updateDownVideoLikes(
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId,
            @PathVariable @ApiParam(value = "유저Id", required = true) long userId) throws Exception {
        videoService.downLikeCnt(videoId, userId);

        return SingleApiResponse.success(videoService.findById(videoId, userId));
    }


    //   GetMapping api 이름
    @ApiOperation(value = "나의 비디오 리스트 조회", notes = "비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos/myVideos")
    public VideoListApiResponse<VideoListResponseDto> getMyVideoList(
            @RequestParam("userId") @ApiParam(value = "유저 Id", required = true) Long userId,
            @RequestParam("cate") @ApiParam(value = "카테고리 Id", required = true) Long categoryId,
            @RequestParam("page") @ApiParam(value = "페이지 번호", required = true) Long page) {

        List<VideoListResponseDto> videoListResponseDtos = videoService.getMyVideoList(userId, categoryId, page);
        List<VideoListResponseDto> ret = new ArrayList<>();

        for (int i = 0; i < videoListResponseDtos.size(); i++) {
            if (i != 6) {
                ret.add(videoListResponseDtos.get(i));
            }
            if (i == 6) {
                return VideoListApiResponse.lastSuccess(ret);
            }
        }

        return VideoListApiResponse.success(ret);
    }

    @ApiOperation(value = "메인 비디오 리스트 조회", notes = "비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos")
    public VideoListApiResponse<VideoListResponseDto> getMainVideoList(
            @RequestParam("userId") @ApiParam(value = "유저 Id", required = true) Long userId,
            @RequestParam("cate") @ApiParam(value = "카테고리 Id", required = true) Long categoryId,
            @RequestParam("page") @ApiParam(value = "페이지 번호", required = true) Long page) {

        List<VideoListResponseDto> videoListResponseDtos = videoService.getMainVideoList(userId, categoryId, page);
        List<VideoListResponseDto> ret = new ArrayList<>();

        for (int i = 0; i < videoListResponseDtos.size(); i++) {
            if (i != 6) {
                ret.add(videoListResponseDtos.get(i));
            }
            if (i == 6) {
                return VideoListApiResponse.lastSuccess(ret);
            }
        }

        return VideoListApiResponse.success(ret);
    }

    //    Login하지 않은 회원
    @ApiOperation(value = "로그아웃 회원의 메인 비디오 리스트 조회", notes = "비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos/anonymousUsers")
    public VideoListApiResponse<AnonymousUserVideoListResponseDto> getAnonymousMainVideoList(
            @RequestParam("cate") @ApiParam(value = "카테고리 Id", required = true) Long categoryId,
            @RequestParam("page") @ApiParam(value = "페이지 번호", required = true) Long page) {

        List<AnonymousUserVideoListResponseDto> videoListResponseDtos = videoService.getAnonymousUserMain(categoryId, page);
        List<AnonymousUserVideoListResponseDto> ret = new ArrayList<>();

        for (int i = 0; i < videoListResponseDtos.size(); i++) {
            if (i != 6) {
                ret.add(videoListResponseDtos.get(i));
            }
            if (i == 6) {
                return VideoListApiResponse.lastSuccess(ret);
            }
        }

        return VideoListApiResponse.success(ret);
    }

    @ApiOperation(value = "관심 비디오 리스트 조회", notes = "비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos/likeVideos")
    public VideoListApiResponse<VideoListResponseDto> getLikedVideoList(
            @RequestParam("userId") @ApiParam(value = "유저 Id", required = true) Long userId,
            @RequestParam("cate") @ApiParam(value = "카테고리 Id", required = true) Long categoryId,
            @RequestParam("page") @ApiParam(value = "페이지 번호", required = true) Long page){

        List<VideoListResponseDto> videoListResponseDtos = videoService.getLikedVideoList(userId, categoryId, page);
        List<VideoListResponseDto> ret = new ArrayList<>();

        for (int i = 0; i < videoListResponseDtos.size(); i++) {
            if (i != 6) {
                ret.add(videoListResponseDtos.get(i));
            }
            if (i == 6) {
                return VideoListApiResponse.lastSuccess(ret);
            }
        }

        return VideoListApiResponse.success(ret);
    }
}
