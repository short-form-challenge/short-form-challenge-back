package com.leonduri.d7back.api.video;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.category.CategoryRepository;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.user.UserRepository;
import com.leonduri.d7back.api.video.dto.VideoListResponseDto;
import com.leonduri.d7back.api.video.dto.VideoSaveRequestDto;
import com.leonduri.d7back.api.video.dto.VideoSimpleResponseDto;
import com.leonduri.d7back.utils.FileSystemStorageService;
import com.leonduri.d7back.utils.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final FileSystemStorageService fileSystemStorageService;

    public List<VideoListResponseDto> getVideoList(Long userId, Long categoryId, Long page) {

        List<Video> videoList = videoRepository.getVideoList(userId, categoryId, (page - 1) * 6L, 6L);
        return videoRepository.getVideoList(userId, categoryId, (page - 1) * 6L, 6L).stream().map(VideoListResponseDto::new).collect(Collectors.toList());
    }

    public VideoSimpleResponseDto saveVideo(VideoSaveRequestDto requestDto, long userId,
                                            MultipartFile video, MultipartFile thumbnail) throws Exception {
        User u = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        Category c = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(Exception::new);
        LocalDateTime postedAt = LocalDateTime.now();
        Video v = requestDto.toEntity(
                fileSystemStorageService.storeVideo(video, u.getEmail(), postedAt),
                fileSystemStorageService.storeThumbnail(thumbnail, u.getEmail(), postedAt),
                u, c, postedAt
        );
        v = videoRepository.save(v);
        v.setShowId(v.getId());
        videoRepository.save(v);
        return new VideoSimpleResponseDto(v);
    }
}
