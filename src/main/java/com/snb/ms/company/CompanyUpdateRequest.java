package com.snb.ms.company;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Company update request payload")
public class CompanyUpdateRequest {

    @NotBlank(message = "{validation.company.registrationNumber.required}")
    @Size(max = 100, message = "{validation.company.registrationNumber.size}")
    @Schema(description = "Company registration number", example = "REG-2026-0001", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    private String registrationNumber;

    @NotBlank(message = "{validation.company.emailAddress.required}")
    @Email(message = "{validation.company.emailAddress.email}")
    @Size(max = 150, message = "{validation.company.emailAddress.size}")
    @Schema(description = "Company email address", example = "company.updated@example.com", maxLength = 150, requiredMode = Schema.RequiredMode.REQUIRED)
    private String emailAddress;

    @NotBlank(message = "{validation.company.mobileNumber.required}")
    @Size(max = 20, message = "{validation.company.mobileNumber.size}")
    @Schema(description = "Company mobile number", example = "+971555010102", maxLength = 20, requiredMode = Schema.RequiredMode.REQUIRED)
    private String mobileNumber;
}
