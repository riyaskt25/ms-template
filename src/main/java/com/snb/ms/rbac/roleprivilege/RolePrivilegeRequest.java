package com.snb.ms.rbac.roleprivilege;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role-privilege grant request payload")
public class RolePrivilegeRequest {

    @NotNull(message = "{validation.rolePrivilege.roleId.required}")
    @Positive(message = "{validation.common.id.positive}")
    @Schema(description = "Role identifier to grant the privilege to", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long roleId;

    @NotNull(message = "{validation.rolePrivilege.privilegeId.required}")
    @Positive(message = "{validation.common.id.positive}")
    @Schema(description = "Privilege identifier to grant", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long privilegeId;
}
