package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.video.Video;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VideoUpdateRequestDto {
    private int length;
    private String title;

    public Video getUpdatedEntity(Video v
//            , String contentFilePath, String thumbnailFilePath
//            , Category category
    ) {
//        if (!contentFilePath.equals("")) { // video content has been updated
//            v.setFilePath(contentFilePath);
//        if (!thumbnailFilePath.equals(""))
//            v.setThumbnailPath(thumbnailFilePath);
        v.setVideoLength(this.length);
        if (!this.title.equals("")) v.setTitle(this.title);

        // TODO category change available?
        // v.setCategory(category);

        return v;
    }
}
