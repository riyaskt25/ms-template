package com.snb.ms.companysalesman;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySalesmanRequest {

    @DecimalMin(value = "0.0", inclusive = true, message = "currentTarget must be zero or positive")
    private BigDecimal currentTarget;

    @DecimalMin(value = "0.0", inclusive = true, message = "achievedTarget must be zero or positive")
    private BigDecimal achievedTarget;

    @Size(max = 20, message = "associationStatus must not exceed 20 characters")
    private String associationStatus;

    private LocalDateTime joinedAt;

    private LocalDateTime exitedAt;
}
