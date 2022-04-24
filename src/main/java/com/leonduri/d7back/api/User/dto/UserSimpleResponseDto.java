package com.leonduri.d7back.api.User.dto;

import com.leonduri.d7back.api.User.User;
import lombok.Getter;

@Getter
public class UserSimpleResponseDto {
    Long id;
    String email;
    String nickname;

    public UserSimpleResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
