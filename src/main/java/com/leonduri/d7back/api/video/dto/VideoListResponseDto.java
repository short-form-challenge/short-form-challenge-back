package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.likes.Likes;
import com.leonduri.d7back.api.likes.LikesRepository;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.api.video.Video;
import lombok.Getter;

import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;

@Getter
public class VideoListResponseDto {
    public Long id;
    public Long showId;
    public String title;
    public String thumbnailPath;
    public Long likeCnt;
    public Boolean isLiked = false;
//    public User user;
    UserSimpleResponseDto user;
    public Category category;
//    public List<int[]> userLike;

    public VideoListResponseDto(Video video) {
        this.id = video.getId();
        this.showId = video.getShowId();
        this.title = video.getTitle();
        this.thumbnailPath = video.getThumbnailPath();
        this.likeCnt = video.getLikeCnt();
//        this.user = video.getUser();
        UserSimpleResponseDto userSimpleResponseDto = new UserSimpleResponseDto(video.getUser());
        this.user = userSimpleResponseDto;
        List<Likes> likesList = video.getUser().getLikesList();
        for(Likes like : likesList){
            if (like.getVideo().getId() == video.getId()) {
                this.isLiked = true;
            }
        }
        this.category = video.getCategory();

    }

}
