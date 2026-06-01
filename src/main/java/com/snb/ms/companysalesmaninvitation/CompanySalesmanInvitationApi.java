// File: src/main/java/com/snb/ms/companysalesmaninvitation/CompanySalesmanInvitationApi.java
package com.snb.ms.companysalesmaninvitation;

import com.snb.ms.shared.BaseResponseDTO;
import com.snb.ms.shared.api.CommonApiParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "[009] - Company Salesman Invitations", description = "Operations for inviting salesmen to register under a company")
public interface CompanySalesmanInvitationApi {

    @Operation(
        operationId = "createCompanySalesmanInvitation",
        summary = "Create salesman invitation",
        description = "Creates invitation records for one or more companies and allows the invited salesman to register later in the system."
    )
    @CommonApiParameters
    @RequestBody(
        required = true,
        description = "Salesman invitation payload to create",
        content = @Content(
            schema = @Schema(implementation = CompanySalesmanInvitationRequest.class),
            examples = @ExampleObject(
                name = "CreateCompanySalesmanInvitationRequestExample",
                value = "{\n  \"emailAddress\": \"salesman@example.com\",\n  \"mobileNumber\": \"+971555010201\",\n  \"idNumber\": \"784-1986-0000001-1\",\n  \"companyIds\": [1001, 1002],\n  \"expiryDate\": \"2026-06-08T10:15:30\"\n}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Invitation created successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CompanySalesmanInvitationResponse.class)))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation failed or an open invitation already exists for the same invitee details",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "CreateCompanySalesmanInvitationValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"EMAILADDRESS_INVALID\",\n      \"message\": \"emailAddress must be a valid email address\",\n      \"description\": \"Rejected value: salesman.invalid\"\n    }\n  ]\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "One or more companies not found",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "CreateCompanySalesmanInvitationCompanyNotFoundError",
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
                    name = "CreateCompanySalesmanInvitationInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Internal server error\",\n      \"description\": \"Failed to create invitation for company ids=[1001,1002]\"\n    }\n  ]\n}"
                )
            )
        )
    })
    ResponseEntity<List<CompanySalesmanInvitationResponse>> create(@Valid CompanySalesmanInvitationRequest request);
}