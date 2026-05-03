package com.snb.ms.companysalesman;
import com.snb.ms.shared.BaseResponseDTO;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CompanySalesmanDto extends BaseResponseDTO {
    private Long companySalesmanId;
    private Long companyId;
    private Long salesmanId;
    private BigDecimal currentTarget;
    private BigDecimal achievedTarget;
    private String associationStatus;
    private LocalDateTime joinedAt;
    private LocalDateTime exitedAt;
}
