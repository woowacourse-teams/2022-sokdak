package com.wooteco.sokdak.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ErrorResponse> handleInternalException(InternalException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException() {
        return ResponseEntity.internalServerError().body(new ErrorResponse("서버에 알 수 없는 문제가 발생했습니다."));
    }
}
