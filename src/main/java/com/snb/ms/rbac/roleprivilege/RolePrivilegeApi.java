package com.snb.ms.rbac.roleprivilege;

import com.snb.ms.shared.BaseResponseDTO;
import com.snb.ms.shared.api.CommonApiParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Role Privileges", description = "RBAC role-privilege grant operations")
public interface RolePrivilegeApi {

    @Operation(operationId = "getPrivilegesByRoleCode", summary = "Get privileges by role code", description = "Returns all active privilege grants for a role.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role privileges fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RolePrivilegeResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    List<RolePrivilegeResponse> findByRoleCode(String roleCode);

    @Operation(operationId = "grantPrivilegeToRole", summary = "Grant privilege to role", description = "Creates a role-privilege grant.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")})
    @RequestBody(required = true, description = "Grant payload",
        content = @Content(schema = @Schema(implementation = RolePrivilegeRequest.class),
            examples = @ExampleObject(name = "GrantPrivilege",
                value = "{\n  \"privilegeCode\": \"USER_VIEW\"\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Privilege granted successfully",
            content = @Content(schema = @Schema(implementation = RolePrivilegeResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed or privilege already granted",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Role or privilege not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    ResponseEntity<RolePrivilegeResponse> grant(String roleCode, @Valid RolePrivilegeRequest request);

    @Operation(operationId = "grantPrivilegesToRoleBulk", summary = "Grant privileges to role in bulk", description = "Creates multiple role-privilege grants in one request.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")})
    @RequestBody(required = true, description = "Bulk grant payload",
        content = @Content(schema = @Schema(implementation = RolePrivilegeBulkRequest.class),
            examples = @ExampleObject(name = "GrantPrivilegesBulk",
                value = "{\n  \"privilegeCodes\": [\"USER_VIEW\", \"USER_EDIT\"]\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Privileges granted successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RolePrivilegeResponse.class)))),
        @ApiResponse(responseCode = "400", description = "Validation failed or privilege already granted",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Role or privilege not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    ResponseEntity<List<RolePrivilegeResponse>> grantBulk(String roleCode, @Valid RolePrivilegeBulkRequest request);

    @Operation(operationId = "revokeRolePrivilege", summary = "Revoke role-privilege grant", description = "Soft-deletes a role-privilege grant by roleCode and privilegeCode.")
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN"),
        @Parameter(name = "privilegeCode", description = "Privilege code", required = true, example = "USER_VIEW")
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Privilege revoked successfully",
            content = @Content(schema = @Schema(implementation = RolePrivilegeResponse.class))),
        @ApiResponse(responseCode = "404", description = "Grant not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "NotFound",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role-privilege grant not found for roleCode=SUPER_ADMIN and privilegeCode=USER_VIEW\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    RolePrivilegeResponse revoke(
        String roleCode,
        String privilegeCode
    );
}
