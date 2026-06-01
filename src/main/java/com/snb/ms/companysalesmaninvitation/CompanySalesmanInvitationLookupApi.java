package com.snb.ms.companysalesmaninvitation;

import com.snb.ms.shared.BaseResponseDTO;
import com.snb.ms.shared.api.CommonApiParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Tag(name = "[009] - Company Salesman Invitations", description = "Operations for inviting salesmen to register under a company")
public interface CompanySalesmanInvitationLookupApi {

    @Operation(
        operationId = "listCompanySalesmanInvitationsByEmail",
        summary = "List invitations by email",
        description = "Returns all company invitation records that match the supplied email address, including the related company details."
    )
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "email", description = "Invitee email address", required = true, example = "salesman@example.com")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Invitations fetched successfully",
            content = @Content(schema = @Schema(implementation = CompanySalesmanInvitationListResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation failed",
            content = @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(
                    name = "ListCompanySalesmanInvitationsValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"EMAIL_INVALID\",\n      \"message\": \"email must be a valid email address\",\n      \"description\": \"Rejected value: salesman.invalid\"\n    }\n  ]\n}"
                )
            )
        )
    })
    List<CompanySalesmanInvitationListResponse> listByEmail(
        @NotBlank(message = "{validation.companySalesmanInvitation.lookupEmail.required}")
        @Email(message = "{validation.companySalesmanInvitation.lookupEmail.email}")
        String email
    );
}