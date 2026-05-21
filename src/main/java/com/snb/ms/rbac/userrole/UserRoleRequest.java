package com.snb.ms.rbac.userrole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User-role assignment request payload")
public class UserRoleRequest {

    @NotEmpty(message = "{validation.userRole.roleIds.required}")
    @Valid
    @Schema(description = "Role identifiers to assign", example = "[1,2,3]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<@Positive(message = "{validation.common.id.positive}") Long> roleIds;
}
