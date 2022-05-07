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
        if(data.isEmpty() || data.size() < 7){
            response.setLast(true);
//            data가 비어있거나 showId가 마지막이거나 data의 크기가 원래 반환하는 크기보다 작을때
        }
        else {
            response.setLast(false);
        }
        return response;
    }
}
