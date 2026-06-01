package com.snb.ms.companysalesmaninvitation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Company salesman invitation request payload")
public class CompanySalesmanInvitationRequest {

    @NotBlank(message = "{validation.companySalesmanInvitation.emailAddress.required}")
    @Email(message = "{validation.companySalesmanInvitation.emailAddress.email}")
    @Size(max = 150, message = "{validation.companySalesmanInvitation.emailAddress.size}")
    @Schema(description = "Invitee email address", example = "salesman@example.com", maxLength = 150, requiredMode = Schema.RequiredMode.REQUIRED)
    private String emailAddress;

    @NotBlank(message = "{validation.companySalesmanInvitation.mobileNumber.required}")
    @Size(max = 20, message = "{validation.companySalesmanInvitation.mobileNumber.size}")
    @Schema(description = "Invitee mobile number", example = "+971555010201", maxLength = 20, requiredMode = Schema.RequiredMode.REQUIRED)
    private String mobileNumber;

    @NotBlank(message = "{validation.companySalesmanInvitation.idNumber.required}")
    @Size(max = 50, message = "{validation.companySalesmanInvitation.idNumber.size}")
    @Schema(description = "Invitee national or identity number", example = "784-1986-0000001-1", maxLength = 50, requiredMode = Schema.RequiredMode.REQUIRED)
    private String idNumber;

    @Future(message = "{validation.companySalesmanInvitation.expiryDate.future}")
    @Schema(description = "Invitation expiry timestamp. If omitted, the API sets it to 7 days from invitation time.", example = "2026-06-08T10:15:30")
    private LocalDateTime expiryDate;
}