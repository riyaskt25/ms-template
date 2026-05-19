package com.snb.ms.companysalesman;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySalesmanDto {
    private Long companySalesmanId;
    private Long companyId;
    private Long salesmanId;
    private BigDecimal currentTarget;
    private BigDecimal achievedTarget;
    private String associationStatus;
    private LocalDateTime joinedAt;
    private LocalDateTime exitedAt;
}
