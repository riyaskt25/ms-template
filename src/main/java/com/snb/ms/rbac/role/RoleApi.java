package com.snb.ms.rbac.role;

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

@Tag(name = "Roles", description = "RBAC role management operations")
public interface RoleApi {

    @Operation(operationId = "getAllRoles", summary = "List roles", description = "Returns all active role records.")
    @CommonApiParameters
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Roles fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    List<RoleResponse> findAll();

    @Operation(operationId = "getRoleByCode", summary = "Get role by code", description = "Finds a role by code.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role fetched successfully",
            content = @Content(schema = @Schema(implementation = RoleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid role code supplied",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Role not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "RoleNotFound",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role not found for code=INVALID\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    RoleResponse findByCode(String roleCode);

    @Operation(operationId = "createRole", summary = "Create role", description = "Creates a new role record.")
    @CommonApiParameters
    @RequestBody(required = true, description = "Role payload to create",
        content = @Content(schema = @Schema(implementation = RoleCreateRequest.class),
            examples = @ExampleObject(name = "CreateRole",
                value = "{\n  \"roleCode\": \"SUPER_ADMIN\",\n  \"roleName\": \"Super Admin\",\n  \"description\": \"Full platform access\"\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Role created successfully",
            content = @Content(schema = @Schema(implementation = RoleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    ResponseEntity<RoleResponse> create(@Valid RoleCreateRequest request);

    @Operation(operationId = "createRolesBulk", summary = "Create roles in bulk", description = "Creates multiple role records in a single request.")
    @CommonApiParameters
    @RequestBody(required = true, description = "Bulk role payload to create",
        content = @Content(schema = @Schema(implementation = RoleBulkCreateRequest.class),
            examples = @ExampleObject(name = "CreateRolesBulk",
                value = "{\n  \"roles\": [\n    {\n      \"roleCode\": \"SUPER_ADMIN\",\n      \"roleName\": \"Super Admin\",\n      \"description\": \"Full platform access\"\n    },\n    {\n      \"roleCode\": \"AUDITOR\",\n      \"roleName\": \"Auditor\",\n      \"description\": \"Read-only audit access\"\n    }\n  ]\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Roles created successfully",
            content = @Content(schema = @Schema(implementation = RoleBulkResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    ResponseEntity<RoleBulkResponse> createBulk(@Valid RoleBulkCreateRequest request);

    @Operation(operationId = "updateRole", summary = "Update role", description = "Updates an existing role record by code.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")})
    @RequestBody(required = true, description = "Role payload to update",
        content = @Content(schema = @Schema(implementation = RoleUpdateRequest.class),
            examples = @ExampleObject(name = "UpdateRole",
                value = "{\n  \"roleName\": \"Super Administrator\",\n  \"description\": \"Updated description\"\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role updated successfully",
            content = @Content(schema = @Schema(implementation = RoleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Role not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "RoleNotFound",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role not found for code=INVALID\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    RoleResponse update(String roleCode, @Valid RoleUpdateRequest request);

    @Operation(operationId = "deleteRole", summary = "Delete role", description = "Soft-deletes a role record by code.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role deleted successfully",
            content = @Content(schema = @Schema(implementation = RoleResponse.class))),
        @ApiResponse(responseCode = "404", description = "Role not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "RoleNotFound",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role not found for code=INVALID\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
    })
    RoleResponse softDelete(String roleCode);
}
