package com.snb.ms.rbac.userrole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User-role assignment response payload")
public class UserRoleResponse {

    @Schema(description = "Assignment record identifier", example = "1")
    private Long id;

    @Schema(description = "User identifier", example = "10")
    private Long userId;

    @Schema(description = "Role identifier", example = "1")
    private Long roleId;

    @Schema(description = "Role code", example = "SUPER_ADMIN")
    private String roleCode;

    @Schema(description = "Role name", example = "Super Admin")
    private String roleName;

    @Schema(description = "Identifier of actor who created the record", example = "1")
    private Long createdBy;

    @Schema(description = "Record creation timestamp", example = "2026-05-19T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Identifier of actor who last updated the record", example = "1")
    private Long updatedBy;

    @Schema(description = "Last update timestamp", example = "2026-05-19T11:00:00")
    private LocalDateTime updatedAt;
}
