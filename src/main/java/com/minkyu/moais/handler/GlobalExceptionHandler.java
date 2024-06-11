package com.minkyu.moais.handler;

import com.minkyu.moais.common.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final DataResponse dataResponse;

    @Autowired
    public GlobalExceptionHandler() {
        this.dataResponse = new DataResponse();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("에러 반환 : "+ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        return ResponseEntity.ok(dataResponse.fail("400", ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler({CustomException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
        log.error("에러 반환 : "+ex.getMessage());
        return ResponseEntity.ok(dataResponse.fail(String.valueOf(ex.getStatusCode()), ex.getMessage()));
    }

}