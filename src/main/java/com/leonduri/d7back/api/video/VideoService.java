package com.leonduri.d7back.api.video;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.category.CategoryRepository;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.user.UserRepository;
import com.leonduri.d7back.api.user.dto.AdminUserResponseDto;
import com.leonduri.d7back.api.video.dto.*;
import com.leonduri.d7back.utils.FileSystemStorageService;
import com.leonduri.d7back.utils.exception.CNoPermissionException;
import com.leonduri.d7back.utils.exception.CUnauthorizedException;
import com.leonduri.d7back.utils.exception.CUserNotFoundException;
import com.leonduri.d7back.api.likes.Likes;
import com.leonduri.d7back.api.likes.LikesRepository;
import com.leonduri.d7back.api.video.dto.VideoListResponseDto;
import com.leonduri.d7back.utils.exception.CVideoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final FileSystemStorageService fileSystemStorageService;
    private final LikesRepository likesRepository;

    public VideoDetailResponseDto findVideoById(Long videoId, Long userId) throws Exception {
        User requestUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        Video video = videoRepository.findById(videoId).orElseThrow(CVideoNotFoundException::new);
        return new VideoDetailResponseDto(video, requestUser);
    }

    public VideoLikesResponseDto findUserById(Long videoId, Long userId) throws Exception {
        User requestUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        Video video = videoRepository.findById(videoId).orElseThrow(CVideoNotFoundException::new);
        return new VideoLikesResponseDto(video, requestUser);
    }

    public AdminVideoDetailResponseDto findById(Long videoId) throws Exception {
        Video video = videoRepository.findById(videoId).orElseThrow(CVideoNotFoundException::new);
        return new AdminVideoDetailResponseDto(video);
    }

    public int upHit(Long videoId) {
        return videoRepository.upHit(videoId);
    }

    public void deleteVideoById(Long videoId, long postedBy) {
        Video video = videoRepository.findById(videoId).orElseThrow(CVideoNotFoundException::new);
        if (video.getPostedBy().getId() != postedBy) { // 권한없음
            throw new CNoPermissionException();
        }
        fileSystemStorageService.delete(video.getFilePath());
        fileSystemStorageService.delete(video.getThumbnailPath());
        videoRepository.deleteById(videoId);
    }

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

    public List<VideoListResponseDto> getMyVideoList(Long userId, Long showId, Long lastId, Integer size) {
        User requestUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        List<VideoListResponseDto> ret = new ArrayList<>();
        List<Video> vList = videoRepository.getMyVideoList(userId, showId, size * 2);

        for (int i = 0; i < vList.size(); i++) {
            if(lastId == 0){
                int cnt = 0;
                ret.add(new VideoListResponseDto(vList.get(i), requestUser));
                cnt++;
                if (cnt == size + 1) {
                    break;
                }
            }
            if(vList.get(i).getId().equals(lastId)) {
                int cnt = 0;
                for(int j = i + 1; j < vList.size(); j++){
                    cnt++;
                    ret.add(new VideoListResponseDto(vList.get(j), requestUser));
                    if(cnt == size  + 1)
                        break;
                }
                break;
            }
        }
        return ret;
    }

    public List<VideoListResponseDto> getMainVideoList(Long userId, Long categoryId, Long showId, Long lastId, Integer size) {
        User requestUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        List<VideoListResponseDto> ret = new ArrayList<>();
        List<Video> vList = videoRepository.getMainVideoList(categoryId, showId, size * 2);
        for (int i = 0; i < vList.size(); i++) {
            if(lastId == 0){
                int cnt = 0;
                ret.add(new VideoListResponseDto(vList.get(i), requestUser));
                cnt++;
                if (cnt == size + 1) {
                    break;
                }
            }
            if(vList.get(i).getId().equals(lastId)) {
                int cnt = 0;
                for(int j = i + 1; j < vList.size(); j++){
                    cnt++;
                    ret.add(new VideoListResponseDto(vList.get(j), requestUser));
                    if(cnt == size  + 1)
                        break;
                }
                break;
            }
        }
        return ret;
    }

    public List<AnonymousUserVideoListResponseDto> getAnonymousUserMain(Long categoryId, Long showId, Long lastId, Integer size) {
        List<AnonymousUserVideoListResponseDto> ret = new ArrayList<>();
        List<Video> vList = videoRepository.getAnonymousUserMain(categoryId, showId, size * 2);

        for (int i = 0; i < vList.size(); i++) {
            if (lastId == 0) {
                int cnt = 0;
                ret.add(new AnonymousUserVideoListResponseDto(vList.get(i)));
                cnt++;
                if (cnt == size + 1) {
                    break;
                }
            }
            if (vList.get(i).getId().equals(lastId)) {
                int cnt = 0;
                for (int j = i + 1; j < vList.size(); j++) {
                    cnt++;
                    ret.add(new AnonymousUserVideoListResponseDto(vList.get(j)));
                    if (cnt == size + 1)
                        break;
                }
                break;
            }
        }
        return ret;
    }

    public List<VideoListResponseDto> getLikedVideoList(Long userId, Long showId, Long lastId, Integer size) {
        User requestUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        List<VideoListResponseDto> ret = new ArrayList<>();
        List<Video> vList = videoRepository.getLikedVideoList(userId, showId, size * 2);

        for (int i = 0; i < vList.size(); i++) {
            if (lastId == 0) {
                int cnt = 0;
                ret.add(new VideoListResponseDto(vList.get(i), requestUser));
                cnt++;
                if (cnt == size + 1) {
                    break;
                }
            }
            if (vList.get(i).getId().equals(lastId)) {
                int cnt = 0;
                for (int j = i + 1; j < vList.size(); j++) {
                    cnt++;
                    ret.add(new VideoListResponseDto(vList.get(j), requestUser));
                    if (cnt == size + 1)
                        break;
                }
                break;
            }
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
        if (v.getPostedBy().isAdmin() && v.getPostedBy().getId() != userId) throw new CUnauthorizedException();

        if (!video.isEmpty()) fileSystemStorageService.storeVideo(video, videoId);
        if (!thumbnail.isEmpty()) fileSystemStorageService.storeThumbnail(thumbnail, videoId);

        v = requestDto.getUpdatedEntity(v);

        return new VideoSimpleResponseDto(v);
    }

//    ADMIN API
    public List<AdminMainVideoListResponseDto> findAllVideoList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return videoRepository.findAll(pageRequest).stream().map(AdminMainVideoListResponseDto::new).collect(Collectors.toList());
    }

    public void deleteVideosByPostedBy(Long postedBy) {
        User u = userRepository.findById(postedBy).orElseThrow(CUserNotFoundException::new);
        for (Video v: u.getVideos()) {
            fileSystemStorageService.delete(v.getFilePath());
            fileSystemStorageService.delete(v.getThumbnailPath());
        }
        videoRepository.deleteVideosByPostedBy(postedBy);
    }

    public List<AdminVideoListResponseDto> findAdminUserVideoList(Long userId, Long page, Integer size) {

        User u = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        return videoRepository.getAdminUserVideoList(userId,page * size, size + 1).stream().map(AdminVideoListResponseDto::new).collect(Collectors.toList());
    }

    public Long countUserVideo(Long userId) {
        return videoRepository.countUserVideo(userId);
    }

    public Long countAllVideo() {
        return videoRepository.countAllVideo();
    }

    public AdminUserResponseDto findAdminUser(Long userId) {
        User u = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        return new AdminUserResponseDto(u);
    }

    public void changedShowId(Long newShowId, Long videoId) {
        videoRepository.changedShowId(newShowId, videoId);
    }

}
