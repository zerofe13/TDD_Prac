package com.scurity.userProj.app.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorResult {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"user not found"),
    DUPLICATE_USER_ID(HttpStatus.BAD_REQUEST,"duplicate userid");

    private final HttpStatus httpStatus;
    private final String message;
}
