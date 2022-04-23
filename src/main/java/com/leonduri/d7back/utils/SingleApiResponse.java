package com.leonduri.d7back.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SingleApiResponse<T> extends ApiResponse {
    private T data;

    public SingleApiResponse<T> success(T data) {
        SingleApiResponse<T> response = new SingleApiResponse<>();
        response.setSuccessResponse();
        response.setData(data);
        return response;
    }
}
