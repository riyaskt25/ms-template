package com.snb.ms.rbac.roleprivilege;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Privilege details associated with a role")
public class RoleAssociatedPrivilegeResponse {

    @Schema(description = "Privilege identifier", example = "1001")
    private Long privilegeId;

    @Schema(description = "Privilege code", example = "USER_VIEW")
    private String privilegeCode;

    @Schema(description = "Privilege name", example = "View Users")
    private String privilegeName;

    @Schema(description = "Privilege description", example = "Can view user profiles")
    private String privilegeDescription;
}
