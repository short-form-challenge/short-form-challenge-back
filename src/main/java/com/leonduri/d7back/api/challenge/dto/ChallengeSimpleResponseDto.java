package com.leonduri.d7back.api.challenge.dto;

import com.leonduri.d7back.api.category.Category;
import com.leonduri.d7back.api.challenge.Challenge;
import com.leonduri.d7back.api.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;


@Getter
@Setter
public class ChallengeSimpleResponseDto {
    private long id;
    private int dayCnt;
    private int badgeCnt;
    private LocalDate lastChallengedAt;
    private String user;
    private String category;

    public ChallengeSimpleResponseDto(Challenge c) {
        this.id = c.getId();
        this.dayCnt = c.getDayCnt();
        this.badgeCnt = c.getBadgeCnt();
        this.lastChallengedAt = c.getLastChallengedAt();
        this.user = c.getUser().getEmail();
        this.category = c.getCategory().getKind();
    }
}
