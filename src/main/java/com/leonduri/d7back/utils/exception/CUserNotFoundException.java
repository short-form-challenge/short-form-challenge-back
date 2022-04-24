package com.leonduri.d7back.utils.exception;

public class CUserNotFoundException extends RuntimeException {
    public static final String errorMsg = "해당 id를 가진 유저를 찾을 수 없습니다.";
    public CUserNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CUserNotFoundException(String msg) {
        super(msg);
    }

    public CUserNotFoundException() {
        super();
    }

}
