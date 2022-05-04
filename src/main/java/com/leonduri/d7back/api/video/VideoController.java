package com.leonduri.d7back.api.video;

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

    @ApiOperation(value = "비디오 디테일 조회", notes = "비디오 하나의 디테일을 조회한다.")
    @GetMapping(value = "/videos/{videoId}/{userId}")
    public SingleApiResponse<VideoDetailResponseDto> getVideoById(
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId,
            @PathVariable @ApiParam(value = "유저 Id", required = true) long userId) throws Exception {
        videoService.upHit(videoId);
        return SingleApiResponse.success(videoService.findVideoById(videoId, userId));
    }

    @ApiOperation(value = "비디오 삭제", notes = "비디오 id를 기준으로 비디오 하나를 삭제한다.")
    @DeleteMapping(value = "videos/{videoId}")
    public void deleteVideoById(
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId) throws Exception {
        videoService.deleteVideoById(videoId);
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
            @RequestBody @ApiParam(value = "로그인을 한 특정 유저의 메인 비디오 리스트", required = true) VideoListRequestDto videoListRequestDto){
        Long userId = videoListRequestDto.getUserId();
        Long categoryId = videoListRequestDto.getCate();
        Long page = videoListRequestDto.getPage();

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
    public VideoListApiResponse<AnonymousUserVideoListResponseDto> getAnonymousMainVideoList(@RequestBody @ApiParam(value = "로그인을 하지 않은 익명 유저의 메인 비디오 리스트", required = true)
                                                                                                      AnonymousUserVideoListRequestDto requestDto){
        Long categoryId = requestDto.getCate();
        Long page = requestDto.getPage();

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
    public VideoListApiResponse<VideoListResponseDto> getLikedVideoList(@RequestBody @ApiParam(value = "특정 유저의 관심 비디오 리스트", required = true)
                                                                                    VideoListRequestDto videoListRequestDto){
        Long userId = videoListRequestDto.getUserId();
        Long categoryId = videoListRequestDto.getCate();
        Long page = videoListRequestDto.getPage();

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
