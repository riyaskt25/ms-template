package com.snb.ms.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeEnum {
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "error.not.found"),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "error.invalid.argument"),
    MALFORMED_JSON(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "error.malformed.json"),
    CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "error.conflict"),
    DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT, "CONFLICT", "error.data.integrity"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR", "error.internal");

    private final HttpStatus status;
    private final String type;
    private final String messageKey;
}