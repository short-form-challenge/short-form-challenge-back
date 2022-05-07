package com.leonduri.d7back.api.video;

import com.leonduri.d7back.api.likes.Likes;
import com.leonduri.d7back.api.likes.LikesRepository;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.user.UserRepository;
import com.leonduri.d7back.api.video.dto.AnonymousUserVideoListResponseDto;
import com.leonduri.d7back.api.video.dto.VideoDetailResponseDto;
import com.leonduri.d7back.api.video.dto.VideoLikesResponseDto;
import com.leonduri.d7back.api.video.dto.VideoListResponseDto;
import com.leonduri.d7back.utils.exception.CUserNotFoundException;
import com.leonduri.d7back.utils.exception.CVideoNotFoundException;
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
    private final LikesRepository likesRepository;

    Long count = 7L;
    public VideoDetailResponseDto findVideoById(Long videoId, Long userId) throws Exception {
        User requestUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        Video video = videoRepository.findById(videoId).orElseThrow(CVideoNotFoundException::new);
        VideoDetailResponseDto videoListResponseDto = new VideoDetailResponseDto(video, requestUser);
        return videoListResponseDto;
    }

    public VideoLikesResponseDto findById(Long videoId, Long userId) throws Exception {
        User requestUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        Video video = videoRepository.findById(videoId).orElseThrow(CVideoNotFoundException::new);
        VideoLikesResponseDto v = new VideoLikesResponseDto(video, requestUser);

        return v;
    }

    public int upHit(Long videoId) {
        return videoRepository.upHit(videoId);
    }

    public void deleteVideoById(Long videoId) {
        videoRepository.deleteById(videoId);
    }
  
    //        Exception 추가 필요
    //    like를 눌렀는지 안 눌렀는지 판단 -> 프론트? 백?
    public void upLikeCnt(Long videoId, Long userId) throws Exception {
        videoRepository.upLikeCnt(videoId);
        User likedBy = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        Video likedOn = videoRepository.findById(videoId).orElseThrow(CVideoNotFoundException::new);
        likesRepository.save(new Likes(likedBy, likedOn));
    }

    public void downLikeCnt(Long videoId, Long userId) throws Exception {
        videoRepository.downLikeCnt(videoId);
        User likedBy = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        Video likedOn = videoRepository.findById(videoId).orElseThrow(CVideoNotFoundException::new);
        likesRepository.deleteLikes(videoId, userId);
        //삭제
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
