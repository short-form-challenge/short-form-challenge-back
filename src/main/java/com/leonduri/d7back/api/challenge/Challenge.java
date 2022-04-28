package com.leonduri.d7back.api.challenge;

import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.category.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "day_cnt", columnDefinition = "Int default 0")
    private Integer dayCnt;

    @Column(name = "badge_cnt", columnDefinition = "Int default 0")
    private Integer badgeCnt;

    @Column(name = "last_challenged_at")
    private LocalDate lastChallengedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public void setDayCntZero() {
        this.dayCnt = 0;
    }

    public void increaseDayCnt() {
        this.dayCnt += 1;
    }

    public void increaseBadgeCnt() {
        this.badgeCnt += 1;
    }

}
