package com.leonduri.d7back.api.video;

import com.leonduri.d7back.api.likes.Likes;
import com.leonduri.d7back.api.likes.LikesRepository;
import com.leonduri.d7back.api.video.dto.*;
import com.leonduri.d7back.utils.SingleApiResponse;
import com.leonduri.d7back.utils.VideoListApiResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping(value = "/videos/{videoId}/{userId}")
    public SingleApiResponse<VideoDetailResponseDto> getVideoById(
            @PathVariable long videoId, @PathVariable long userId) throws Exception {
        videoService.upHit(videoId);
        return SingleApiResponse.success(videoService.findVideoById(videoId, userId));
    }

    //    userId 임시
    @PostMapping(value = "/videos/upLikes/{videoId}/{userId}")
    public SingleApiResponse<VideoLikesResponseDto> updateUpVideoLikes(
            @PathVariable long videoId, @PathVariable long userId) throws Exception {
        videoService.upLikeCnt(videoId, userId);
        return SingleApiResponse.success(videoService.findById(videoId, userId));
    }

    @PostMapping(value = "/videos/downLikes/{videoId}/{userId}")
    public SingleApiResponse<VideoLikesResponseDto> updateDownVideoLikes(
            @PathVariable long videoId, @PathVariable long userId) throws Exception {
        videoService.downLikeCnt(videoId, userId);
        return SingleApiResponse.success(videoService.findById(videoId, userId));
    }


//   GetMapping api 이름
    @ApiOperation(value = "나의 비디오 리스트 조회", notes = "비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos/myVideos")
    public VideoListApiResponse<VideoListResponseDto> getMyVideoList(
            @RequestBody @ApiParam(value = "특정 유저가 올린 마이 비디오 리스트", required = true) VideoListRequestDto videoListRequestDto) {
        Long userId = videoListRequestDto.getUserId();
        Long categoryId = videoListRequestDto.getCate();
        Long page = videoListRequestDto.getPage();
        List<VideoListResponseDto> videoListResponseDtos = videoService.getMyVideoList(userId, categoryId, page);

        return VideoListApiResponse.success(videoListResponseDtos);
    }

    @ApiOperation(value = "메인 비디오 리스트 조회", notes = "비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos")
    public VideoListApiResponse<VideoListResponseDto> getMainVideoList(
            @RequestBody @ApiParam(value = "로그인을 한 특정 유저의 메인 비디오 리스트", required = true) VideoListRequestDto videoListRequestDto){
        Long userId = videoListRequestDto.getUserId();
        Long categoryId = videoListRequestDto.getCate();
        Long page = videoListRequestDto.getPage();

        List<VideoListResponseDto> mainVideoListResponseDtos = videoService.getMainVideoList(userId, categoryId, page);
        return VideoListApiResponse.success(mainVideoListResponseDtos);
    }

//    Login하지 않은 회원
    @ApiOperation(value = "로그아웃 회원의 메인 비디오 리스트 조회", notes = "비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos/anonymousUsers")
    public VideoListApiResponse<AnonymousUserVideoListResponseDto> getAnonymousMainVideoList(@RequestBody @ApiParam(value = "로그인을 하지 않은 익명 유저의 메인 비디오 리스트", required = true)
                                                                                                      AnonymousUserVideoListRequestDto requestDto){
        Long categoryId = requestDto.getCate();
        Long page = requestDto.getPage();

        List<AnonymousUserVideoListResponseDto> videoList = videoService.getAnonymousUserMain(categoryId, page);

        return VideoListApiResponse.success(videoList);
    }

    @ApiOperation(value = "관심 비디오 리스트 조회", notes = "비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos/likeVideos")
    public VideoListApiResponse<VideoListResponseDto> getLikedVideoList(@RequestBody @ApiParam(value = "특정 유저의 관심 비디오 리스트", required = true)
                                                                                    VideoListRequestDto videoListRequestDto){
        Long userId = videoListRequestDto.getUserId();
        Long categoryId = videoListRequestDto.getCate();
        Long page = videoListRequestDto.getPage();

        List<VideoListResponseDto> videoListResponseDtos = videoService.getLikedVideoList(userId, categoryId, page);

        return VideoListApiResponse.success(videoListResponseDtos);
    }
}
