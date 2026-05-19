package com.snb.ms.rbac.roleprivilege;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role-privilege grant response payload")
public class RolePrivilegeResponse {

    @Schema(description = "Grant record identifier", example = "1")
    private Long id;

    @Schema(description = "Role identifier", example = "1")
    private Long roleId;

    @Schema(description = "Role code", example = "SUPER_ADMIN")
    private String roleCode;

    @Schema(description = "Privilege identifier", example = "1")
    private Long privilegeId;

    @Schema(description = "Privilege code", example = "USER_VIEW")
    private String privilegeCode;

    @Schema(description = "Privilege name", example = "View Users")
    private String privilegeName;

    @Schema(description = "Identifier of actor who created the record", example = "1")
    private Long createdBy;

    @Schema(description = "Record creation timestamp", example = "2026-05-19T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Identifier of actor who last updated the record", example = "1")
    private Long updatedBy;

    @Schema(description = "Last update timestamp", example = "2026-05-19T11:00:00")
    private LocalDateTime updatedAt;
}
