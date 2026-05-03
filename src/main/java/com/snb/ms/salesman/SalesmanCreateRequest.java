package com.snb.ms.salesman;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesmanCreateRequest {

    @Size(max = 100, message = "{validation.salesman.firstName.size}")
    private String firstName;

    @Size(max = 100, message = "{validation.salesman.middleName.size}")
    private String middleName;

    @Size(max = 100, message = "{validation.salesman.lastName.size}")
    private String lastName;

    @Size(max = 50, message = "{validation.salesman.accountNumber.size}")
    private String accountNumber;

    @Size(max = 50, message = "{validation.salesman.cifNumber.size}")
    private String cifNumber;

    @Size(max = 50, message = "{validation.salesman.idNumber.size}")
    private String idNumber;

    @NotNull(message = "{validation.salesman.companyId.required}")
    @Positive(message = "{validation.salesman.companyId.positive}")
    private Long companyId;

    @NotBlank(message = "{validation.salesman.emailAddress.required}")
    @Email(message = "{validation.salesman.emailAddress.email}")
    @Size(max = 150, message = "{validation.salesman.emailAddress.size}")
    private String emailAddress;

    @NotBlank(message = "{validation.salesman.mobileNumber.required}")
    @Size(max = 20, message = "{validation.salesman.mobileNumber.size}")
    private String mobileNumber;
}
