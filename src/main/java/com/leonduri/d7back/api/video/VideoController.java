package com.leonduri.d7back.api.video;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.leonduri.d7back.api.video.dto.MyVideoListRequestDto;
import com.leonduri.d7back.api.video.dto.VideoListResponseDto;
import com.leonduri.d7back.utils.VideoApiResponse;
import com.leonduri.d7back.utils.VideoListApiResponse;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = {"2. Video"})
@RequiredArgsConstructor
@RestController
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

    @ApiOperation(value = "비디오 리스트 조회", notes = "비디오를 6개씩 조회한다.")
    @GetMapping(value = "/videos")
    public VideoListApiResponse<VideoListResponseDto> getMyVideoList(@RequestBody @ApiParam(required = true)
                                                                                 MyVideoListRequestDto myVideoListRequestDto){
        Long userId = myVideoListRequestDto.getUserId();
        Long categoryId = myVideoListRequestDto.getCate();
        Long page = myVideoListRequestDto.getPage();
        List<VideoListResponseDto> videoListResponseDtos = videoService.getMyVideoList(userId, categoryId, page);

        return VideoListApiResponse.success(videoListResponseDtos);
    }
}
