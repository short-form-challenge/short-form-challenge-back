package com.leonduri.d7back.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoApiResponse {

    @ApiModelProperty(value = "응답 성공여부 : true/false")
    private boolean success;

    @ApiModelProperty(value = "응답 코드 번호 : >=0 정상, < 0 비정상")
    private int code;

    @ApiModelProperty(value = "응답 메시지")
    private String msg;

    @ApiModelProperty(value = "마지막 여부")
    private boolean isLast;

    @ApiModelProperty(value = "비디오 전체 갯수")
    private Long videoCount;

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

    protected static VideoApiResponse fail() {
        VideoApiResponse response = new VideoApiResponse();
        response.setFailResponse();
        return response;
    }

    // overloading
    protected static VideoApiResponse fail(String msg) {
        VideoApiResponse response = new VideoApiResponse();
        response.setFailResponse();
        response.setMsg(msg);
        return response;
    }
}
