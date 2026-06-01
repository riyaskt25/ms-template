package com.snb.ms.companysalesmaninvitation;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySalesmanInvitationDto {
    private Long companySalesmanInvitationId;
    private Long salesmanId;
    private String emailAddress;
    private String mobileNumber;
    private String idNumber;
    private Long companyId;
    private String status;
    private LocalDateTime invitedAt;
    private LocalDateTime respondedAt;
    private LocalDateTime expiryDate;
}