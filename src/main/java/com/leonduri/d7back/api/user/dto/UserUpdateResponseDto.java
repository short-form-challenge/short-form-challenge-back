package com.leonduri.d7back.api.user.dto;

import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.utils.ResponseDtoFilePathParser;

public class UserUpdateResponseDto {
    public long userId;
    public String nickname;
    public String profileFilePath;

    public UserUpdateResponseDto(User u) {
        this.userId = u.getId();
        this.nickname = u.getNickname();
        this.profileFilePath = ResponseDtoFilePathParser.parseProfileFilePath(u.getProfileFilePath());
    }
}
