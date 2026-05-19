package com.snb.ms.rbac.userrole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User-role assignment request payload")
public class UserRoleRequest {

    @NotNull(message = "{validation.userRole.userId.required}")
    @Positive(message = "{validation.common.id.positive}")
    @Schema(description = "User identifier to assign the role to", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotNull(message = "{validation.userRole.roleId.required}")
    @Positive(message = "{validation.common.id.positive}")
    @Schema(description = "Role identifier to assign", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long roleId;
}
