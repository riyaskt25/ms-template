package com.snb.ms.rbac.roleprivilege;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Aggregated role-privilege response payload")
public class RolePrivilegesAggregateResponse {

    @Schema(description = "Role identifier", example = "1001")
    private Long roleId;

    @Schema(description = "Role code", example = "SUPER_ADMIN")
    private String roleCode;

    @Schema(description = "Role name", example = "Super Admin")
    private String roleName;

    @Schema(description = "Role description", example = "Full platform access")
    private String roleDescription;

    @Schema(description = "Privileges assigned to the role")
    private List<RoleAssociatedPrivilegeResponse> privileges;
}
