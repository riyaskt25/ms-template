// File: src/main/java/com/snb/ms/salesman/SalesmanCreateRequest.java
package com.snb.ms.salesman;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Salesman creation request payload")
public class SalesmanCreateRequest {

  @Size(max = 100, message = "{validation.salesman.firstName.size}")
  @Schema(description = "First name", example = "Ahamed", maxLength = 100)
  private String firstName;

  @Size(max = 100, message = "{validation.salesman.middleName.size}")
  @Schema(description = "Middle name", example = "I", maxLength = 100)
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
  @Schema(
      description = "National or identity number",
      example = "784-1986-0000001-1",
      maxLength = 50)
  private String idNumber;

  @NotEmpty(message = "{validation.salesman.companyIds.required}")
  @Schema(
      description = "Linked company identifiers",
      example = "[1001, 1002]",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private List<
          @NotNull(message = "{validation.salesman.companyIds.notNull}") @Positive(message = "{validation.salesman.companyIds.positive}") Long>
      companyIds;

  @NotBlank(message = "{validation.salesman.emailAddress.required}")
  @Email(message = "{validation.salesman.emailAddress.email}")
  @Size(max = 150, message = "{validation.salesman.emailAddress.size}")
  @Schema(
      description = "Salesman email address",
      example = "ahamed.khan@example.com",
      maxLength = 150,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String emailAddress;

  @NotBlank(message = "{validation.salesman.mobileNumber.required}")
  @Size(max = 20, message = "{validation.salesman.mobileNumber.size}")
  @Schema(
      description = "Salesman mobile number",
      example = "+971555010201",
      maxLength = 20,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String mobileNumber;
}
