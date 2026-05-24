package com.snb.ms.rbac.userrole;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User details with associated role details")
public class UserRolesAggregateResponse {

    @Schema(description = "User identifier", example = "10")
    private Long userId;

    @Schema(description = "User email address", example = "admin1@snb.com")
    private String userEmailAddress;

    @Schema(description = "User mobile number", example = "971500000001")
    private String userMobileNumber;

    @Schema(description = "User type", example = "ADMIN")
    private String userType;

    @Schema(description = "User account status", example = "ACTIVE")
    private String userAccountStatus;

    @ArraySchema(schema = @Schema(implementation = UserAssociatedRoleResponse.class), arraySchema = @Schema(description = "Associated roles"))
    private List<UserAssociatedRoleResponse> roles;
}
