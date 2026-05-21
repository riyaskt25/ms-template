package com.snb.ms.companysalesman;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

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
