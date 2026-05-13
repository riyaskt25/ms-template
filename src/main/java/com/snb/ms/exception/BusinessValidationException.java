package com.snb.ms.exception;

import lombok.Getter;

@Getter
public class BusinessValidationException extends RuntimeException {

    private final ErrorCodeEnum errorCode;
    private final String description;
    private final String descriptionKey;
    private final Object[] descriptionArgs;

    public BusinessValidationException(String descriptionKey, Object[] descriptionArgs, String fallbackDescription) {
        super(fallbackDescription);
        this.errorCode = ErrorCodeEnum.INVALID_ARGUMENT;
        this.description = fallbackDescription;
        this.descriptionKey = descriptionKey;
        this.descriptionArgs = descriptionArgs;
    }

    public static BusinessValidationException invalidCompanyStatusValue(Object value) {
        return new BusinessValidationException(
            "error.company.status.invalid.value",
            new Object[]{value},
            "Invalid company status: " + value + ". Allowed values are ACTIVE or REJECTED."
        );
    }

    public static BusinessValidationException invalidCompanyStatusTransition(Object from, Object to) {
        return new BusinessValidationException(
            "error.company.status.invalid.transition",
            new Object[]{from, to},
            "Company status transition is not allowed from " + from + " to " + to + "."
        );
    }
}