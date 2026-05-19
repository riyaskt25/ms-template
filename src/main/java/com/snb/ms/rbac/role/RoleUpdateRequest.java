package com.snb.ms.rbac.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role update request payload")
public class RoleUpdateRequest {

    @NotBlank(message = "{validation.role.roleName.required}")
    @Size(max = 150, message = "{validation.role.roleName.size}")
    @Schema(description = "Human-readable role name", example = "Super Admin", maxLength = 150, requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleName;

    @Size(max = 255, message = "{validation.role.description.size}")
    @Schema(description = "Role description", example = "Full platform access", maxLength = 255)
    private String description;
}
