package com.leonduri.d7back.api.user.dto;

import com.leonduri.d7back.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserSignUpRequestDto {
    private String email;
    private String password;
    private String nickname;

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
