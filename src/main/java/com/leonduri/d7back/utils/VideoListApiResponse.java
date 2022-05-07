package com.leonduri.d7back.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VideoListApiResponse<T> extends VideoApiResponse {
    private List<T> data;
    private boolean isLast;

    public static<T> VideoListApiResponse<T> success(List<T> data) {
        VideoListApiResponse<T> response = new VideoListApiResponse<>();
        response.setSuccessResponse();
        response.setData(data);
        response.setLast(true);
        return response;
    }

    public static<T> VideoListApiResponse<T> lastSuccess(List<T> data) {
        VideoListApiResponse<T> response = new VideoListApiResponse<>();
        response.setSuccessResponse();
        response.setData(data);
        response.setLast(false);
        return response;
    }

}
