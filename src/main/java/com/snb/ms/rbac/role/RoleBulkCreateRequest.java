package com.snb.ms.rbac.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
