package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.likes.Likes;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.api.video.Video;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnonymousUserVideoListResponseDto {
    public Long id;
    public Long showId;
    public String title;
    public String thumbnailPath;
    public Long likeCnt;
    public Boolean isLiked = false;
    UserSimpleResponseDto posted_by;
    public Category category;

    public AnonymousUserVideoListResponseDto(Video video) {
        this.id = video.getId();
        this.showId = video.getShowId();
        this.title = video.getTitle();
        this.thumbnailPath = video.getThumbnailPath();
        this.likeCnt = video.getLikeCnt();

        UserSimpleResponseDto userSimpleResponseDto = new UserSimpleResponseDto(video.getUser());
        this.posted_by = userSimpleResponseDto;

        this.category = video.getCategory();
    }
}
