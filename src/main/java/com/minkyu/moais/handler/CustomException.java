package com.minkyu.moais.handler;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

// 커스텀 예외처리
@Getter
public abstract class CustomException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public CustomException(String message) {
        super(message);
    }

    public abstract int getStatusCode();

}
