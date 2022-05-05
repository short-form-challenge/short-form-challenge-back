package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.video.Video;
import lombok.Getter;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@Getter
public class VideoSaveRequestDto {
    private String title;
    private int length;
    private long categoryId;

    public Video toEntity(String contentFilePath, String thumbnailFilePath,
                          User postedBy, Category category, LocalDateTime postedAt) {
        Video v = new Video();
        v.setFilePath(contentFilePath);
        v.setThumbnailPath(thumbnailFilePath);
        v.setTitle(this.title);
        v.setVideoLength(this.length);
        v.setUser(postedBy);
        v.setCategory(category);
        v.setPostedAt(postedAt);

        v.setShowId(1L); // temporal value
        v.setHit(0L);
        v.setLikeCnt(0L);
        v.setIsDeleted(false);
        return v;
    }
}
