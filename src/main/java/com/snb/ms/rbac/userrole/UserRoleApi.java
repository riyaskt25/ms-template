package com.snb.ms.rbac.userrole;

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

@Tag(name = "User Roles", description = "RBAC user-role assignment operations")
public interface UserRoleApi {

    @Operation(operationId = "getAllUserRoles", summary = "List all user-role assignments", description = "Returns all active user-role assignments.")
    @CommonApiParameters
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Assignments fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserRoleResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    List<UserRoleResponse> findAll();

    @Operation(operationId = "getRolesByUserId", summary = "Get roles by user id", description = "Returns all active role assignments for a user.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "userId", description = "User identifier", required = true, example = "10")})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User roles fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserRoleResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    List<UserRoleResponse> findByUserId(@Positive(message = "{validation.common.id.positive}") Long userId);

    @Operation(operationId = "assignRoleToUser", summary = "Assign role to user", description = "Creates a user-role assignment.")
    @CommonApiParameters
    @RequestBody(required = true, description = "Assignment payload",
        content = @Content(schema = @Schema(implementation = UserRoleRequest.class),
            examples = @ExampleObject(name = "AssignRole",
                value = "{\n  \"userId\": 10,\n  \"roleId\": 1\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Role assigned successfully",
            content = @Content(schema = @Schema(implementation = UserRoleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed or role already assigned",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "User or role not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    ResponseEntity<UserRoleResponse> assign(@Valid UserRoleRequest request);

    @Operation(operationId = "revokeUserRole", summary = "Revoke user-role assignment", description = "Soft-deletes a user-role assignment by its record id.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "id", description = "Assignment record identifier", required = true, example = "1")})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role revoked successfully",
            content = @Content(schema = @Schema(implementation = UserRoleResponse.class))),
        @ApiResponse(responseCode = "404", description = "Assignment not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "NotFound",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"User-role assignment not found for id=999\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    UserRoleResponse revoke(@Positive(message = "{validation.common.id.positive}") Long id);
}
