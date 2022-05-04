package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.api.video.Video;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoLikesResponseDto {
    public Long id;
    public Long likeCnt;
    UserSimpleResponseDto posted_by;
    UserSimpleResponseDto liked_by;
    public Category category;

    public VideoLikesResponseDto(Video video, User requestUser) {
        this.id = video.getId();
        this.likeCnt = video.getLikeCnt();

        UserSimpleResponseDto userSimpleResponseDto = new UserSimpleResponseDto(video.getUser());
        this.posted_by = userSimpleResponseDto;

        UserSimpleResponseDto liked_by = new UserSimpleResponseDto(requestUser);
        this.liked_by = liked_by;

        this.category = video.getCategory();
    }
}
