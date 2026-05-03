package com.snb.ms.shared;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersRequest {

    @NotBlank(message = "emailAddress is required")
    @Email(message = "emailAddress must be a valid email")
    @Size(max = 150, message = "emailAddress must not exceed 150 characters")
    private String emailAddress;

    @NotBlank(message = "mobileNumber is required")
    @Size(max = 20, message = "mobileNumber must not exceed 20 characters")
    private String mobileNumber;

    @Size(max = 20, message = "userType must not exceed 20 characters")
    private String userType;

    @Size(max = 20, message = "accountStatus must not exceed 20 characters")
    private String accountStatus;

    @Pattern(regexp = "^[YN]$", message = "accountLockedFlag must be Y or N")
    private String accountLockedFlag;

    @PositiveOrZero(message = "createdBy must be zero or positive")
    private Long createdBy;

    @PositiveOrZero(message = "updatedBy must be zero or positive")
    private Long updatedBy;
}
