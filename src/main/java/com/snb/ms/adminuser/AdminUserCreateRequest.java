package com.snb.ms.adminuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserCreateRequest {

    @NotBlank(message = "{validation.adminUser.firstName.required}")
    @Size(max = 100, message = "{validation.adminUser.firstName.size}")
    private String firstName;

    @NotBlank(message = "{validation.adminUser.middleName.required}")
    @Size(max = 100, message = "{validation.adminUser.middleName.size}")
    private String middleName;

    @NotBlank(message = "{validation.adminUser.lastName.required}")
    @Size(max = 100, message = "{validation.adminUser.lastName.size}")
    private String lastName;

    @NotBlank(message = "{validation.adminUser.extensionNumber.required}")
    @Size(max = 20, message = "{validation.adminUser.extensionNumber.size}")
    private String extensionNumber;

    @NotBlank(message = "{validation.adminUser.emailAddress.required}")
    @Email(message = "{validation.adminUser.emailAddress.email}")
    @Size(max = 150, message = "{validation.adminUser.emailAddress.size}")
    private String emailAddress;

    @NotBlank(message = "{validation.adminUser.mobileNumber.required}")
    @Size(max = 20, message = "{validation.adminUser.mobileNumber.size}")
    private String mobileNumber;
}
