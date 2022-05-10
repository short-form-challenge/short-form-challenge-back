package com.leonduri.d7back.api.user.dto;

import com.leonduri.d7back.api.user.User;

import java.time.LocalDateTime;

public class AdminUserResponseDto {
    public long userId;
    public String nickname;
    public LocalDateTime createdAt;
    public  LocalDateTime lastLogin;

    public AdminUserResponseDto (User u) {
        this.userId = u.getId();
        this.nickname = u.getNickname();
        this.createdAt = u.getCreatedAt();
        this.lastLogin = u.getLastLogin();
    }
}
