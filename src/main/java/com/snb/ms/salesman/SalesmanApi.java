package com.snb.ms.salesman;

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

@Tag(name = "Salesmen", description = "Operations for salesman resources")
public interface SalesmanApi {

    @Operation(
        operationId = "getAllSalesmen",
        summary = "List salesmen",
        description = "Returns all active salesman records."
    )
    @CommonApiParameters
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Salesmen fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SalesmanResponse.class)))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "ListSalesmenInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch salesmen\",\n      \"description\": \"Database unavailable while listing salesmen\"\n    }\n  ]\n}"
                )
            )
        )
    })
    List<SalesmanResponse> findAll();

    @Operation(
        operationId = "getSalesmanById",
        summary = "Get salesman by id",
        description = "Finds a salesman by identifier."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "id", description = "Salesman identifier", required = true, example = "1001")
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
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "GetSalesmanByIdValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"INVALID_PATH_PARAM\",\n      \"message\": \"id must be greater than 0\",\n      \"description\": \"Path variable 'id' must be positive\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Salesman not found",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "GetSalesmanByIdNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Salesman not found for id=1999\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "GetSalesmanInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch salesman\",\n      \"description\": \"Database unavailable while loading salesman id=1001\"\n    }\n  ]\n}"
                )
            )
        )
    })
    SalesmanResponse findById(@Positive(message = "{validation.common.id.positive}") Long id);

    @Operation(
        operationId = "createSalesman",
        summary = "Create salesman",
        description = "Creates a salesman and links the record with a company and internal user profile."
    )
    @CommonApiParameters
    @RequestBody(
        required = true,
        description = "Salesman payload to create",
        content = @Content(
            schema = @Schema(implementation = SalesmanCreateRequest.class),
            examples = @ExampleObject(
                name = "CreateSalesmanRequestExample",
                value = "{\n  \"firstName\": \"Ahamed\",\n  \"middleName\": \"I\",\n  \"lastName\": \"Khan\",\n  \"accountNumber\": \"ACC-9911\",\n  \"cifNumber\": \"CIF-1022\",\n  \"idNumber\": \"784-1986-0000001-1\",\n  \"companyId\": 1001,\n  \"emailAddress\": \"ahamed.khan@example.com\",\n  \"mobileNumber\": \"+971555010201\"\n}"
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
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "CreateSalesmanValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"FIRSTNAME_REQUIRED\",\n      \"message\": \"firstName is required\",\n      \"description\": \"Field 'firstName' must not be blank\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Company reference not found",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "CreateSalesmanCompanyNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Company not found for id=1999\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "CreateSalesmanInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to create salesman\",\n      \"description\": \"Database transaction failed while creating salesman ACC-9911\"\n    }\n  ]\n}"
                )
            )
        )
    })
    ResponseEntity<SalesmanResponse> create(@Valid SalesmanCreateRequest request);

    @Operation(
        operationId = "updateSalesman",
        summary = "Update salesman",
        description = "Updates mutable salesman fields by identifier."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "id", description = "Salesman identifier", required = true, example = "1001")
    })
    @RequestBody(
        required = true,
        description = "Salesman payload to update",
        content = @Content(
            schema = @Schema(implementation = SalesmanUpdateRequest.class),
            examples = @ExampleObject(
                name = "UpdateSalesmanRequestExample",
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
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "UpdateSalesmanValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"IDNUMBER_REQUIRED\",\n      \"message\": \"idNumber is required\",\n      \"description\": \"Field 'idNumber' must not be blank\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Salesman not found",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "UpdateSalesmanNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Salesman not found for id=1999\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "UpdateSalesmanInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to update salesman\",\n      \"description\": \"Database unavailable while updating salesman id=1001\"\n    }\n  ]\n}"
                )
            )
        )
    })
    SalesmanResponse update(@Positive(message = "{validation.common.id.positive}") Long id,
                           @Valid SalesmanUpdateRequest request);

    @Operation(
        operationId = "softDeleteSalesman",
        summary = "Soft delete salesman",
        description = "Marks a salesman as inactive without removing the row."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "id", description = "Salesman identifier", required = true, example = "1001")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Salesman soft-deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Salesman not found",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "SoftDeleteSalesmanNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Salesman not found for id=1999\"\n    }\n  ]\n}"
                )
            )
        )
    })
    ResponseEntity<Void> softDelete(@Positive(message = "{validation.common.id.positive}") Long id);
}
