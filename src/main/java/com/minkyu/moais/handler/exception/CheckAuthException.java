package com.minkyu.moais.handler.exception;

import com.minkyu.moais.handler.CustomException;

// status -> 402
public class CheckAuthException extends CustomException {

    private static final String msg = "존재하지 않는 JWT 토큰입니다.";

    public CheckAuthException() {
        super(msg);
    }

    @Override
    public int getStatusCode() {
        return 402;
    }
}