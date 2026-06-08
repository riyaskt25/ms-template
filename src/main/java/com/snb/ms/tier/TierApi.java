package com.snb.ms.tier;

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

@Tag(name = "[034] - Tiers", description = "Operations for tier resources")
public interface TierApi {

  @Operation(
      operationId = "getAllTiers",
      summary = "List tiers",
      description = "Returns active tiers using offset-based pagination.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "page", description = "Zero-based page index", example = "0"),
    @Parameter(name = "size", description = "Page size", example = "20"),
    @Parameter(name = "sortBy", description = "Sort fields", example = "displayOrder"),
    @Parameter(name = "sortDirection", description = "Sort direction", example = "ASC")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Tiers fetched successfully",
        content = @Content(schema = @Schema(implementation = TierPageResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  TierPageResponse findAll(@Valid @ParameterObject TierListQuery query);

  @Operation(
      operationId = "getTierById",
      summary = "Get tier by id",
      description = "Finds a single tier by identifier.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "id", description = "Tier identifier", required = true, example = "1001")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Tier fetched successfully",
        content = @Content(schema = @Schema(implementation = TierResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Tier not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  TierResponse findById(@Positive(message = "{validation.common.id.positive}") Long id);

  @Operation(
      operationId = "createTier",
      summary = "Create tier",
      description = "Creates a new tier record.")
  @CommonApiParameters
  @RequestBody(
      required = true,
      description = "Tier payload to create",
      content = @Content(schema = @Schema(implementation = TierCreateRequest.class)))
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Tier created successfully",
        content = @Content(schema = @Schema(implementation = TierResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Validation failed",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  ResponseEntity<TierResponse> create(@Valid TierCreateRequest request);

  @Operation(
      operationId = "updateTier",
      summary = "Update tier",
      description = "Updates an existing tier record by identifier.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "id", description = "Tier identifier", required = true, example = "1001")
  })
  @RequestBody(
      required = true,
      description = "Tier payload to update",
      content = @Content(schema = @Schema(implementation = TierUpdateRequest.class)))
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Tier updated successfully",
        content = @Content(schema = @Schema(implementation = TierResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Tier not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  TierResponse update(
      @Positive(message = "{validation.common.id.positive}") Long id,
      @Valid TierUpdateRequest request);

  @Operation(
      operationId = "softDeleteTier",
      summary = "Soft delete tier",
      description = "Marks a tier as deleted.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "id", description = "Tier identifier", required = true, example = "1001")
  })
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Tier soft-deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "Tier not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  ResponseEntity<Void> softDelete(@Positive(message = "{validation.common.id.positive}") Long id);
}
