package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.api.video.Video;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminMainVideoListResponseDto {
    public Long id;
    public Long showId;
    public String title;
    public UserSimpleResponseDto postedBy;

    public AdminMainVideoListResponseDto(Video video) {
        this.id = video.getId();
        this.showId = video.getShowId();
        this.title = video.getTitle();

        User postedBy = video.getPostedBy();
        UserSimpleResponseDto user = new UserSimpleResponseDto(postedBy);
        this.postedBy = user;
    }
}
