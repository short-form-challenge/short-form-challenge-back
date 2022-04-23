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
        this.setMsg("실패했습니다.");
    }

    protected ApiResponse fail() {
        ApiResponse response = new ApiResponse();
        response.setFailResponse();
        return response;
    }
}
