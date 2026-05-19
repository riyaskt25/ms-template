package com.snb.ms.rbac.privilege;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Privilege creation request payload")
public class PrivilegeCreateRequest {

    @NotBlank(message = "{validation.privilege.privilegeCode.required}")
    @Size(max = 100, message = "{validation.privilege.privilegeCode.size}")
    @Schema(description = "Stable privilege code used in authorization checks", example = "USER_VIEW", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    private String privilegeCode;

    @NotBlank(message = "{validation.privilege.privilegeName.required}")
    @Size(max = 150, message = "{validation.privilege.privilegeName.size}")
    @Schema(description = "Human-readable privilege name", example = "View Users", maxLength = 150, requiredMode = Schema.RequiredMode.REQUIRED)
    private String privilegeName;

    @Size(max = 255, message = "{validation.privilege.description.size}")
    @Schema(description = "Privilege description", example = "Can view user profiles", maxLength = 255)
    private String description;
}
