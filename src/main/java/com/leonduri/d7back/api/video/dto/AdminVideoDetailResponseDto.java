package com.leonduri.d7back.api.video.dto;

import com.leonduri.d7back.api.video.Video;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminVideoDetailResponseDto {
        public long id;
        public long showId;
        public String title;
        public LocalDateTime postedAt;
        public long hit;
        public long likeCnt;
        public long userId;
        public String nickname;
        public String thumbnailPath;
        public String filePath;

        public AdminVideoDetailResponseDto(Video video) {
            this.id = video.getId();
            this.showId = video.getShowId();
            this.title = video.getTitle();
            this.filePath = video.getFilePath();
            this.hit = video.getHit();
            this.postedAt = video.getPostedAt();
            this.likeCnt = video.getLikeCnt();
            this.userId = video.getPostedBy().getId();
            this.nickname = video.getPostedBy().getNickname();
        }
    }

