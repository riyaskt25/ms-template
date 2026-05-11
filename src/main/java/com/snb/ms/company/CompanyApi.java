package com.snb.ms.company;

import com.snb.ms.shared.BaseResponseDTO;
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

@Tag(name = "Companies", description = "Operations for company resources")
public interface CompanyApi {

    @Operation(
        operationId = "getAllCompanies",
        summary = "List companies",
        description = "Returns all active company records managed by this service."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Companies fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CompanyResponse.class)))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    List<CompanyResponse> findAll();

    @Operation(
        operationId = "getCompanyById",
        summary = "Get company by id",
        description = "Finds a single company by its identifier."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader"),
        @Parameter(name = "id", description = "Company identifier", required = true, example = "1")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Company fetched successfully",
            content = @Content(schema = @Schema(implementation = CompanyResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid company id supplied",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Company not found",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "CompanyNotFound",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Company not found for id=999\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "InternalServerError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Unexpected internal server error\",\n      \"description\": null\n    }\n  ]\n}"
                )
            )
        )
    })
    CompanyResponse findById(@Positive(message = "{validation.common.id.positive}") Long id);

    @Operation(
        operationId = "createCompany",
        summary = "Create company",
        description = "Creates a new company record and provisions the linked internal user."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader")
    })
    @RequestBody(
        required = true,
        description = "Company payload to create",
        content = @Content(
            schema = @Schema(implementation = CompanyCreateRequest.class),
            examples = @ExampleObject(
                name = "CreateCompany",
                value = "{\n  \"registrationNumber\": \"REG-2026-0001\",\n  \"emailAddress\": \"company@example.com\",\n  \"mobileNumber\": \"+971555010101\"\n}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Company created successfully",
            content = @Content(schema = @Schema(implementation = CompanyResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Duplicate registration number or conflicting data",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    ResponseEntity<CompanyResponse> create(@Valid CompanyCreateRequest request);

    @Operation(
        operationId = "updateCompany",
        summary = "Update company",
        description = "Updates an existing company record by identifier."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader"),
        @Parameter(name = "id", description = "Company identifier", required = true, example = "1")
    })
    @RequestBody(
        required = true,
        description = "Company payload to update",
        content = @Content(
            schema = @Schema(implementation = CompanyUpdateRequest.class),
            examples = @ExampleObject(
                name = "UpdateCompany",
                value = "{\n  \"registrationNumber\": \"REG-2026-0001\",\n  \"emailAddress\": \"company.updated@example.com\",\n  \"mobileNumber\": \"+971555010102\"\n}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Company updated successfully",
            content = @Content(schema = @Schema(implementation = CompanyResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation failed or invalid id",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Company not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    CompanyResponse update(@Positive(message = "{validation.common.id.positive}") Long id,
                          @Valid CompanyUpdateRequest request);

    @Operation(
        operationId = "softDeleteCompany",
        summary = "Soft delete company",
        description = "Marks a company record as inactive without physical deletion."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader"),
        @Parameter(name = "id", description = "Company identifier", required = true, example = "1")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Company soft-deleted successfully",
            content = @Content(schema = @Schema(implementation = CompanyResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid company id supplied",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Company not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    CompanyResponse softDelete(@Positive(message = "{validation.common.id.positive}") Long id);
}
