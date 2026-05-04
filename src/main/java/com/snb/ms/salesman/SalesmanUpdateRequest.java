package com.snb.ms.salesman;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Salesman update request payload")
public class SalesmanUpdateRequest {

    @Size(max = 100, message = "{validation.salesman.firstName.size}")
    @Schema(description = "First name", example = "Ahamed", maxLength = 100)
    private String firstName;

    @Size(max = 100, message = "{validation.salesman.middleName.size}")
    @Schema(description = "Middle name", example = "Ibrahim", maxLength = 100)
    private String middleName;

    @Size(max = 100, message = "{validation.salesman.lastName.size}")
    @Schema(description = "Last name", example = "Khan", maxLength = 100)
    private String lastName;

    @Size(max = 50, message = "{validation.salesman.accountNumber.size}")
    @Schema(description = "Account number", example = "ACC-9911", maxLength = 50)
    private String accountNumber;

    @Size(max = 50, message = "{validation.salesman.cifNumber.size}")
    @Schema(description = "Customer information file number", example = "CIF-1022", maxLength = 50)
    private String cifNumber;

    @Size(max = 50, message = "{validation.salesman.idNumber.size}")
    @Schema(description = "National or identity number", example = "784-1986-0000001-1", maxLength = 50)
    private String idNumber;
}
