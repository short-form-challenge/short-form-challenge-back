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
public class VideoLikesResponseDto {
    public Long id;
    public Long likeCnt;
    public boolean isLiked;

    public VideoLikesResponseDto(Video video, User requestUser) {
        this.id = video.getId();
        this.likeCnt = video.getLikeCnt();

        List<Likes> list =  requestUser.getLikesList();
        for (Likes likes : list) {
            if (video.getId() == likes.getVideo().getId() && requestUser.getId() == likes.getUser().getId()) {
                isLiked = true;
            } else {
                isLiked = false;
            }
        }

    }
}
