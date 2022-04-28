package com.leonduri.d7back.api.user.dto;

import com.leonduri.d7back.api.challenge.Challenge;
import com.leonduri.d7back.api.challenge.dto.ChallengeByUserResponseDto;
import com.leonduri.d7back.api.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserProfileResponseDto {
    public long userId;
    public String nickname;
    public String profileFilePath;
    public int totalBadgeCnt;
    public int ongoingChallengeCnt;
    // user의 challenge 중 dayCnt > 0 && LastChallengedAt이 오늘인 challenge 수
    public List<ChallengeByUserResponseDto> challenges;

    public UserProfileResponseDto (User u) {
        this.userId = u.getId();
        this.nickname = u.getNickname();
        this.profileFilePath = u.getProfileFilePath();
        List<Challenge> cList = u.getChallenges();
        this.totalBadgeCnt = 0;
        this.ongoingChallengeCnt = 0;
        for (Challenge c: cList) {
            this.totalBadgeCnt += c.getBadgeCnt();
            if (c.getDayCnt() > 0 && c.getLastChallengedAt().equals(LocalDate.now()))
                this.ongoingChallengeCnt++;
        }
        this.challenges = cList.stream().map(ChallengeByUserResponseDto::new)
                .collect(Collectors.toList());
    }
}
