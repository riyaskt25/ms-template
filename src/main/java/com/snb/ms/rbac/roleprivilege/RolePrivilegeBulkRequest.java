package com.snb.ms.rbac.roleprivilege;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role-privilege bulk grant request payload")
public class RolePrivilegeBulkRequest {

    @NotEmpty(message = "{validation.rolePrivilege.privilegeCodes.required}")
    @Valid
    @Schema(description = "Privilege codes to grant", example = "[\"USER_VIEW\",\"USER_EDIT\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<@NotBlank(message = "{validation.rolePrivilege.privilegeCode.required}") String> privilegeCodes;
}
