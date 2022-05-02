package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.video.Video;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AnonymousUserVideoListRequestDto {
    @ApiModelProperty(value = "카테고리 Id")
    public Long cate;
    @ApiModelProperty(value = "페이지 번호")
    public Long page;

    public Video toEntity() {
        Video video = new Video();
        User user = new User();
        Category category = new Category();
        category.setId(cate);

        video.setCategory(category);
        return video;
    }
}
