package com.snb.ms.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final ErrorCodeEnum errorCode;
    private final String description;
    private final String descriptionKey;
    private final Object[] descriptionArgs;

    public ResourceNotFoundException(String message) {
        super(message);
        this.errorCode = ErrorCodeEnum.RESOURCE_NOT_FOUND;
        this.description = message;
        this.descriptionKey = null;
        this.descriptionArgs = null;
    }

    public ResourceNotFoundException(String descriptionKey, Object[] descriptionArgs, String fallbackDescription) {
        super(fallbackDescription);
        this.errorCode = ErrorCodeEnum.RESOURCE_NOT_FOUND;
        this.description = fallbackDescription;
        this.descriptionKey = descriptionKey;
        this.descriptionArgs = descriptionArgs;
    }

    public static ResourceNotFoundException companyById(Object id) {
        return new ResourceNotFoundException(
            "error.resource.company.not.found.by.id",
            new Object[]{id},
            "Company not found for id=" + id
        );
    }

    public static ResourceNotFoundException salesmanById(Object id) {
        return new ResourceNotFoundException(
            "error.resource.salesman.not.found.by.id",
            new Object[]{id},
            "Salesman not found for id=" + id
        );
    }

    public static ResourceNotFoundException adminUserById(Object id) {
        return new ResourceNotFoundException(
            "error.resource.adminUser.not.found.by.id",
            new Object[]{id},
            "Admin user not found for id=" + id
        );
    }

    public static ResourceNotFoundException postById(Object id) {
        return new ResourceNotFoundException(
            "error.resource.post.not.found.by.id",
            new Object[]{id},
            "Post not found for id=" + id
        );
    }
}
