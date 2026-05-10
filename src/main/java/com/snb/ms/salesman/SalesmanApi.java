package com.snb.ms.salesman;

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

@Tag(name = "Salesmen", description = "Operations for salesman resources")
public interface SalesmanApi {

    @Operation(
        operationId = "getAllSalesmen",
        summary = "List salesmen",
        description = "Returns all active salesman records."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Salesmen fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SalesmanResponse.class)))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    List<SalesmanResponse> findAll();

    @Operation(
        operationId = "getSalesmanById",
        summary = "Get salesman by id",
        description = "Finds a salesman by identifier."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader"),
        @Parameter(name = "id", description = "Salesman identifier", required = true, example = "1")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Salesman fetched successfully",
            content = @Content(schema = @Schema(implementation = SalesmanResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid id supplied",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Salesman not found"),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    SalesmanResponse findById(@Positive(message = "{validation.common.id.positive}") Long id);

    @Operation(
        operationId = "createSalesman",
        summary = "Create salesman",
        description = "Creates a salesman and links the record with a company and internal user profile."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader")
    })
    @RequestBody(
        required = true,
        description = "Salesman payload to create",
        content = @Content(
            schema = @Schema(implementation = SalesmanCreateRequest.class),
            examples = @ExampleObject(
                name = "CreateSalesman",
                value = "{\n  \"firstName\": \"Ahamed\",\n  \"middleName\": \"I\",\n  \"lastName\": \"Khan\",\n  \"accountNumber\": \"ACC-9911\",\n  \"cifNumber\": \"CIF-1022\",\n  \"idNumber\": \"784-1986-0000001-1\",\n  \"companyId\": 1,\n  \"emailAddress\": \"ahamed.khan@example.com\",\n  \"mobileNumber\": \"+971555010201\"\n}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Salesman created successfully",
            content = @Content(schema = @Schema(implementation = SalesmanResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Company reference not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    ResponseEntity<SalesmanResponse> create(@Valid SalesmanCreateRequest request);

    @Operation(
        operationId = "updateSalesman",
        summary = "Update salesman",
        description = "Updates mutable salesman fields by identifier."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader"),
        @Parameter(name = "id", description = "Salesman identifier", required = true, example = "1")
    })
    @RequestBody(
        required = true,
        description = "Salesman payload to update",
        content = @Content(
            schema = @Schema(implementation = SalesmanUpdateRequest.class),
            examples = @ExampleObject(
                name = "UpdateSalesman",
                value = "{\n  \"firstName\": \"Ahamed\",\n  \"middleName\": \"Ibrahim\",\n  \"lastName\": \"Khan\",\n  \"accountNumber\": \"ACC-9911\",\n  \"cifNumber\": \"CIF-1022\",\n  \"idNumber\": \"784-1986-0000001-1\"\n}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Salesman updated successfully",
            content = @Content(schema = @Schema(implementation = SalesmanResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation failed or invalid id",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Salesman not found"),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    SalesmanResponse update(@Positive(message = "{validation.common.id.positive}") Long id,
                           @Valid SalesmanUpdateRequest request);

    @Operation(
        operationId = "softDeleteSalesman",
        summary = "Soft delete salesman",
        description = "Marks a salesman as inactive without removing the row."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader"),
        @Parameter(name = "id", description = "Salesman identifier", required = true, example = "1")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Salesman soft-deleted successfully",
            content = @Content(schema = @Schema(implementation = SalesmanResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid id supplied",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Salesman not found"),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    SalesmanResponse softDelete(@Positive(message = "{validation.common.id.positive}") Long id);
}
