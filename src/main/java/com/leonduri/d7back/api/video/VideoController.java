package com.leonduri.d7back.api.video;

import com.leonduri.d7back.api.video.dto.VideoListResponseDto;
import com.leonduri.d7back.api.video.dto.VideoSaveRequestDto;
import com.leonduri.d7back.api.video.dto.VideoSimpleResponseDto;
import com.leonduri.d7back.api.video.dto.VideoUpdateRequestDto;
import com.leonduri.d7back.config.security.JwtTokenProvider;
import com.leonduri.d7back.utils.SingleApiResponse;
import com.leonduri.d7back.utils.VideoListApiResponse;
import com.leonduri.d7back.utils.exception.CInvalidJwtTokenException;
import com.leonduri.d7back.utils.exception.CWrongMediaFormatException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import com.leonduri.d7back.api.likes.LikesRepository;
import com.leonduri.d7back.api.video.dto.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = {"2. Video"})
@RestController
@RequiredArgsConstructor
public class VideoController {
//    private final VideoRepository videoRepository;
    private final VideoService videoService;
    private final LikesRepository likesRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //    userId 임시
    @ApiOperation(value = "비디오 디테일 조회", notes = "비디오 하나의 디테일을 조회한다.")
    @GetMapping(value = "/videos/{videoId}/{userId}")
    public SingleApiResponse<VideoDetailResponseDto> getVideoById(
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId,
            @PathVariable @ApiParam(value = "유저 Id", required = true) long userId) throws Exception {
        videoService.upHit(videoId);
        return SingleApiResponse.success(videoService.findVideoById(videoId, userId));
    }

    //TODO 파일 삭제 추가 필요
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
    public VideoListApiResponse<VideoListResponseDto> getVideoList(@ApiParam(value = "유저 Id", required = true) @RequestParam("userId") Long userId,
                                                                   @ApiParam(value = "카테고리 Id", required = true) @RequestParam("cate") Long categoryId,
                                                                   @ApiParam(value = "페이지 번호", required = true) @RequestParam("page") Long page) {
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
  
    @Secured("ROLE_USER")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "accessToken",
            required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "비디오 업로드", notes = "비디오를 업로드한다.")
    @PostMapping(value = "/videos")
    public SingleApiResponse<VideoSimpleResponseDto> saveVideo(
            HttpServletRequest request,
            @RequestPart(value = "videoInfo") VideoSaveRequestDto requestDto,
            @RequestPart(required = true) MultipartFile video,
            @RequestPart(required = true) MultipartFile thumbnail
    ) throws Exception {

        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();

        // required fields in JSON (videoInfo)
        if (requestDto.getTitle() == null || requestDto.getLength() == 0 || requestDto.getCategoryId() == 0)
            throw new MissingServletRequestParameterException("", "");

        if (!video.getContentType().startsWith("video") || !thumbnail.getContentType().startsWith("image")) {
            throw new CWrongMediaFormatException();
        }

        return SingleApiResponse.success(videoService.saveVideo(
                requestDto, Long.parseLong(jwtTokenProvider.getUserPk(jwt)), video, thumbnail));
    }

    @Secured("ROLE_USER")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "accessToken",
            required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "비디오 수정", notes = "비디오를 수정한다.")
    @PutMapping(value = "/videos/{videoId}")
    public SingleApiResponse<VideoSimpleResponseDto> updateVideo(
            HttpServletRequest request,
            @PathVariable(value = "videoId", required = true) long videoId,
            @RequestPart(value = "videoUpdateInfo", required = true) VideoUpdateRequestDto requestDto,
            @RequestPart(required = false) MultipartFile video,
            @RequestPart(required = false) MultipartFile thumbnail
    ) throws Exception {

        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();

        if (!video.isEmpty() && !video.getContentType().startsWith("video")) throw new CWrongMediaFormatException();
        if (!thumbnail.isEmpty() && !thumbnail.getContentType().startsWith("image")) throw new CWrongMediaFormatException();

        return SingleApiResponse.success(videoService.updateVideo(
                Long.parseLong(jwtTokenProvider.getUserPk(jwt)), videoId, requestDto, video, thumbnail));
    }
}
