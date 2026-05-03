package com.snb.ms.company;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreateRequest {

    @NotBlank(message = "{validation.company.registrationNumber.required}")
    @Size(max = 100, message = "{validation.company.registrationNumber.size}")
    private String registrationNumber;

    @NotBlank(message = "{validation.company.emailAddress.required}")
    @Email(message = "{validation.company.emailAddress.email}")
    @Size(max = 150, message = "{validation.company.emailAddress.size}")
    private String emailAddress;

    @NotBlank(message = "{validation.company.mobileNumber.required}")
    @Size(max = 20, message = "{validation.company.mobileNumber.size}")
    private String mobileNumber;
}
