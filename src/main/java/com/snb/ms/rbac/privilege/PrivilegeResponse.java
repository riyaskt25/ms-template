package com.snb.ms.rbac.privilege;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Privilege response payload")
public class PrivilegeResponse {

    @Schema(description = "Unique privilege identifier", example = "1001")
    private Long privilegeId;

    @Schema(description = "Stable privilege code used in authorization checks", example = "USER_VIEW")
    private String privilegeCode;

    @Schema(description = "Human-readable privilege name", example = "View Users")
    private String privilegeName;

    @Schema(description = "Privilege description", example = "Can view user profiles")
    private String description;

    @Schema(description = "Identifier of actor who created the record", example = "1001")
    private Long createdBy;

    @Schema(description = "Record creation timestamp", example = "2026-05-19T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Identifier of actor who last updated the record", example = "1002")
    private Long updatedBy;

    @Schema(description = "Last update timestamp", example = "2026-05-19T11:00:00")
    private LocalDateTime updatedAt;
}
