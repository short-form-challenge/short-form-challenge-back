package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.likes.Likes;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.api.video.Video;
import com.leonduri.d7back.utils.ResponseDtoFilePathParser;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VideoListResponseDto {
    public Long id;
    public Long showId;
    public String title;
    public String thumbnailPath;
    public Long likeCnt;
    public Boolean isLiked = false;
    public LocalDateTime postedAt;
    UserSimpleResponseDto postedBy;
    public Category category;

    public VideoListResponseDto(Video video) {
        this.id = video.getId();
        this.showId = video.getShowId();
        this.title = video.getTitle();
        this.thumbnailPath = ResponseDtoFilePathParser.parseThumbnailFilePath(video.getThumbnailPath());
        this.likeCnt = video.getLikeCnt();
        this.postedAt = video.getPostedAt();
        this.postedBy = new UserSimpleResponseDto(video.getPostedBy());
        this.category = video.getCategoryId();
    }

    public VideoListResponseDto(Video video, User requestUser) {
        this.id = video.getId();
        this.showId = video.getShowId();
        this.title = video.getTitle();
        this.thumbnailPath = ResponseDtoFilePathParser.parseThumbnailFilePath(video.getThumbnailPath());
        this.likeCnt = video.getLikeCnt();
        this.postedAt = video.getPostedAt();
        this.postedBy = new UserSimpleResponseDto(video.getPostedBy());

        List<Likes> likesList = requestUser.getLikesList();
        for(Likes like : likesList){
            if (like.getVideo().getId().equals(video.getId())) {
                this.isLiked = true;
                break;
            }
        }
        this.category = video.getCategoryId();
    }
}
