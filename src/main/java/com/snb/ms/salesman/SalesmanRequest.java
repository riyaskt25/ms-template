package com.snb.ms.salesman;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesmanRequest {

  @Size(max = 100, message = "firstName must not exceed 100 characters")
  private String firstName;

  @Size(max = 100, message = "middleName must not exceed 100 characters")
  private String middleName;

  @Size(max = 100, message = "lastName must not exceed 100 characters")
  private String lastName;

  @Size(max = 50, message = "accountNumber must not exceed 50 characters")
  private String accountNumber;

  @Size(max = 50, message = "cifNumber must not exceed 50 characters")
  private String cifNumber;

  @Size(max = 50, message = "idNumber must not exceed 50 characters")
  private String idNumber;

  @DecimalMin(
      value = "0.0",
      inclusive = true,
      message = "availableIncentiveAmount must be zero or positive")
  private BigDecimal availableIncentiveAmount;

  @PositiveOrZero(message = "createdBy must be zero or positive")
  private Long createdBy;

  @PositiveOrZero(message = "updatedBy must be zero or positive")
  private Long updatedBy;
}
