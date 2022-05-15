package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.api.video.Video;
import com.leonduri.d7back.utils.ResponseDtoFilePathParser;
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
    UserSimpleResponseDto postedBy;
    public LocalDateTime postedAt;
    public Category category;

    public AnonymousUserVideoListResponseDto(Video video) {
        this.id = video.getId();
        this.showId = video.getShowId();
        this.title = video.getTitle();
        this.thumbnailPath = ResponseDtoFilePathParser.parseThumbnailFilePath(video.getThumbnailPath());
        this.likeCnt = video.getLikeCnt();
        this.postedAt = video.getPostedAt();

        this.postedBy = new UserSimpleResponseDto(video.getPostedBy());
        this.category = video.getCategoryId();
    }
}
