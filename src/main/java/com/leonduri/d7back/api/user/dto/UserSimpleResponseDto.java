package com.leonduri.d7back.api.user.dto;

import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.utils.ResponseDtoFilePathParser;
import lombok.Getter;

@Getter
public class UserSimpleResponseDto {
    Long id;
    String email;
    String nickname;
    String profileFilePath;

    public UserSimpleResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileFilePath = ResponseDtoFilePathParser.parseProfileFilePath(user.getProfileFilePath());
    }
}
