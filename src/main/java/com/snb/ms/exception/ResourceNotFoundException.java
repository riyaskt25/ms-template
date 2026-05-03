package com.snb.ms.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final ErrorCodeEnum errorCode;
    private final String description;

    public ResourceNotFoundException(String message) {
        super(message);
        this.errorCode = ErrorCodeEnum.RESOURCE_NOT_FOUND;
        this.description = message;
    }
}
