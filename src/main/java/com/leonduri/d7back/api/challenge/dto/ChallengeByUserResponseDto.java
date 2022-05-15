package com.leonduri.d7back.api.challenge.dto;

import com.leonduri.d7back.api.challenge.Challenge;

public class ChallengeByUserResponseDto {
    public int badgeCnt;
    public int dayCnt;
    public long category;

    public ChallengeByUserResponseDto(Challenge c) {
        this.badgeCnt = c.getBadgeCnt();
        this.dayCnt = c.getDayCnt();
        this.category = c.getCategory().getId();
    }
}
