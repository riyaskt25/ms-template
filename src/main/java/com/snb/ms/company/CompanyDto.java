package com.snb.ms.company;
import com.snb.ms.shared.BaseResponseDTO;

import lombok.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto extends BaseResponseDTO {
    private Long companyId;
    private String emailAddress;
    private String mobileNumber;
    private String registrationNumber;
    private String companyStatus;
    private String companyType;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
