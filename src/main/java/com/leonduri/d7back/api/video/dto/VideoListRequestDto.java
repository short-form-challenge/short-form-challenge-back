package com.leonduri.d7back.api.video.dto;
import com.leonduri.d7back.api.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoListRequestDto {
    @ApiModelProperty(value = "유저 Id")
    public Long userId;
    @ApiModelProperty(value = "카테고리 Id")
    public Long cate;
    @ApiModelProperty(value = "페이지 번호")
    public Long page;

//    public Video toEntity() {
//        Video video = new Video();
//        User user = new User();
//        Category category = new Category();
//        user.setId(userId);
//        category.setId(cate);
//
//        video.setUser(user);
//        video.setCategory(category);
//        return video;
//    }

    @Builder
    public VideoListRequestDto(Long userId, Long cate, Long page) {
        this.userId = userId;
        this.cate = cate;
        this.page = page;
    }

    public User getUser(Long userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

}

