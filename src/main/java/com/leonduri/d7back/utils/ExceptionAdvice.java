package com.leonduri.d7back.utils;

import com.leonduri.d7back.utils.exception.CEmailSignInFailedException;
import com.leonduri.d7back.utils.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice // 예외 발생 시 json 형태로 결과 반환 | 프로젝트의 모든 Controller에 로직 적용
// 특정 디렉토리 하위의 Controller에만 적용시 @RestContollerAdvice(basePackages = "com.leonduri...")와 같이 설정
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    protected ApiResponse defaultException(HttpServletRequest request, Exception e) {
        return ApiResponse.fail();
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse userNotFound(HttpServletRequest request, CUserNotFoundException e) {
        return ApiResponse.fail(CUserNotFoundException.errorMsg);
    }

    @ExceptionHandler(CEmailSignInFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse emailSignInFailed(HttpServletRequest request, CEmailSignInFailedException e) {
        return ApiResponse.fail(CEmailSignInFailedException.errorMsg);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED) // 405
    protected ApiResponse requestMethodNotAllowed(HttpServletRequest request, Exception e) {
        return ApiResponse.fail("Http method가 올바르지 않습니다.");
    }
}
