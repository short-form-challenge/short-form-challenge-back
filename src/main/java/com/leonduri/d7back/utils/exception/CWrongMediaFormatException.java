package com.leonduri.d7back.utils.exception;

public class CWrongMediaFormatException extends RuntimeException {
    public static final String errorMsg = "media 파일의 형식이 올바르지 않습니다.";
    public CWrongMediaFormatException(String msg, Throwable t) {
        super(msg, t);
    }

    public CWrongMediaFormatException(String msg) {
        super(msg);
    }

    public CWrongMediaFormatException() {
        super();
    }

}
