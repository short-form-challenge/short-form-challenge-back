package com.leonduri.d7back.api.video;

import com.leonduri.d7back.api.video.dto.VideoListResponseDto;
import com.leonduri.d7back.api.video.dto.VideoSaveRequestDto;
import com.leonduri.d7back.api.video.dto.VideoSimpleResponseDto;
import com.leonduri.d7back.api.video.dto.VideoUpdateRequestDto;
import com.leonduri.d7back.config.security.JwtTokenProvider;
import com.leonduri.d7back.utils.*;
import com.leonduri.d7back.utils.exception.CInvalidJwtTokenException;
import com.leonduri.d7back.utils.exception.CNoPermissionException;
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

    public int size = 10;
    public int adminSize = 10;

    //    userId 임시
    @ApiOperation(value = "비디오 디테일 조회", notes = "비디오 하나의 디테일을 조회한다.")
    @GetMapping(value = "/videos/{videoId}")
    public SingleApiResponse<VideoDetailResponseDto> getVideoById(
            HttpServletRequest request,
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId) throws Exception {
        String accessToken = request.getHeader("X-AUTH-TOKEN");
        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();

        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(jwt));

        videoService.upHit(videoId);
        return SingleApiResponse.success(videoService.findVideoById(videoId, userId));
    }

    @ApiOperation(value = "비디오 삭제", notes = "비디오 id를 기준으로 비디오 하나를 삭제한다.")
    @DeleteMapping(value = "/videos/{videoId}")
    public ApiResponse deleteVideoById(
            HttpServletRequest request,
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId) throws Exception {
        String accessToken = request.getHeader("X-AUTH-TOKEN");
        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();
        videoService.deleteVideoById(videoId, Long.parseLong(jwtTokenProvider.getUserPk(jwt)));
        return ApiResponse.success("해당 video " + videoId + "를 삭제하였습니다.");
    }

    //    userId 임시
    @ApiOperation(value = "like 증가", notes = "해당 비디오의 like를 증가시킨다.")
    @PostMapping(value = "/videos/upLikes/{videoId}")
    public SingleApiResponse<VideoLikesResponseDto> updateUpVideoLikes(
            HttpServletRequest request,
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId) throws Exception {
        String accessToken = request.getHeader("X-AUTH-TOKEN");
        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();

        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(jwt));
        videoService.upLikeCnt(videoId, userId);
        return SingleApiResponse.success(videoService.findUserById(videoId, userId));
    }

    @ApiOperation(value = "like 감소", notes = "해당 비디오의 like를 감소시킨다.")
    @PostMapping(value = "/videos/downLikes/{videoId}")
    public SingleApiResponse<VideoLikesResponseDto> updateDownVideoLikes(
            HttpServletRequest request,
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId) throws Exception {

        String accessToken = request.getHeader("X-AUTH-TOKEN");
        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();

        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(jwt));
        videoService.downLikeCnt(videoId, userId);
        return SingleApiResponse.success(videoService.findUserById(videoId, userId));
    }


    //   GetMapping api 이름
    @ApiOperation(value = "나의 비디오 리스트 조회", notes = "내가 올린 비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos/myVideos")
    public VideoListApiResponse<VideoListResponseDto> getMyVideoList(
            HttpServletRequest request,
            @RequestParam("showId") @ApiParam(value = "마지막 비디오의 showId", required = true) Long showId,
            @RequestParam("lastId") @ApiParam(value = "마지막 비디오의 lastId", required = true) Long lastId) {

        String accessToken = request.getHeader("X-AUTH-TOKEN");
        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();

        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(jwt));

        List<VideoListResponseDto> videoListResponseDtos = videoService.getMyVideoList(userId, showId, lastId, size);
        List<VideoListResponseDto> ret = new ArrayList<>();

        for (int i = 0; i < videoListResponseDtos.size(); i++) {
            if (i != size) {
                ret.add(videoListResponseDtos.get(i));
            }
            if (i == size) {
                return VideoListApiResponse.lastSuccess(ret);
            }
        }

        return VideoListApiResponse.success(ret);
    }

    @ApiOperation(value = "다른 유저의 비디오 리스트 조회", notes = "해당 유저가 올린 비디오를 10개씩 조회한다.")
    @GetMapping(value = "/videos/otherVideos")
    public VideoListApiResponse<VideoListResponseDto> getOtherVideoList(
            @RequestParam("userId") @ApiParam(value = "유저의 Id", required = true) Long userId,
            @RequestParam("showId") @ApiParam(value = "마지막 비디오의 showId", required = true) Long showId,
            @RequestParam("lastId") @ApiParam(value = "마지막 비디오의 lastId", required = true) Long lastId) {

        List<VideoListResponseDto> videoListResponseDtos = videoService.getMyVideoList(userId, showId, lastId, size);
        List<VideoListResponseDto> ret = new ArrayList<>();

        for (int i = 0; i < videoListResponseDtos.size(); i++) {
            if (i != size) {
                ret.add(videoListResponseDtos.get(i));
            }
            if (i == size) {
                return VideoListApiResponse.lastSuccess(ret);
            }
        }

        return VideoListApiResponse.success(ret);
    }

    @ApiOperation(value = "메인 비디오 리스트 조회", notes = "메인 비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos")
    public VideoListApiResponse<VideoListResponseDto> getVideoList(
            HttpServletRequest request,
            @ApiParam(value = "카테고리 Id", required = true) @RequestParam("cate") Long categoryId,
            @ApiParam(value = "마지막 video의 showId", required = true) @RequestParam("showId") Long showId,
            @ApiParam(value = "마지막 video의 Id", required = true) @RequestParam("lastId") Long lastId) {

        String accessToken = request.getHeader("X-AUTH-TOKEN");
        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();

        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(jwt));

        List<VideoListResponseDto> videoListResponseDtos = videoService.getMainVideoList(userId, categoryId, showId, lastId, size);
        List<VideoListResponseDto> ret = new ArrayList<>();

        for (int i = 0; i < videoListResponseDtos.size(); i++) {
            if (i != size) {
                ret.add(videoListResponseDtos.get(i));
            }
            if (i == size) {
                return VideoListApiResponse.lastSuccess(ret);
            }
        }

        return VideoListApiResponse.success(ret);
    }

    // Login하지 않은 회원
    @ApiOperation(value = "로그아웃 회원의 메인 비디오 리스트 조회", notes = "로그인을 하지 않은 유저에게 메인 비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos/anonymousUsers")
    public VideoListApiResponse<AnonymousUserVideoListResponseDto> getAnonymousMainVideoList(
            @RequestParam("cate") @ApiParam(value = "카테고리 Id", required = true) Long categoryId,
            @RequestParam("showId") @ApiParam(value = "마지막 video의 showId", required = true) Long showId,
            @RequestParam("lastId") @ApiParam(value = "마지막 video의 lastId", required = true) Long lastId) {

        List<AnonymousUserVideoListResponseDto> videoListResponseDtos = videoService.getAnonymousUserMain(categoryId, showId, lastId, size);
        List<AnonymousUserVideoListResponseDto> ret = new ArrayList<>();

        for (int i = 0; i < videoListResponseDtos.size(); i++) {
            if (i != size) {
                ret.add(videoListResponseDtos.get(i));
            }
            if (i == size) {
                return VideoListApiResponse.lastSuccess(ret);
            }
        }

        return VideoListApiResponse.success(ret);
    }

    @ApiOperation(value = "관심 비디오 리스트 조회", notes = "내가 좋아요를 누른 비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos/likeVideos")
    public VideoListApiResponse<VideoListResponseDto> getLikedVideoList(
            HttpServletRequest request,
            @RequestParam("showId") @ApiParam(value = "마지막 video의 showId", required = true) Long showId,
            @RequestParam("lastId") @ApiParam(value = "마지막 video의 lastId", required = true) Long lastId) {

        String accessToken = request.getHeader("X-AUTH-TOKEN");
        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();

        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(jwt));

        List<VideoListResponseDto> videoListResponseDtos = videoService.getLikedVideoList(userId, showId, lastId, size);
        List<VideoListResponseDto> ret = new ArrayList<>();

        for (int i = 0; i < videoListResponseDtos.size(); i++) {
            if (i != size) {
                ret.add(videoListResponseDtos.get(i));
            }
            if (i == size) {
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
            @RequestPart(name = "videoInfo", value = "videoInfo") VideoSaveRequestDto requestDto,
            @RequestPart(required = true) MultipartFile video,
            @RequestPart(required = true) MultipartFile thumbnail
    ) throws Exception {
        String accessToken = request.getHeader("X-AUTH-TOKEN");

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
            @RequestPart(name = "videoUpdateInfo", value = "videoUpdateInfo", required = true) VideoUpdateRequestDto requestDto,
            @RequestPart(required = false) MultipartFile video,
            @RequestPart(required = false) MultipartFile thumbnail
    ) throws Exception {

        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();

        if (!video.isEmpty() && !video.getContentType().startsWith("video")) throw new CWrongMediaFormatException();
        if (!thumbnail.isEmpty() && !thumbnail.getContentType().startsWith("image"))
            throw new CWrongMediaFormatException();

        return SingleApiResponse.success(videoService.updateVideo(
                Long.parseLong(jwtTokenProvider.getUserPk(jwt)), videoId, requestDto, video, thumbnail));
    }

    @ApiOperation(value = "Admin page : 유저의 비디오 삭제", notes = "유저 정보로 전체 비디오 삭제")
    @DeleteMapping(value = "/admin/{userId}")
    public ApiResponse deleteVideosByPostedBy(
            @PathVariable @ApiParam(value = "유저 Id", required = true) long userId) throws Exception {
        videoService.deleteVideosByPostedBy(userId);
        return ApiResponse.success("해당 user " + userId + "가 게시한 비디오를 모두 삭제하였습니다.");
    }

    //        API 두개 호출 필요
    @ApiOperation(value = "Admin page : 유저의 비디오 리스트 조회", notes = "유저 정보로 비디오를 10개씩 조회한다.")
    @GetMapping(value = "/admin/videos/{userId}/{page}")
    public AdminVideoListApiResponse<AdminVideoListResponseDto> getAdminUserVideoList(
            @PathVariable @ApiParam(value = "유저 Id", required = true) long userId,
            @PathVariable @ApiParam(value = "페이지 번호", required = true) long page) throws Exception {

        List<AdminVideoListResponseDto> videoListResponseDtos = videoService.findAdminUserVideoList(userId, page, adminSize);
        List<AdminVideoListResponseDto> ret = new ArrayList<>();
        Long videoCount = videoService.countUserVideo(userId);

        for (int i = 0; i < videoListResponseDtos.size(); i++) {
            if (i != adminSize) {
                ret.add(videoListResponseDtos.get(i));
            }
            if (i == adminSize) {
                return AdminVideoListApiResponse.lastSuccess(ret, videoCount);
            }
        }

        return AdminVideoListApiResponse.success(videoListResponseDtos, videoCount);
    }

    @ApiOperation(value = "Admin page : 메인 비디오 조회", notes = "메인 비디오를 10개씩 전체 조회를 한다.")
    @GetMapping(value = "/admin/videos/{page}")
    public AdminVideoListApiResponse<AdminMainVideoListResponseDto> getAdminVideosList(
            @PathVariable Integer page
    ) {
        List<AdminMainVideoListResponseDto> videoListResponseDtos = videoService.findAllVideoList(page, adminSize);
        List<AdminMainVideoListResponseDto> ret = new ArrayList<>();
        Long videoCount = videoService.countAllVideo();

        for (int i = 0; i < videoListResponseDtos.size(); i++) {
            if (i != adminSize) {
                ret.add(videoListResponseDtos.get(i));
            }
            if (i == adminSize) {
                return AdminVideoListApiResponse.lastSuccess(ret, videoCount);
            }
        }

        return AdminVideoListApiResponse.success(videoListResponseDtos, videoCount);
    }

    @ApiOperation(value = "showId 변경", notes = "admin이 비디오의 showId를 변경한다")
    @PutMapping(value = "/admin/videos/{videoId}")
    public SingleApiResponse<AdminVideoDetailResponseDto> updateShowId(
            @RequestParam @ApiParam(value = "show Id", required = true) long showId,
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId) throws Exception {
        videoService.changedShowId(showId, videoId);

        return SingleApiResponse.success(videoService.findById(videoId));
    }

    @ApiOperation(value = "비디오 디테일 조회", notes = "비디오 하나의 디테일을 조회한다.")
    @GetMapping(value = "/admin/videoDetails/{videoId}")
    public SingleApiResponse<AdminVideoDetailResponseDto> getAdminVideoById(
            @PathVariable @ApiParam(value = "비디오 Id", required = true) long videoId) throws Exception {
        return SingleApiResponse.success(videoService.findById(videoId));
    }
}
