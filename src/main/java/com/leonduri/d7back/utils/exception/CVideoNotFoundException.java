package com.leonduri.d7back.utils.exception;

public class CVideoNotFoundException extends RuntimeException {
    public static final String errorMsg = "해당 id를 가진 비디오를 찾을 수 없습니다.";
    public CVideoNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CVideoNotFoundException(String msg) {
        super(msg);
    }

    public CVideoNotFoundException() {
        super();
    }

}
