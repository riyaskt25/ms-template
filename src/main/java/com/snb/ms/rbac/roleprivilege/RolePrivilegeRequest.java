package com.snb.ms.rbac.roleprivilege;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role-privilege grant request payload")
public class RolePrivilegeRequest {

    @NotNull(message = "{validation.rolePrivilege.privilegeCode.required}")
    @NotBlank(message = "{validation.rolePrivilege.privilegeCode.required}")
    @Schema(description = "Privilege code to grant", example = "USER_VIEW", requiredMode = Schema.RequiredMode.REQUIRED)
    private String privilegeCode;
}
