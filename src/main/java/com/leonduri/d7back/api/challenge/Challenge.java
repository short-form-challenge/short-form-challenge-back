package com.leonduri.d7back.api.challenge;

import com.leonduri.d7back.api.User.User;
import com.leonduri.d7back.api.category.Category;

import javax.persistence.*;

@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "day_cnt", columnDefinition = "Int default 0")
    private Integer dayCnt;

    @Column(name = "badge_cnt", columnDefinition = "Int default 0")
    private Integer badgeCnt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
