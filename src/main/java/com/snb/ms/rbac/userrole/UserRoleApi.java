package com.snb.ms.rbac.userrole;

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
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "User Roles", description = "RBAC user-role assignment operations")
public interface UserRoleApi {

    @Operation(operationId = "getUserRoles", summary = "Get roles by user id", description = "Returns all active role assignments for a user.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "userId", description = "User identifier", required = true, example = "10")})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User roles fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserRoleResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GetUserRolesInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch user roles\",\n      \"description\": \"Database unavailable while loading roles for userId=10\"\n    }\n  ]\n}")))
    })
    List<UserRoleResponse> findByUserId(@Positive(message = "{validation.common.id.positive}") Long userId);

    @Operation(operationId = "assignUserRoles", summary = "Assign roles to user", description = "Creates one or more user-role assignments.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "userId", description = "User identifier", required = true, example = "10")})
    @RequestBody(required = true, description = "Assignment payload",
        content = @Content(schema = @Schema(implementation = UserRoleRequest.class),
            examples = @ExampleObject(name = "AssignUserRolesRequestExample",
                value = "{\n  \"roleCodes\": [\"SUPER_ADMIN\", \"AUDITOR\"]\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Roles assigned successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserRoleResponse.class)))),
        @ApiResponse(responseCode = "400", description = "Validation failed or role already assigned",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "AssignUserRolesValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"ROLECODES_REQUIRED\",\n      \"message\": \"at least one roleCode is required\",\n      \"description\": \"Field 'roleCodes' must contain at least one value\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "404", description = "User or role not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "AssignUserRolesNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role not found for code=INVALID_ROLE\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "AssignUserRolesInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to assign roles\",\n      \"description\": \"Database unavailable while assigning roles to userId=10\"\n    }\n  ]\n}")))
    })
    ResponseEntity<List<UserRoleResponse>> assign(@Positive(message = "{validation.common.id.positive}") Long userId, @Valid UserRoleRequest request);

    @Operation(operationId = "replaceUserRoles", summary = "Replace user roles", description = "Replaces all active role assignments for a user with the provided roles.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "userId", description = "User identifier", required = true, example = "10")})
    @RequestBody(required = true, description = "Role replacement payload",
        content = @Content(schema = @Schema(implementation = UserRoleRequest.class),
            examples = @ExampleObject(name = "ReplaceUserRolesRequestExample",
                value = "{\n  \"roleCodes\": [\"SUPER_ADMIN\", \"AUDITOR\"]\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User roles updated successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserRoleResponse.class)))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "ReplaceUserRolesValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"ROLECODES_REQUIRED\",\n      \"message\": \"at least one roleCode is required\",\n      \"description\": \"Field 'roleCodes' must contain at least one value\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "404", description = "User or role not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "ReplaceUserRolesNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role not found for code=INVALID_ROLE\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "ReplaceUserRolesInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to replace user roles\",\n      \"description\": \"Database unavailable while replacing roles for userId=10\"\n    }\n  ]\n}")))
    })
    List<UserRoleResponse> replace(@Positive(message = "{validation.common.id.positive}") Long userId, @Valid UserRoleRequest request);

    @Operation(operationId = "revokeUserRole", summary = "Revoke user-role assignment", description = "Soft-deletes a user-role assignment by userId and roleCode.")
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "userId", description = "User identifier", required = true, example = "10"),
        @Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role revoked successfully",
            content = @Content(schema = @Schema(implementation = UserRoleResponse.class))),
        @ApiResponse(responseCode = "404", description = "Assignment not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "RevokeUserRoleNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"User-role assignment not found for userId=10 and roleCode=INVALID\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "RevokeUserRoleInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to revoke user role\",\n      \"description\": \"Database unavailable while revoking roleCode=SUPER_ADMIN for userId=10\"\n    }\n  ]\n}")))
    })
    UserRoleResponse revoke(
        @Positive(message = "{validation.common.id.positive}") Long userId,
        String roleCode
    );
}
