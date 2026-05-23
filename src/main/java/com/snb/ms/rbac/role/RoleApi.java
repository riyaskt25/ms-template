package com.snb.ms.rbac.role;

import com.snb.ms.shared.api.CommonApiParameters;
import com.snb.ms.shared.BaseResponseDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Roles", description = "RBAC role management operations")
public interface RoleApi {

    @Operation(operationId = "getAllRoles", summary = "List roles", description = "Returns all active role records.")
    @CommonApiParameters
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Roles fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GetAllRolesInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch roles\",\n      \"description\": \"Database unavailable while listing roles\"\n    }\n  ]\n}")))
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
                examples = @ExampleObject(name = "GetRoleByCodeNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role not found for code=INVALID\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GetRoleByCodeInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch role\",\n      \"description\": \"Database unavailable while loading role for code=SUPER_ADMIN\"\n    }\n  ]\n}")))
    })
    RoleResponse findByCode(String roleCode);

    @Operation(operationId = "createRole", summary = "Create role", description = "Creates a new role record.")
    @CommonApiParameters
    @RequestBody(required = true, description = "Role payload to create",
        content = @Content(schema = @Schema(implementation = RoleCreateRequest.class),
            examples = @ExampleObject(name = "CreateRoleRequestExample",
                value = "{\n  \"roleCode\": \"SUPER_ADMIN\",\n  \"roleName\": \"Super Admin\",\n  \"description\": \"Full platform access\"\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Role created successfully",
            content = @Content(schema = @Schema(implementation = RoleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "CreateRoleValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"ROLECODE_REQUIRED\",\n      \"message\": \"roleCode is required\",\n      \"description\": \"Field 'roleCode' must not be blank\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "CreateRoleInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to create role\",\n      \"description\": \"Database transaction failed while creating role SUPER_ADMIN\"\n    }\n  ]\n}")))
    })
    ResponseEntity<RoleResponse> create(@Valid RoleCreateRequest request);

    @Operation(operationId = "createRolesBulk", summary = "Create roles in bulk", description = "Creates multiple role records in a single request.")
    @CommonApiParameters
    @RequestBody(required = true, description = "Bulk role payload to create",
        content = @Content(schema = @Schema(implementation = RoleBulkCreateRequest.class),
            examples = @ExampleObject(name = "CreateRolesBulkRequestExample",
                value = "{\n  \"roles\": [\n    {\n      \"roleCode\": \"SUPER_ADMIN\",\n      \"roleName\": \"Super Admin\",\n      \"description\": \"Full platform access\"\n    },\n    {\n      \"roleCode\": \"AUDITOR\",\n      \"roleName\": \"Auditor\",\n      \"description\": \"Read-only audit access\"\n    }\n  ]\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Roles created successfully",
            content = @Content(schema = @Schema(implementation = RoleBulkResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "CreateRolesBulkValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"ROLES_REQUIRED\",\n      \"message\": \"at least one role is required\",\n      \"description\": \"Field 'roles' must contain at least one value\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "CreateRolesBulkInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to create roles in bulk\",\n      \"description\": \"Database unavailable while creating bulk roles\"\n    }\n  ]\n}")))
    })
    ResponseEntity<RoleBulkResponse> createBulk(@Valid RoleBulkCreateRequest request);

    @Operation(operationId = "updateRole", summary = "Update role", description = "Updates an existing role record by code.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")})
    @RequestBody(required = true, description = "Role payload to update",
        content = @Content(schema = @Schema(implementation = RoleUpdateRequest.class),
            examples = @ExampleObject(name = "UpdateRoleRequestExample",
                value = "{\n  \"roleName\": \"Super Administrator\",\n  \"description\": \"Updated description\"\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role updated successfully",
            content = @Content(schema = @Schema(implementation = RoleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "UpdateRoleValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"ROLENAME_REQUIRED\",\n      \"message\": \"roleName is required\",\n      \"description\": \"Field 'roleName' must not be blank\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "404", description = "Role not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "UpdateRoleNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role not found for code=INVALID\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "UpdateRoleInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to update role\",\n      \"description\": \"Database unavailable while updating role SUPER_ADMIN\"\n    }\n  ]\n}")))
    })
    RoleResponse update(String roleCode, @Valid RoleUpdateRequest request);

    @Operation(operationId = "deleteRole", summary = "Delete role", description = "Soft-deletes a role record by code.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")})
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "DeleteRoleNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role not found for code=INVALID\"\n    }\n  ]\n}")))
    })
    ResponseEntity<Void> softDelete(String roleCode);
}
