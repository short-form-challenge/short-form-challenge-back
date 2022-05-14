package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.api.video.Video;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnonymousUserVideoListResponseDto {
    public Long id;
    public Long showId;
    public String title;
    public String thumbnailPath;
    public Long likeCnt;
    public Boolean isLiked = false;
    public LocalDateTime postedAt;
    UserSimpleResponseDto posted_by;
    public Category category;

    public AnonymousUserVideoListResponseDto(Video video) {
        this.id = video.getId();
        this.showId = video.getShowId();
        this.title = video.getTitle();
        this.thumbnailPath = video.getThumbnailPath();
        this.likeCnt = video.getLikeCnt();
        this.postedAt = video.getPostedAt();

        UserSimpleResponseDto userSimpleResponseDto = new UserSimpleResponseDto(video.getPostedBy());
        this.posted_by = userSimpleResponseDto;

        this.category = video.getCategoryId();
    }
}
