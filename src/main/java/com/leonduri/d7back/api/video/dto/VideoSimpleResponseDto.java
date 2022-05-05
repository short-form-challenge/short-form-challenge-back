package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.likes.Likes;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.api.video.Video;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideoSimpleResponseDto {
    public Long id;
    public String title;
    public long postedBy;

    public VideoSimpleResponseDto(Video video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.postedBy = video.getUser().getId();
    }
}
