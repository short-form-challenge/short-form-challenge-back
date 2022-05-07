package com.leonduri.d7back.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignInRequestDto {

    @ApiModelProperty(value = "이메일", example = "required/string", required = true)
    private final String email;

    @ApiModelProperty(value = "비밀번호", example = "required/string", required = true)
    private final String password;

    @Builder
    public UserSignInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
