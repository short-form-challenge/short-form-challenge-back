package com.leonduri.d7back.api.likes;

import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.video.Video;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "liked_by")
    private User user;

    @ManyToOne
    @JoinColumn(name = "liked_on")
    private Video video;
}
