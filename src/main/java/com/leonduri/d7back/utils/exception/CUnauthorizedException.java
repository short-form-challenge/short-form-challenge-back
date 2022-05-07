package com.leonduri.d7back.utils.exception;

public class CUnauthorizedException extends RuntimeException {
    public static final String errorMsg = "접근 권한이 없는 유저입니다.";
    public CUnauthorizedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CUnauthorizedException(String msg) {
        super(msg);
    }

    public CUnauthorizedException() {
        super();
    }

}
