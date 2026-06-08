package com.snb.ms.userdevice;

import com.snb.ms.shared.BaseResponseDTO;
import com.snb.ms.shared.api.CommonApiParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "[033] - User Devices", description = "Operations for user device resources")
public interface UserDeviceApi {

  @Operation(
      operationId = "getAllUserDevices",
      summary = "List user devices",
      description = "Returns active user devices using offset-based pagination.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "page", description = "Zero-based page index", example = "0"),
    @Parameter(name = "size", description = "Page size", example = "20"),
    @Parameter(name = "sortBy", description = "Sort fields", example = "createdAt"),
    @Parameter(name = "sortDirection", description = "Sort direction", example = "DESC")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User devices fetched successfully",
        content = @Content(schema = @Schema(implementation = UserDevicePageResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  UserDevicePageResponse findAll(@Valid @ParameterObject UserDeviceListQuery query);

  @Operation(
      operationId = "getUserDeviceById",
      summary = "Get user device by id",
      description = "Finds a single user device by identifier.")
  @CommonApiParameters
  @Parameters({
    @Parameter(
        name = "id",
        description = "User device identifier",
        required = true,
        example = "1001")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User device fetched successfully",
        content = @Content(schema = @Schema(implementation = UserDeviceResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "User device not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  UserDeviceResponse findById(@Positive(message = "{validation.common.id.positive}") Long id);

  @Operation(
      operationId = "createUserDevice",
      summary = "Create user device",
      description = "Creates a new user device record.")
  @CommonApiParameters
  @RequestBody(
      required = true,
      description = "User device payload to create",
      content = @Content(schema = @Schema(implementation = UserDeviceCreateRequest.class)))
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "User device created successfully",
        content = @Content(schema = @Schema(implementation = UserDeviceResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Validation failed",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  ResponseEntity<UserDeviceResponse> create(@Valid UserDeviceCreateRequest request);

  @Operation(
      operationId = "updateUserDevice",
      summary = "Update user device",
      description = "Updates an existing user device record by identifier.")
  @CommonApiParameters
  @Parameters({
    @Parameter(
        name = "id",
        description = "User device identifier",
        required = true,
        example = "1001")
  })
  @RequestBody(
      required = true,
      description = "User device payload to update",
      content = @Content(schema = @Schema(implementation = UserDeviceUpdateRequest.class)))
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User device updated successfully",
        content = @Content(schema = @Schema(implementation = UserDeviceResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "User device not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  UserDeviceResponse update(
      @Positive(message = "{validation.common.id.positive}") Long id,
      @Valid UserDeviceUpdateRequest request);

  @Operation(
      operationId = "softDeleteUserDevice",
      summary = "Soft delete user device",
      description = "Marks a user device as deleted.")
  @CommonApiParameters
  @Parameters({
    @Parameter(
        name = "id",
        description = "User device identifier",
        required = true,
        example = "1001")
  })
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "User device soft-deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "User device not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  ResponseEntity<Void> softDelete(@Positive(message = "{validation.common.id.positive}") Long id);
}
