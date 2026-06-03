package com.snb.ms.rbac.userrole;

import com.snb.ms.shared.BaseResponseDTO;
import com.snb.ms.shared.api.CommonApiParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

@Tag(name = "[505] - User Roles", description = "RBAC user-role assignment operations")
public interface UserRoleApi {

  @Operation(
      operationId = "getUserRoles",
      summary = "Get roles by user id",
      description = "Returns all active role assignments for a user.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "userId", description = "User identifier", required = true, example = "1001")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User roles fetched successfully",
        content =
            @Content(
                schema = @Schema(implementation = UserRolesAggregateResponse.class),
                examples =
                    @ExampleObject(
                        name = "GetUserRolesSuccess",
                        value =
                            "{\n  \"userId\": 1001,\n  \"userEmailAddress\": \"admin1@snb.com\",\n  \"userMobileNumber\": \"971500000001\",\n  \"userType\": \"ADMIN\",\n  \"userAccountStatus\": \"ACTIVE\",\n  \"roles\": [\n    {\n      \"roleId\": 1001,\n      \"roleCode\": \"SUPER_ADMIN\",\n      \"roleName\": \"Super Admin\",\n      \"roleDescription\": \"Full platform access\"\n    },\n    {\n      \"roleId\": 1002,\n      \"roleCode\": \"AUDITOR\",\n      \"roleName\": \"Auditor\",\n      \"roleDescription\": \"Read-only audit access\"\n    }\n  ]\n}"))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "GetUserRolesInternalError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch user roles\",\n      \"description\": \"Database unavailable while loading roles for userId=1001\"\n    }\n  ]\n}")))
  })
  UserRolesAggregateResponse findByUserId(
      @Positive(message = "{validation.common.id.positive}") Long userId);

  @Operation(
      operationId = "assignUserRoles",
      summary = "Assign roles to user",
      description = "Creates one or more user-role assignments.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "userId", description = "User identifier", required = true, example = "1001")
  })
  @RequestBody(
      required = true,
      description = "Assignment payload",
      content =
          @Content(
              schema = @Schema(implementation = UserRoleRequest.class),
              examples =
                  @ExampleObject(
                      name = "AssignUserRolesRequestExample",
                      value = "{\n  \"roleCodes\": [\"SUPER_ADMIN\", \"AUDITOR\"]\n}")))
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Roles assigned successfully",
        content =
            @Content(
                schema = @Schema(implementation = UserRolesAggregateResponse.class),
                examples =
                    @ExampleObject(
                        name = "AssignUserRolesSuccess",
                        value =
                            "{\n  \"userId\": 1001,\n  \"userEmailAddress\": \"admin1@snb.com\",\n  \"userMobileNumber\": \"971500000001\",\n  \"userType\": \"ADMIN\",\n  \"userAccountStatus\": \"ACTIVE\",\n  \"roles\": [\n    {\n      \"roleId\": 1001,\n      \"roleCode\": \"SUPER_ADMIN\",\n      \"roleName\": \"Super Admin\",\n      \"roleDescription\": \"Full platform access\"\n    },\n    {\n      \"roleId\": 1002,\n      \"roleCode\": \"AUDITOR\",\n      \"roleName\": \"Auditor\",\n      \"roleDescription\": \"Read-only audit access\"\n    }\n  ]\n}"))),
    @ApiResponse(
        responseCode = "400",
        description = "Validation failed",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "AssignUserRolesValidationError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"ROLECODES_REQUIRED\",\n      \"message\": \"at least one roleCode is required\",\n      \"description\": \"Field 'roleCodes' must contain at least one value\"\n    }\n  ]\n}"))),
    @ApiResponse(
        responseCode = "409",
        description = "Role already assigned",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "AssignUserRolesConflictError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"CONFLICT\",\n      \"code\": \"CONFLICT\",\n      \"message\": \"Conflict\",\n      \"description\": \"User already has this role assigned\"\n    }\n  ]\n}"))),
    @ApiResponse(
        responseCode = "404",
        description = "User or role not found",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "AssignUserRolesNotFoundError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role not found for code=INVALID_ROLE\"\n    }\n  ]\n}"))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "AssignUserRolesInternalError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to assign roles\",\n      \"description\": \"Database unavailable while assigning roles to userId=1001\"\n    }\n  ]\n}")))
  })
  ResponseEntity<UserRolesAggregateResponse> assign(
      @Positive(message = "{validation.common.id.positive}") Long userId,
      @Valid UserRoleRequest request);

  @Operation(
      operationId = "replaceUserRoles",
      summary = "Replace user roles",
      description = "Replaces all active role assignments for a user with the provided roles.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "userId", description = "User identifier", required = true, example = "1001")
  })
  @RequestBody(
      required = true,
      description = "Role replacement payload",
      content =
          @Content(
              schema = @Schema(implementation = UserRoleRequest.class),
              examples =
                  @ExampleObject(
                      name = "ReplaceUserRolesRequestExample",
                      value = "{\n  \"roleCodes\": [\"SUPER_ADMIN\", \"AUDITOR\"]\n}")))
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User roles updated successfully",
        content =
            @Content(
                schema = @Schema(implementation = UserRolesAggregateResponse.class),
                examples =
                    @ExampleObject(
                        name = "ReplaceUserRolesSuccess",
                        value =
                            "{\n  \"userId\": 1001,\n  \"userEmailAddress\": \"admin1@snb.com\",\n  \"userMobileNumber\": \"971500000001\",\n  \"userType\": \"ADMIN\",\n  \"userAccountStatus\": \"ACTIVE\",\n  \"roles\": [\n    {\n      \"roleId\": 1003,\n      \"roleCode\": \"REGIONAL_MANAGER\",\n      \"roleName\": \"Regional Manager\",\n      \"roleDescription\": \"Manages regional operations and approvals\"\n    }\n  ]\n}"))),
    @ApiResponse(
        responseCode = "400",
        description = "Validation failed",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "ReplaceUserRolesValidationError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"ROLECODES_REQUIRED\",\n      \"message\": \"at least one roleCode is required\",\n      \"description\": \"Field 'roleCodes' must contain at least one value\"\n    }\n  ]\n}"))),
    @ApiResponse(
        responseCode = "404",
        description = "User or role not found",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "ReplaceUserRolesNotFoundError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role not found for code=INVALID_ROLE\"\n    }\n  ]\n}"))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "ReplaceUserRolesInternalError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to replace user roles\",\n      \"description\": \"Database unavailable while replacing roles for userId=1001\"\n    }\n  ]\n}")))
  })
  UserRolesAggregateResponse replace(
      @Positive(message = "{validation.common.id.positive}") Long userId,
      @Valid UserRoleRequest request);

  @Operation(
      operationId = "revokeUserRole",
      summary = "Revoke user-role assignment",
      description = "Soft-deletes a user-role assignment by userId and roleCode.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "userId", description = "User identifier", required = true, example = "1001"),
    @Parameter(
        name = "roleCode",
        description = "Role code",
        required = true,
        example = "SUPER_ADMIN")
  })
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Role revoked successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "Assignment not found",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "RevokeUserRoleNotFoundError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"User-role assignment not found for userId=1001 and roleCode=INVALID\"\n    }\n  ]\n}")))
  })
  ResponseEntity<Void> revoke(
      @Positive(message = "{validation.common.id.positive}") Long userId, String roleCode);
}
