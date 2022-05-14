package com.leonduri.d7back.utils.exception;

public class CNoPermissionException extends RuntimeException {
        public static final String errorMsg = "해당 유저는 수정, 삭제 권한이 없습니다.";
        public CNoPermissionException(String msg, Throwable t) {
            super(msg, t);
        }

        public CNoPermissionException(String msg) {
            super(msg);
        }

        public CNoPermissionException() {
            super();
        }

    }