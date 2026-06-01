package com.snb.ms.companysalesmaninvitation;

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
import org.springframework.http.ResponseEntity;

@Tag(name = "[030] - Company Salesman Invitations", description = "Operations for inviting salesmen to register under a company")
public interface CompanySalesmanInvitationApi {

    @Operation(
        operationId = "createCompanySalesmanInvitation",
        summary = "Create salesman invitation",
        description = "Creates a company-owned invitation record that allows the invited salesman to register later in the system."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "companyId", description = "Company identifier", required = true, example = "1001")
    })
    @RequestBody(
        required = true,
        description = "Salesman invitation payload to create",
        content = @Content(
            schema = @Schema(implementation = CompanySalesmanInvitationRequest.class),
            examples = @ExampleObject(
                name = "CreateCompanySalesmanInvitationRequestExample",
                value = "{\n  \"emailAddress\": \"salesman@example.com\",\n  \"mobileNumber\": \"+971555010201\",\n  \"idNumber\": \"784-1986-0000001-1\",\n  \"expiryDate\": \"2026-06-08T10:15:30\"\n}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Invitation created successfully",
            content = @Content(schema = @Schema(implementation = CompanySalesmanInvitationResponse.class))
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
            description = "Company not found",
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
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Internal server error\",\n      \"description\": \"Failed to create invitation for company id=1001\"\n    }\n  ]\n}"
                )
            )
        )
    })
    ResponseEntity<CompanySalesmanInvitationResponse> create(
        @Positive(message = "{validation.common.id.positive}") Long companyId,
        @Valid CompanySalesmanInvitationRequest request
    );
}