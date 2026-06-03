package com.snb.ms.rbac.userrole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User-role assignment request payload")
public class UserRoleRequest {

  @NotEmpty(message = "{validation.userRole.roleCodes.required}")
  @Valid
  @Schema(
      description = "Role codes to assign",
      example = "[\"SUPER_ADMIN\",\"AUDITOR\"]",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private List<@NotBlank(message = "{validation.userRole.roleCode.required}") String> roleCodes;
}
