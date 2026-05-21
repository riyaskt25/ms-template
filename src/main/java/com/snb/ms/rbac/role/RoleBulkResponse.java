package com.snb.ms.rbac.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Bulk role response payload")
public class RoleBulkResponse {

    @Schema(description = "Created role records")
    private List<RoleResponse> roles;
}
