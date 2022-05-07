package com.leonduri.d7back.api.video;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.category.CategoryRepository;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.user.UserRepository;
import com.leonduri.d7back.api.video.dto.VideoListResponseDto;
import com.leonduri.d7back.api.video.dto.VideoSaveRequestDto;
import com.leonduri.d7back.api.video.dto.VideoSimpleResponseDto;
import com.leonduri.d7back.api.video.dto.VideoUpdateRequestDto;
import com.leonduri.d7back.utils.FileSystemStorageService;
import com.leonduri.d7back.utils.exception.CUnauthorizedException;
import com.leonduri.d7back.utils.exception.CUserNotFoundException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final FileSystemStorageService fileSystemStorageService;
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

    public VideoSimpleResponseDto saveVideo(VideoSaveRequestDto requestDto, long userId,
                                            MultipartFile video, MultipartFile thumbnail) throws Exception {
        User u = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        Category c = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(Exception::new);
        LocalDateTime postedAt = LocalDateTime.now();
        Video v = requestDto.toEntity(u, c);
        v = videoRepository.save(v);

        // after getting the video id
        long vid = v.getId();
        v.setFilePath(fileSystemStorageService.storeVideo(video, vid));
        v.setThumbnailPath(fileSystemStorageService.storeThumbnail(thumbnail, vid));
        v.setShowId(vid);

        // update
        videoRepository.save(v);

        return new VideoSimpleResponseDto(v);
    }

    public VideoSimpleResponseDto updateVideo(long userId, long videoId, VideoUpdateRequestDto requestDto,
                                              MultipartFile video, MultipartFile thumbnail) throws Exception {
        Video v = videoRepository.findById(videoId).orElseThrow(Exception::new);

        // postedBy validation
        if (v.getUser().isAdmin() && v.getUser().getId() != userId) throw new CUnauthorizedException();

        if (!video.isEmpty()) fileSystemStorageService.storeVideo(video, videoId);
        if (!thumbnail.isEmpty()) fileSystemStorageService.storeThumbnail(thumbnail, videoId);

        v = requestDto.getUpdatedEntity(v);

        return new VideoSimpleResponseDto(v);
    }
}
