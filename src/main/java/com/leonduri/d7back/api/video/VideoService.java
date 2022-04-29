package com.leonduri.d7back.api.video;

import com.leonduri.d7back.api.video.dto.VideoListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;

    public List<VideoListResponseDto> getMyVideoList(Long userId, Long categoryId, Long page) {

        List<Video> videoList = videoRepository.getMyVideoList(userId, categoryId, (page - 1) * 6L, 6L);
        return videoRepository.getMyVideoList(userId, categoryId, (page - 1) * 6L, 6L).stream().map(VideoListResponseDto::new).collect(Collectors.toList());
    }
}
