package com.snb.ms.companysalesmaninvitation;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Company salesman invitation response payload")
public class CompanySalesmanInvitationResponse {
    @Schema(description = "Unique invitation identifier", example = "1001")
    private Long companySalesmanInvitationId;

    @Schema(description = "Linked salesman identifier when the invitation has been accepted and registration completed", example = "1005")
    private Long salesmanId;

    @Schema(description = "Invitee email address", example = "salesman@example.com")
    private String emailAddress;

    @Schema(description = "Invitee mobile number", example = "+971555010201")
    private String mobileNumber;

    @Schema(description = "Invitee national or identity number", example = "784-1986-0000001-1")
    private String idNumber;

    @Schema(description = "Company identifier that issued the invitation", example = "1001")
    private Long companyId;

    @Schema(description = "Current invitation status", example = "INVITED")
    private String status;

    @Schema(description = "Invitation creation timestamp in ISO-8601 format", example = "2026-06-01T10:15:30")
    private LocalDateTime invitedAt;

    @Schema(description = "Timestamp when the invitee responded, if available", example = "2026-06-02T08:30:00")
    private LocalDateTime respondedAt;

    @Schema(description = "Invitation expiry timestamp in ISO-8601 format", example = "2026-06-08T10:15:30")
    private LocalDateTime expiryDate;

    public static CompanySalesmanInvitationResponse fromDto(CompanySalesmanInvitationDto dto) {
        return new CompanySalesmanInvitationResponse(
            dto.getCompanySalesmanInvitationId(),
            dto.getSalesmanId(),
            dto.getEmailAddress(),
            dto.getMobileNumber(),
            dto.getIdNumber(),
            dto.getCompanyId(),
            dto.getStatus(),
            dto.getInvitedAt(),
            dto.getRespondedAt(),
            dto.getExpiryDate()
        );
    }
}