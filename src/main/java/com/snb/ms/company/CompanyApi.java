package com.snb.ms.company;

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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "Companies", description = "Operations for company resources")
public interface CompanyApi {

    @Operation(
        operationId = "getAllCompanies",
        summary = "List companies (offset pagination)",
        description = "Returns active company records using offset-based pagination. Best for small to medium datasets or when random access to pages is needed."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "page", description = "Zero-based page index", example = "0"),
        @Parameter(name = "size", description = "Page size (max 200)", example = "20"),
        @Parameter(name = "sortBy", description = "Sort fields (comma-separated). Defaults to companyId ASC when omitted. Allowed fields: companyId, registrationNumber, companyStatus, companyType, emailAddress, mobileNumber, createdAt, updatedAt.", example = "companyStatus,registrationNumber"),
        @Parameter(name = "sortDirection", description = "Sort directions. Provide a single value (e.g. DESC) to apply to all sortBy fields, or comma-separated values matching the sortBy count. Allowed values: ASC, DESC. Defaults to ASC when omitted.", example = "ASC,DESC"),
        @Parameter(name = "includeSalesmen", description = "Set true to include associated salesmen in each company. Defaults to false.", example = "false"),
        @Parameter(name = "registrationNumber", description = "Optional registration number contains filter", example = "REG-2026"),
        @Parameter(name = "companyStatus", description = "Optional company status contains filter", example = "ACTIVE"),
        @Parameter(name = "companyType", description = "Optional company type contains filter", example = "DEALER"),
        @Parameter(name = "emailAddress", description = "Optional email contains filter", example = "company@example.com"),
        @Parameter(name = "mobileNumber", description = "Optional mobile contains filter", example = "+971555")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Companies fetched successfully",
            content = @Content(schema = @Schema(implementation = CompanyPageResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    CompanyPageResponse findAll(@Valid @ParameterObject CompanyListQuery query);

    @Operation(
        operationId = "getAllCompaniesCursor",
        summary = "List companies (lazy loading with cursor)",
        description = "Returns active company records using cursor-based lazy loading. Recommended for large datasets or continuous scrolling. Provides efficient, deterministic pagination."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "cursor", description = "Opaque cursor token from previous response's lazyLoading.nextCursor. Omit for first request.", example = "eyJjb21wYW55SWQiOiIxMjAifQ"),
        @Parameter(name = "limit", description = "Lazy-loading batch size (max 200)", example = "20"),
        @Parameter(name = "sortBy", description = "Sort fields (comma-separated). Defaults to companyId ASC when omitted. companyId is always appended as tie-breaker when missing. Allowed fields: companyId, registrationNumber, companyStatus, companyType, emailAddress, mobileNumber, createdAt, updatedAt.", example = "companyStatus,registrationNumber"),
        @Parameter(name = "sortDirection", description = "Sort directions. Provide a single value (e.g. DESC) to apply to all sortBy fields, or comma-separated values matching the sortBy count. Allowed values: ASC, DESC. Defaults to ASC when omitted.", example = "ASC,DESC"),
        @Parameter(name = "includeSalesmen", description = "Set true to include associated salesmen in each company. Defaults to false.", example = "false"),
        @Parameter(name = "registrationNumber", description = "Optional registration number contains filter", example = "REG-2026"),
        @Parameter(name = "companyStatus", description = "Optional company status contains filter", example = "ACTIVE"),
        @Parameter(name = "companyType", description = "Optional company type contains filter", example = "DEALER"),
        @Parameter(name = "emailAddress", description = "Optional email contains filter", example = "company@example.com"),
        @Parameter(name = "mobileNumber", description = "Optional mobile contains filter", example = "+971555")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Companies fetched successfully",
            content = @Content(schema = @Schema(implementation = CompanyPageResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid cursor token or parameters",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    CompanyPageResponse findAllLazy(@Valid @ParameterObject CompanyListQuery query);

    @Operation(
        operationId = "getCompanyById",
        summary = "Get company by id",
        description = "Finds a single company by its identifier."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "id", description = "Company identifier", required = true, example = "1"),
        @Parameter(name = "includeSalesmen", description = "Set true to include associated salesmen for this company. Defaults to false.", example = "false")
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
    CompanyResponse findById(@Positive(message = "{validation.common.id.positive}") Long id,
                             Boolean includeSalesmen);

    @Operation(
        operationId = "createCompany",
        summary = "Create company",
        description = "Creates a new company record and provisions the linked internal user."
    )
    @CommonApiParameters
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
            content = @Content(schema = @Schema(implementation = CompanyWriteResponse.class))
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
    ResponseEntity<CompanyWriteResponse> create(@Valid CompanyCreateRequest request);

    @Operation(
        operationId = "updateCompany",
        summary = "Update company",
        description = "Updates an existing company record by identifier."
    )
    @CommonApiParameters
    @Parameters({
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
            content = @Content(schema = @Schema(implementation = CompanyWriteResponse.class))
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
    CompanyWriteResponse update(@Positive(message = "{validation.common.id.positive}") Long id,
                                @Valid CompanyUpdateRequest request);

    @Operation(
        operationId = "softDeleteCompany",
        summary = "Soft delete company",
        description = "Marks a company record as inactive without physical deletion."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "id", description = "Company identifier", required = true, example = "1")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Company soft-deleted successfully",
            content = @Content(schema = @Schema(implementation = CompanyWriteResponse.class))
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
    CompanyWriteResponse softDelete(@Positive(message = "{validation.common.id.positive}") Long id);

    @Operation(
        operationId = "decideCompanyStatus",
        summary = "Decide company status",
        description = "Allows administrator to decide a pending company as ACTIVE or REJECTED."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "id", description = "Company identifier", required = true, example = "1")
    })
    @RequestBody(
        required = true,
        description = "Status decision payload",
        content = @Content(
            schema = @Schema(implementation = CompanyStatusDecisionRequest.class),
            examples = @ExampleObject(
                name = "DecideCompanyStatus",
                value = "{\n  \"status\": \"ACTIVE\"\n}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Company status updated successfully",
            content = @Content(schema = @Schema(implementation = CompanyWriteResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid status value or transition",
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
    CompanyWriteResponse decideStatus(@Positive(message = "{validation.common.id.positive}") Long id,
                                      @Valid CompanyStatusDecisionRequest request);
}
