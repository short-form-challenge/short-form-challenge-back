package com.leonduri.d7back.api.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignInRequestDto {
    private final String email;
    private final String password;

    @Builder
    public UserSignInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
