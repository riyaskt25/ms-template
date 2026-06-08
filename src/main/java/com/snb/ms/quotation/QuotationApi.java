package com.snb.ms.quotation;

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

@Tag(name = "[032] - Quotations", description = "Operations for quotation resources")
public interface QuotationApi {

  @Operation(
      operationId = "getAllQuotations",
      summary = "List quotations",
      description = "Returns active quotations using offset-based pagination.")
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
        description = "Quotations fetched successfully",
        content = @Content(schema = @Schema(implementation = QuotationPageResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  QuotationPageResponse findAll(@Valid @ParameterObject QuotationListQuery query);

  @Operation(
      operationId = "getQuotationById",
      summary = "Get quotation by id",
      description = "Finds a single quotation by identifier.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "id", description = "Quotation identifier", required = true, example = "1001")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Quotation fetched successfully",
        content = @Content(schema = @Schema(implementation = QuotationResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Quotation not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  QuotationResponse findById(@Positive(message = "{validation.common.id.positive}") Long id);

  @Operation(
      operationId = "createQuotation",
      summary = "Create quotation",
      description = "Creates a new quotation record.")
  @CommonApiParameters
  @RequestBody(
      required = true,
      description = "Quotation payload to create",
      content = @Content(schema = @Schema(implementation = QuotationCreateRequest.class)))
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Quotation created successfully",
        content = @Content(schema = @Schema(implementation = QuotationResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Validation failed",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  ResponseEntity<QuotationResponse> create(@Valid QuotationCreateRequest request);

  @Operation(
      operationId = "updateQuotation",
      summary = "Update quotation",
      description = "Updates an existing quotation record by identifier.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "id", description = "Quotation identifier", required = true, example = "1001")
  })
  @RequestBody(
      required = true,
      description = "Quotation payload to update",
      content = @Content(schema = @Schema(implementation = QuotationUpdateRequest.class)))
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Quotation updated successfully",
        content = @Content(schema = @Schema(implementation = QuotationResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Quotation not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  QuotationResponse update(
      @Positive(message = "{validation.common.id.positive}") Long id,
      @Valid QuotationUpdateRequest request);

  @Operation(
      operationId = "softDeleteQuotation",
      summary = "Soft delete quotation",
      description = "Marks a quotation as deleted.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "id", description = "Quotation identifier", required = true, example = "1001")
  })
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Quotation soft-deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "Quotation not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  ResponseEntity<Void> softDelete(@Positive(message = "{validation.common.id.positive}") Long id);
}
