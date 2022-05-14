package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.video.Video;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VideoSaveRequestDto {
    private String title;
    private int length;
    private long categoryId;

    public Video toEntity(User postedBy, Category category) {
        Video v = new Video();
        v.setTitle(this.title);
        v.setVideoLength(this.length);
        v.setPostedBy(postedBy);
        v.setCategory(category);

        v.setPostedAt(LocalDateTime.now());
        v.setFilePath("");  // temporarily
        v.setThumbnailPath(""); // temporarily
        v.setShowId(1L); // temporal value
        v.setHit(0L);
        v.setLikeCnt(0L);
        return v;
    }
}
