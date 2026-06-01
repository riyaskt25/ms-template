package com.snb.ms.adminuser;

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

@Tag(name = "[009] - Admin Users", description = "Operations for admin user resources")
public interface AdminUserApi {

    @Operation(
        operationId = "getAllAdminUsers",
        summary = "List admin users",
        description = "Returns all active admin user records."
    )
    @CommonApiParameters
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Admin users fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AdminUserResponse.class)))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "ListAdminUsersInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch admin users\",\n      \"description\": \"Database unavailable while listing admin users\"\n    }\n  ]\n}"
                )
            )
        )
    })
    List<AdminUserResponse> findAll();

    @Operation(
        operationId = "getAdminUserByUserId",
        summary = "Get admin user by userId",
        description = "Finds an admin user by user identifier."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "userId", description = "User identifier", required = true, example = "1001")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Admin user fetched successfully",
            content = @Content(schema = @Schema(implementation = AdminUserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid userId supplied",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "GetAdminUserByUserIdValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"INVALID_PATH_PARAM\",\n      \"message\": \"userId must be greater than 0\",\n      \"description\": \"Path variable 'userId' must be positive\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Admin user not found",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "GetAdminUserByUserIdNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Admin user not found for userId=1999\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "GetAdminUserByUserIdInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch admin user\",\n      \"description\": \"Database unavailable while loading admin user userId=1001\"\n    }\n  ]\n}"
                )
            )
        )
    })
    AdminUserResponse findById(@Positive(message = "{validation.common.id.positive}") Long userId);

    @Operation(
        operationId = "createAdminUser",
        summary = "Create admin user",
        description = "Creates an admin user record and provisions the linked internal user profile."
    )
    @CommonApiParameters
    @RequestBody(
        required = true,
        description = "Admin user payload to create",
        content = @Content(
            schema = @Schema(implementation = AdminUserCreateRequest.class),
            examples = @ExampleObject(
                name = "CreateAdminUserRequestExample",
                value = "{\n  \"firstName\": \"Sara\",\n  \"middleName\": \"M\",\n  \"lastName\": \"Naseer\",\n  \"extensionNumber\": \"EXT-1001\",\n  \"snbId\": \"SNB1001\",\n  \"emailAddress\": \"sara.naseer@example.com\",\n  \"mobileNumber\": \"+971555010301\"\n}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Admin user created successfully",
            content = @Content(schema = @Schema(implementation = AdminUserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation failed",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "CreateAdminUserValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"EMAILADDRESS_INVALID\",\n      \"message\": \"emailAddress must be a valid email\",\n      \"description\": \"Rejected value: invalid-email\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Admin user not found",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "CreateAdminUserNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Admin user not found for id=1999\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "CreateAdminUserInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to create admin user\",\n      \"description\": \"Database transaction failed while creating admin user sara.naseer@example.com\"\n    }\n  ]\n}"
                )
            )
        )
    })
    ResponseEntity<AdminUserResponse> create(@Valid AdminUserCreateRequest request);

    @Operation(
        operationId = "updateAdminUser",
        summary = "Update admin user",
        description = "Updates an existing admin user by user identifier."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "userId", description = "User identifier", required = true, example = "1001")
    })
    @RequestBody(
        required = true,
        description = "Admin user payload to update",
        content = @Content(
            schema = @Schema(implementation = AdminUserUpdateRequest.class),
            examples = @ExampleObject(
                name = "UpdateAdminUserRequestExample",
                value = "{\n  \"firstName\": \"Sara\",\n  \"middleName\": \"Mariam\",\n  \"lastName\": \"Naseer\",\n  \"extensionNumber\": \"EXT-1002\",\n  \"snbId\": \"SNB1002\",\n  \"emailAddress\": \"sara.naseer@example.com\",\n  \"mobileNumber\": \"+971555010302\"\n}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Admin user updated successfully",
            content = @Content(schema = @Schema(implementation = AdminUserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation failed or invalid userId",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "UpdateAdminUserValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"MOBILENUMBER_REQUIRED\",\n      \"message\": \"mobileNumber is required\",\n      \"description\": \"Field 'mobileNumber' must not be blank\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Admin user not found",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "UpdateAdminUserByUserIdNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Admin user not found for userId=1999\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "UpdateAdminUserByUserIdInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to update admin user\",\n      \"description\": \"Database unavailable while updating admin user userId=1001\"\n    }\n  ]\n}"
                )
            )
        )
    })
    AdminUserResponse update(@Positive(message = "{validation.common.id.positive}") Long userId,
                            @Valid AdminUserUpdateRequest request);

    @Operation(
        operationId = "softDeleteAdminUser",
        summary = "Soft delete admin user",
        description = "Marks an admin user as inactive by user identifier."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "userId", description = "User identifier", required = true, example = "1001")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Admin user soft-deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Admin user not found",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "SoftDeleteAdminUserByUserIdNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Admin user not found for userId=1999\"\n    }\n  ]\n}"
                )
            )
        )
    })
    ResponseEntity<Void> softDelete(@Positive(message = "{validation.common.id.positive}") Long userId);
}
