package com.minkyu.moais.handler.exception;

import com.minkyu.moais.handler.CustomException;

// status -> 401
public class CheckSigninException extends CustomException {

    private static final String msg = "아이디 또는 비밀번호가 올바르지 않습니다.";

    public CheckSigninException() {
        super(msg);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }

}