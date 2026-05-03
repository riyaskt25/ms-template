package com.snb.ms.adminuser;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserRequest {

    @Size(max = 100, message = "firstName must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "middleName must not exceed 100 characters")
    private String middleName;

    @Size(max = 100, message = "lastName must not exceed 100 characters")
    private String lastName;

    @Size(max = 20, message = "extensionNumber must not exceed 20 characters")
    private String extensionNumber;

    @Size(max = 30, message = "adminType must not exceed 30 characters")
    private String adminType;

    @Size(max = 20, message = "adminStatus must not exceed 20 characters")
    private String adminStatus;

    @PositiveOrZero(message = "createdBy must be zero or positive")
    private Long createdBy;

    @PositiveOrZero(message = "updatedBy must be zero or positive")
    private Long updatedBy;
}
