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
import java.util.List;

@Api(tags = {"2. Video"})
@RequiredArgsConstructor
@RestController
public class VideoController {

    private final VideoService videoService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "비디오 리스트 조회", notes = "비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos")
    public VideoListApiResponse<VideoListResponseDto> getVideoList(@ApiParam(value = "유저 Id", required = true) @RequestParam("userId") Long userId,
                                                                   @ApiParam(value = "카테고리 Id", required = true) @RequestParam("cate") Long categoryId,
                                                                   @ApiParam(value = "페이지 번호", required = true) @RequestParam("page") Long page) {
        List<VideoListResponseDto> videoListResponseDtos = videoService.getVideoList(userId, categoryId, page);

        return VideoListApiResponse.success(videoListResponseDtos);
    }

    @Secured("ROLE_USER")
    @ApiImplicitParam(name = "Authorization", value = "accessToken",
            required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "비디오 업로드", notes = "비디오를 업로드한다.")
    @PostMapping(value = "/videos")
    public SingleApiResponse<VideoSimpleResponseDto> saveVideo(
            HttpServletRequest request,
            @RequestPart(value = "videoInfo") VideoSaveRequestDto requestDto,
            @RequestPart(required = true) MultipartFile video,
            @RequestPart(required = true) MultipartFile thumbnail
    ) throws Exception {

        String jwt = jwtTokenProvider.resolveToken(request);
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
    @ApiImplicitParam(name = "Authorization", value = "accessToken",
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

        String jwt = jwtTokenProvider.resolveToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();

        if (!video.isEmpty() && !video.getContentType().startsWith("video")) throw new CWrongMediaFormatException();
        if (!thumbnail.isEmpty() && !thumbnail.getContentType().startsWith("image")) throw new CWrongMediaFormatException();

        return SingleApiResponse.success(videoService.updateVideo(
                Long.parseLong(jwtTokenProvider.getUserPk(jwt)), videoId, requestDto, video, thumbnail));
    }
}
