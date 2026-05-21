package com.snb.ms.rbac.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Bulk role creation request payload")
public class RoleBulkCreateRequest {

    @NotEmpty(message = "{validation.role.roles.required}")
    @Valid
    @Schema(description = "Role creation payloads", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<RoleCreateRequest> roles;
}
