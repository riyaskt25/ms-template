package com.snb.ms.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {

  @NotBlank(message = "registrationNumber is required")
  @Size(max = 100, message = "registrationNumber must not exceed 100 characters")
  private String registrationNumber;

  @Size(max = 20, message = "companyStatus must not exceed 20 characters")
  private String companyStatus;

  @Size(max = 50, message = "companyType must not exceed 50 characters")
  private String companyType;

  @PositiveOrZero(message = "createdBy must be zero or positive")
  private Long createdBy;

  @PositiveOrZero(message = "updatedBy must be zero or positive")
  private Long updatedBy;
}
