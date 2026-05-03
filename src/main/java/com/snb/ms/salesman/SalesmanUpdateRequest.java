package com.snb.ms.salesman;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesmanUpdateRequest {

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
}
