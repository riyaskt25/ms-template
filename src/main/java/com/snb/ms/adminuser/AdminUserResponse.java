package com.snb.ms.adminuser;
import com.snb.ms.shared.BaseResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Admin user response payload")
public class AdminUserResponse extends BaseResponseDTO {
    @Schema(description = "Unique admin user identifier", example = "1")
    private Long adminUserId;

    @Schema(description = "First name", example = "Sara")
    private String firstName;

    @Schema(description = "Middle name", example = "M")
    private String middleName;

    @Schema(description = "Last name", example = "Naseer")
    private String lastName;

    @Schema(description = "Extension number", example = "EXT-1001")
    private String extensionNumber;

    @Schema(description = "Admin role type", example = "SUPERVISOR")
    private String adminType;

    @Schema(description = "Admin status", example = "ACTIVE")
    private String adminStatus;

    @Schema(description = "Identifier of actor who created the record", example = "1001")
    private Long createdBy;

    @Schema(description = "Record creation timestamp in ISO-8601 format", example = "2026-05-04T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Identifier of actor who last updated the record", example = "1002")
    private Long updatedBy;

    @Schema(description = "Last update timestamp in ISO-8601 format", example = "2026-05-04T11:20:45")
    private LocalDateTime updatedAt;
}
