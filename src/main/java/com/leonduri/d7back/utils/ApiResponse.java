package com.leonduri.d7back.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {

    @ApiModelProperty(value = "응답 성공여부 : true/false")
    private boolean success;

    @ApiModelProperty(value = "응답 코드 번호 : >=0 정상, < 0 비정상")
    private int code;

    @ApiModelProperty(value = "응답 메시지")
    private String msg;

    protected void setSuccessResponse() {
        this.setSuccess(true);
        this.setCode(0);
        this.setMsg("성공했습니다.");
    }

    protected void setFailResponse() {
        this.setSuccess(false);
        this.setCode(-1);
        this.setMsg("정의되지 않은 예외가 발생했습니다.");
    }

    public static ApiResponse success() {
        ApiResponse response = new ApiResponse();
        response.setSuccessResponse();
        return response;
    }

    public static ApiResponse success(String msg) {
        ApiResponse response = new ApiResponse();
        response.setSuccessResponse();
        return response;
    }

    public static ApiResponse fail() {
        ApiResponse response = new ApiResponse();
        response.setFailResponse();
        return response;
    }

    // overloading
    public static ApiResponse fail(String msg) {
        ApiResponse response = new ApiResponse();
        response.setFailResponse();
        response.setMsg(msg);
        return response;
    }

    // overloading
    public static ApiResponse fail(String msg, int code) {
        ApiResponse response = new ApiResponse();
        response.setFailResponse();
        response.setMsg(msg);
        response.setCode(code);
        return response;
    }
}
