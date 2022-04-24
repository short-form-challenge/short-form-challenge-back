package com.leonduri.d7back.api.User.dto;

import com.leonduri.d7back.api.User.User;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class UserSignUpRequestDto {
    String email;
    String password;
    String nickname;

    public User toEntity() {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setAdmin(false);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
