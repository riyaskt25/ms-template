package com.snb.ms.company;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Company status decision request payload")
public class CompanyStatusDecisionRequest {

    @NotBlank(message = "{validation.company.status.required}")
    @Schema(description = "Target company status decided by administrator", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;
}