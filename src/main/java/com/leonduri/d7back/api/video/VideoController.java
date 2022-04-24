package com.leonduri.d7back.api.video;

import com.fasterxml.jackson.annotation.OptBoolean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Api(tags = {"2. Video"})
@RestController
@RequiredArgsConstructor
public class VideoController {
    private final VideoRepository videoRepository;

    @ApiOperation(value = "비디오 조회", notes = "모든 비디오를 조회한다.")
    @GetMapping(value = "/videos")
    public List<Video> videoList() {
        return videoRepository.findAll();
    }

    @ApiOperation(value = "비디오 한개 조회", notes = "id에 맞는 비디오 하나를 조회한다.")
    @GetMapping(value = "/videos/{videoId}")
    public Optional<Video> findById(@ApiParam(name = "비디오 ID", required = true) @PathVariable Long videoId) {
        return videoRepository.findById(videoId);
    }
}
