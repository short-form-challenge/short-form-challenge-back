package com.leonduri.d7back.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ListApiResponse<T> extends ApiResponse {
    private List<T> data;
    private Long count;

    public static<T> ListApiResponse<T> success(List<T> data) {
        ListApiResponse<T> response = new ListApiResponse<>();
        response.setSuccessResponse();
        response.setData(data);
        return response;
    }

    public static<T> ListApiResponse<T> adminSuccess(List<T> data, Long count) {
        ListApiResponse<T> response = new ListApiResponse<>();
        response.setSuccessResponse();
        response.setCount(count);
        response.setData(data);
        return response;
    }
}
