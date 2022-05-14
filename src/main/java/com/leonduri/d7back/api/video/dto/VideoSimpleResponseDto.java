package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.video.Video;
import com.leonduri.d7back.utils.ResponseDtoFilePathParser;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideoSimpleResponseDto {
    public Long id;
    public String title;
    public String contentFilePath;
    public String thumbnailFilePath;
    public long postedBy;

    public VideoSimpleResponseDto(Video video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.postedBy = video.getPostedBy().getId();
        this.contentFilePath = ResponseDtoFilePathParser.parseVideoFilePath(video.getFilePath());
        this.thumbnailFilePath = ResponseDtoFilePathParser.parseThumbnailFilePath(video.getThumbnailPath());
    }
}
