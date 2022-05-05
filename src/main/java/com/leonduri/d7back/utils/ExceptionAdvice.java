package com.leonduri.d7back.utils;

import com.leonduri.d7back.utils.exception.CEmailSignInFailedException;
import com.leonduri.d7back.utils.exception.CInvalidJwtTokenException;
import com.leonduri.d7back.utils.exception.CUserNotFoundException;
import com.leonduri.d7back.utils.exception.CWrongMediaFormatException;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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
    protected ApiResponse requestMethodNotAllowed(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        return ApiResponse.fail("Http method가 올바르지 않습니다.");
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    protected ApiResponse malformedJwtException(HttpServletRequest request, MalformedJwtException e) {
        return ApiResponse.fail("jwt 토큰 형식이 올바르지 않습니다.");
    }

    @ExceptionHandler(CInvalidJwtTokenException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse invalidJwtToken(HttpServletRequest request, CInvalidJwtTokenException e) {
        return ApiResponse.fail(CInvalidJwtTokenException.errorMsg);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse missingRequestPart(HttpServletRequest request, MissingServletRequestPartException e) {
        return ApiResponse.fail("request part 중 필수 값이 입력되지 않았습니다.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse missingRequestParameter(HttpServletRequest request, MissingServletRequestParameterException e) {
        return ApiResponse.fail("request parameter 중 필수 값이 입력되지 않았습니다.");
    }

    @ExceptionHandler(CWrongMediaFormatException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse wrongMediaFormat(HttpServletRequest request, CWrongMediaFormatException e) {
        return ApiResponse.fail(CWrongMediaFormatException.errorMsg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponse httpMessageNotReadable(HttpServletRequest request, HttpMessageNotReadableException e) {
        return ApiResponse.fail("JSON 형식이 맞지 않아 파싱할 수 없거나 request message 중 형식 오류가 있습니다.");
    }

}
