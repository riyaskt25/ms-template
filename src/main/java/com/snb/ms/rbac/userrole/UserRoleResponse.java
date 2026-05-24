package com.snb.ms.rbac.userrole;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User-role assignment response payload")
public class UserRoleResponse {

    @Schema(description = "Assignment record identifier", example = "1001")
    private Long id;

    @Schema(description = "User identifier", example = "1001")
    private Long userId;

    @Schema(description = "User email address", example = "admin1@snb.com")
    private String userEmailAddress;

    @Schema(description = "User mobile number", example = "971500000001")
    private String userMobileNumber;

    @Schema(description = "User type", example = "ADMIN")
    private String userType;

    @Schema(description = "User account status", example = "ACTIVE")
    private String userAccountStatus;

    @Schema(description = "Role identifier", example = "1001")
    private Long roleId;

    @Schema(description = "Role code", example = "SUPER_ADMIN")
    private String roleCode;

    @Schema(description = "Role name", example = "Super Admin")
    private String roleName;

    @Schema(description = "Role description", example = "Full platform access")
    private String roleDescription;

    @Schema(description = "Identifier of actor who created the record", example = "1001")
    private Long createdBy;

    @Schema(description = "Record creation timestamp", example = "2026-05-19T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Identifier of actor who last updated the record", example = "1002")
    private Long updatedBy;

    @Schema(description = "Last update timestamp", example = "2026-05-19T11:00:00")
    private LocalDateTime updatedAt;
}
