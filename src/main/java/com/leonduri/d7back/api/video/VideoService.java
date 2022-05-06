package com.leonduri.d7back.api.video;

import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.user.UserRepository;
import com.leonduri.d7back.api.video.dto.AnonymousUserVideoListResponseDto;
import com.leonduri.d7back.api.video.dto.VideoDetailResponseDto;
import com.leonduri.d7back.api.video.dto.VideoListResponseDto;
import com.leonduri.d7back.utils.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    Long count = 7L;
    public VideoDetailResponseDto findVideoById(Long videoId, Long userId) throws Exception {
        User requestUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        Optional<Video> video = videoRepository.findById(videoId);
        VideoDetailResponseDto videoListResponseDto = new VideoDetailResponseDto(video.get(), requestUser);
        return videoListResponseDto;
    }

    public int upHit(Long videoId) {
        return videoRepository.upHit(videoId);
    }

    public void deleteVideoById(Long videoId) {
        videoRepository.deleteById(videoId);
//        Exception 추가 필요
    }

    public List<VideoListResponseDto> getMyVideoList(Long userId, Long categoryId, Long page) {
        User requestUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        List<VideoListResponseDto> ret = new ArrayList<>();
        List<Video> vList = videoRepository.getMyVideoList(userId, categoryId, (page - 1) * (count -1), count);
        for (int i = 0; i < vList.size(); i++) {
            ret.add(new VideoListResponseDto(vList.get(i), requestUser));
        }

        return ret;
    }

    public List<VideoListResponseDto> getMainVideoList(Long userId, Long categoryId, Long page) {
        User requestUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        List<VideoListResponseDto> ret = new ArrayList<>();
        List<Video> vList = videoRepository.getMainVideoList(categoryId, (page - 1) * (count -1), count);
        for (int i = 0; i < vList.size(); i++) {
            ret.add(new VideoListResponseDto(vList.get(i), requestUser));
        }

        return ret;
    }

    public List<AnonymousUserVideoListResponseDto> getAnonymousUserMain(Long categoryId, Long page) {
        return videoRepository.getAnonymousUserMain(categoryId, (page - 1) * (count -1), count).stream().map(AnonymousUserVideoListResponseDto::new).collect(Collectors.toList());
    }

    public List<VideoListResponseDto> getLikedVideoList(Long userId, Long categoryId, Long page) {
        User requestUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        List<VideoListResponseDto> ret = new ArrayList<>();
        List<Video> vList = videoRepository.getLikedVideoList(userId, categoryId, (page - 1) * (count -1), count);
        for (int i = 0; i < vList.size(); i++) {
            ret.add(new VideoListResponseDto(vList.get(i), requestUser));
        }

        return ret;
    }
}
