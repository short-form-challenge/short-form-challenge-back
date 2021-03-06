package com.leonduri.d7back.api.user.dto;

import com.leonduri.d7back.api.challenge.Challenge;
import com.leonduri.d7back.api.challenge.dto.ChallengeByUserResponseDto;
import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.utils.ResponseDtoFilePathParser;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UserProfileResponseDto {
    public long userId;
    public String nickname;
    public String profileFilePath;
    public String email;
    public int totalBadgeCnt;
    public int ongoingChallengeCnt;
    // user의 challenge 중 dayCnt > 0 && LastChallengedAt이 오늘인 challenge 수
    public List<ChallengeByUserResponseDto> challenges;

    public UserProfileResponseDto (User u) {
        this.userId = u.getId();
        this.nickname = u.getNickname();
        this.profileFilePath = ResponseDtoFilePathParser.parseProfileFilePath(u.getProfileFilePath());
        this.email = u.getEmail();
        List<Challenge> cList = u.getChallenges();
        this.totalBadgeCnt = 0;
        this.ongoingChallengeCnt = 0;
        for (Challenge c: cList) {
            this.totalBadgeCnt += c.getBadgeCnt();
            if (c.getDayCnt() > 0) this.ongoingChallengeCnt++;
        }
        this.challenges = cList.stream().map(ChallengeByUserResponseDto::new)
                .collect(Collectors.toList());
    }
}
