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
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Role Privileges", description = "RBAC role-privilege grant operations")
public interface RolePrivilegeApi {

    @Operation(operationId = "getAllRolePrivileges", summary = "List all role-privilege grants", description = "Returns all active role-privilege grants.")
    @CommonApiParameters
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grants fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RolePrivilegeResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    List<RolePrivilegeResponse> findAll();

    @Operation(operationId = "getPrivilegesByRoleId", summary = "Get privileges by role id", description = "Returns all active privilege grants for a role.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleId", description = "Role identifier", required = true, example = "1")})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role privileges fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RolePrivilegeResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    List<RolePrivilegeResponse> findByRoleId(@Positive(message = "{validation.common.id.positive}") Long roleId);

    @Operation(operationId = "grantPrivilegeToRole", summary = "Grant privilege to role", description = "Creates a role-privilege grant.")
    @CommonApiParameters
    @RequestBody(required = true, description = "Grant payload",
        content = @Content(schema = @Schema(implementation = RolePrivilegeRequest.class),
            examples = @ExampleObject(name = "GrantPrivilege",
                value = "{\n  \"roleId\": 1,\n  \"privilegeId\": 1\n}")))
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
    ResponseEntity<RolePrivilegeResponse> grant(@Valid RolePrivilegeRequest request);

    @Operation(operationId = "revokeRolePrivilege", summary = "Revoke role-privilege grant", description = "Soft-deletes a role-privilege grant by its record id.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "id", description = "Grant record identifier", required = true, example = "1")})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Privilege revoked successfully",
            content = @Content(schema = @Schema(implementation = RolePrivilegeResponse.class))),
        @ApiResponse(responseCode = "404", description = "Grant not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "NotFound",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role-privilege grant not found for id=999\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    RolePrivilegeResponse revoke(@Positive(message = "{validation.common.id.positive}") Long id);
}
