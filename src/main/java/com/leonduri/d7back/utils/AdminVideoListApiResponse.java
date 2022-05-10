package com.leonduri.d7back.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdminVideoListApiResponse<T> extends VideoApiResponse {
    private List<T> data;
    private boolean isLast;

    public static<T> AdminVideoListApiResponse<T> success(List<T> data, Long videoCount) {
        AdminVideoListApiResponse<T> response = new AdminVideoListApiResponse<>();
        response.setSuccessResponse();
        response.setData(data);
        response.setVideoCount(videoCount);
        response.setLast(true);
        return response;
    }

    public static<T> AdminVideoListApiResponse<T> lastSuccess(List<T> data, Long videoCount) {
        AdminVideoListApiResponse<T> response = new AdminVideoListApiResponse<>();
        response.setSuccessResponse();
        response.setData(data);
        response.setVideoCount(videoCount);
        response.setLast(false);
        return response;
    }
}
