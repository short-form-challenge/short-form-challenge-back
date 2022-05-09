package com.leonduri.d7back.utils.exception;

public class CEmailInvalidException extends RuntimeException {
    public static final String errorMsg = "중복된 이메일이 존재합니다.";

    public CEmailInvalidException(String msg, Throwable t) {
        super(msg, t);
    }

    public CEmailInvalidException(String msg) {
        super(msg);
    }

    public CEmailInvalidException() {
        super();
    }
}
