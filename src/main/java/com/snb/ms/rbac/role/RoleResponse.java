package com.snb.ms.rbac.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role response payload")
public class RoleResponse {

    @Schema(description = "Unique role identifier", example = "1")
    private Long roleId;

    @Schema(description = "Stable role code used in authorization checks", example = "SUPER_ADMIN")
    private String roleCode;

    @Schema(description = "Human-readable role name", example = "Super Admin")
    private String roleName;

    @Schema(description = "Role description", example = "Full platform access")
    private String description;

    @Schema(description = "Identifier of actor who created the record", example = "1")
    private Long createdBy;

    @Schema(description = "Record creation timestamp", example = "2026-05-19T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Identifier of actor who last updated the record", example = "1")
    private Long updatedBy;

    @Schema(description = "Last update timestamp", example = "2026-05-19T11:00:00")
    private LocalDateTime updatedAt;
}
