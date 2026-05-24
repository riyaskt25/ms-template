package com.snb.ms.adminuser;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Admin user update request payload")
public class AdminUserUpdateRequest {

    @NotBlank(message = "{validation.adminUser.firstName.required}")
    @Size(max = 100, message = "{validation.adminUser.firstName.size}")
    @Schema(description = "First name", example = "Sara", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "{validation.adminUser.middleName.required}")
    @Size(max = 100, message = "{validation.adminUser.middleName.size}")
    @Schema(description = "Middle name", example = "Mariam", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    private String middleName;

    @NotBlank(message = "{validation.adminUser.lastName.required}")
    @Size(max = 100, message = "{validation.adminUser.lastName.size}")
    @Schema(description = "Last name", example = "Naseer", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @NotBlank(message = "{validation.adminUser.extensionNumber.required}")
    @Size(max = 20, message = "{validation.adminUser.extensionNumber.size}")
    @Schema(description = "Extension number", example = "EXT-1002", maxLength = 20, requiredMode = Schema.RequiredMode.REQUIRED)
    private String extensionNumber;

    @NotBlank(message = "{validation.adminUser.snbId.required}")
    @Size(max = 50, message = "{validation.adminUser.snbId.size}")
    @Schema(description = "Organization employee identifier", example = "SNB1002", maxLength = 50, requiredMode = Schema.RequiredMode.REQUIRED)
    private String snbId;

    @NotBlank(message = "{validation.adminUser.emailAddress.required}")
    @Email(message = "{validation.adminUser.emailAddress.email}")
    @Size(max = 150, message = "{validation.adminUser.emailAddress.size}")
    @Schema(description = "Admin user email address", example = "sara.naseer@example.com", maxLength = 150, requiredMode = Schema.RequiredMode.REQUIRED)
    private String emailAddress;

    @NotBlank(message = "{validation.adminUser.mobileNumber.required}")
    @Size(max = 20, message = "{validation.adminUser.mobileNumber.size}")
    @Schema(description = "Admin user mobile number", example = "+971555010302", maxLength = 20, requiredMode = Schema.RequiredMode.REQUIRED)
    private String mobileNumber;
}
