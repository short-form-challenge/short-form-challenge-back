package com.leonduri.d7back.utils.exception;

public class CEmailSignInFailedException extends RuntimeException {
    public static final String errorMsg = "이메일 또는 비밀번호가 정확하지 않습니다.";

    public CEmailSignInFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CEmailSignInFailedException(String msg) {
        super(msg);
    }

    public CEmailSignInFailedException() {
        super();
    }
}
