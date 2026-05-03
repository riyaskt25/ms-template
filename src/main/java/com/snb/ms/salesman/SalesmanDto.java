package com.snb.ms.salesman;
import com.snb.ms.shared.BaseResponseDTO;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SalesmanDto extends BaseResponseDTO {
    private Long salesmanId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String accountNumber;
    private String cifNumber;
    private String idNumber;
    private BigDecimal availableIncentiveAmount;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
