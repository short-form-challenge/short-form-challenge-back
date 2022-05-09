package com.leonduri.d7back.utils.exception;

public class CNicknameInvalidException extends RuntimeException {
    public static final String errorMsg = "중복된 닉네임이 존재합니다.";

    public CNicknameInvalidException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNicknameInvalidException(String msg) {
        super(msg);
    }

    public CNicknameInvalidException() {
        super();
    }
}
