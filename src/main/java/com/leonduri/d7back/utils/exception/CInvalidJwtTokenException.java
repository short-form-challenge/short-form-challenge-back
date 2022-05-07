package com.leonduri.d7back.utils.exception;

public class CInvalidJwtTokenException extends RuntimeException {
    public static final String errorMsg = "jwt 토큰이 유효하지 않습니다.";
    public static final int code = -100;

    public CInvalidJwtTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public CInvalidJwtTokenException(String msg) {
        super(msg);
    }

    public CInvalidJwtTokenException() {
        super();
    }
}
