package com.snb.ms.adminuser;

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

@Tag(name = "Admin Users", description = "Operations for admin user resources")
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
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    List<AdminUserResponse> findAll();

    @Operation(
        operationId = "getAdminUserById",
        summary = "Get admin user by id",
        description = "Finds an admin user by identifier."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "id", description = "Admin user identifier", required = true, example = "1")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Admin user fetched successfully",
            content = @Content(schema = @Schema(implementation = AdminUserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid id supplied",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Admin user not found"),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    AdminUserResponse findById(@Positive(message = "{validation.common.id.positive}") Long id);

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
                name = "CreateAdminUser",
                value = "{\n  \"firstName\": \"Sara\",\n  \"middleName\": \"M\",\n  \"lastName\": \"Naseer\",\n  \"extensionNumber\": \"EXT-1001\",\n  \"emailAddress\": \"sara.naseer@example.com\",\n  \"mobileNumber\": \"+971555010301\"\n}"
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
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    ResponseEntity<AdminUserResponse> create(@Valid AdminUserCreateRequest request);

    @Operation(
        operationId = "updateAdminUser",
        summary = "Update admin user",
        description = "Updates an existing admin user by identifier."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "id", description = "Admin user identifier", required = true, example = "1")
    })
    @RequestBody(
        required = true,
        description = "Admin user payload to update",
        content = @Content(
            schema = @Schema(implementation = AdminUserUpdateRequest.class),
            examples = @ExampleObject(
                name = "UpdateAdminUser",
                value = "{\n  \"firstName\": \"Sara\",\n  \"middleName\": \"Mariam\",\n  \"lastName\": \"Naseer\",\n  \"extensionNumber\": \"EXT-1002\",\n  \"emailAddress\": \"sara.naseer@example.com\",\n  \"mobileNumber\": \"+971555010302\"\n}"
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
            description = "Validation failed or invalid id",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Admin user not found"),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    AdminUserResponse update(@Positive(message = "{validation.common.id.positive}") Long id,
                            @Valid AdminUserUpdateRequest request);

    @Operation(
        operationId = "softDeleteAdminUser",
        summary = "Soft delete admin user",
        description = "Marks an admin user as inactive without deleting the record physically."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "id", description = "Admin user identifier", required = true, example = "1")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Admin user soft-deleted successfully",
            content = @Content(schema = @Schema(implementation = AdminUserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid id supplied",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Admin user not found"),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    AdminUserResponse softDelete(@Positive(message = "{validation.common.id.positive}") Long id);
}
