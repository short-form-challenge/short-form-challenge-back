package com.leonduri.d7back.api.Video;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="file_path", nullable = false)
    private String filePath;

    @Column(name="thumbnail_path", nullable = false)
    private String thumbnailPath;

    @Column(name="is_embeded", columnDefinition = "boolean default false")
    private Boolean isEmbeded;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name="show_id", nullable = false)
    private Long showId;

    @Column(name="posted_at")
    private LocalDateTime postedAt;

    @Column(columnDefinition = "Bigint default 0")
    private Long hit;

    @Column(name = "like_cnt", columnDefinition = "Bigint default 0")
    private Long likeCnt;

    @Column(name="is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;

//    Video n : User 1
//    @ManyToOne
//    @JoinColumn(name = "posted_by")
//    private User user;
}
