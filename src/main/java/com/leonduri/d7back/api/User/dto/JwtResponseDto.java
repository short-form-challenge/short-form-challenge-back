package com.leonduri.d7back.api.User.dto;


public class JwtResponseDto {
    public Long userId;
    public String token;

    public JwtResponseDto(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}