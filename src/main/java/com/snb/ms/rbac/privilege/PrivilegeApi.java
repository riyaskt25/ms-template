package com.snb.ms.rbac.privilege;

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
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Privileges", description = "RBAC privilege management operations")
public interface PrivilegeApi {

    @Operation(operationId = "getAllPrivileges", summary = "List privileges", description = "Returns all active privilege records.")
    @CommonApiParameters
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Privileges fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PrivilegeResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GetAllPrivilegesInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch privileges\",\n      \"description\": \"Database unavailable while listing privileges\"\n    }\n  ]\n}")))
    })
    List<PrivilegeResponse> findAll();

    @Operation(operationId = "getPrivilegeByCode", summary = "Get privilege by code", description = "Finds a privilege by code.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "privilegeCode", description = "Privilege code", required = true, example = "USER_VIEW")})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Privilege fetched successfully",
            content = @Content(schema = @Schema(implementation = PrivilegeResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid privilege code supplied",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GetPrivilegeByCodeValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"INVALID_PRIVILEGE_CODE\",\n      \"message\": \"privilegeCode is invalid\",\n      \"description\": \"Path variable 'privilegeCode' must be non-empty alphanumeric\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "404", description = "Privilege not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GetPrivilegeByCodeNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Privilege not found for code=INVALID\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GetPrivilegeByCodeInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch privilege\",\n      \"description\": \"Database unavailable while loading privilege for code=USER_VIEW\"\n    }\n  ]\n}")))
    })
    PrivilegeResponse findByCode(String privilegeCode);

    @Operation(operationId = "createPrivilege", summary = "Create privilege", description = "Creates a new privilege record.")
    @CommonApiParameters
    @RequestBody(required = true, description = "Privilege payload to create",
        content = @Content(schema = @Schema(implementation = PrivilegeCreateRequest.class),
            examples = @ExampleObject(name = "CreatePrivilegeRequestExample",
                value = "{\n  \"privilegeCode\": \"USER_VIEW\",\n  \"privilegeName\": \"View Users\",\n  \"description\": \"Can view user profiles\"\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Privilege created successfully",
            content = @Content(schema = @Schema(implementation = PrivilegeResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "CreatePrivilegeValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"PRIVILEGECODE_REQUIRED\",\n      \"message\": \"privilegeCode is required\",\n      \"description\": \"Field 'privilegeCode' must not be blank\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "CreatePrivilegeInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to create privilege\",\n      \"description\": \"Database transaction failed while creating privilege USER_VIEW\"\n    }\n  ]\n}")))
    })
    ResponseEntity<PrivilegeResponse> create(@Valid PrivilegeCreateRequest request);

    @Operation(operationId = "updatePrivilege", summary = "Update privilege", description = "Updates an existing privilege record.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "privilegeCode", description = "Privilege code", required = true, example = "USER_VIEW")})
    @RequestBody(required = true, description = "Privilege payload to update",
        content = @Content(schema = @Schema(implementation = PrivilegeUpdateRequest.class),
            examples = @ExampleObject(name = "UpdatePrivilegeRequestExample",
                value = "{\n  \"privilegeName\": \"View All Users\",\n  \"description\": \"Updated description\"\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Privilege updated successfully",
            content = @Content(schema = @Schema(implementation = PrivilegeResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "UpdatePrivilegeValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"PRIVILEGENAME_REQUIRED\",\n      \"message\": \"privilegeName is required\",\n      \"description\": \"Field 'privilegeName' must not be blank\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "404", description = "Privilege not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "UpdatePrivilegeNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Privilege not found for code=INVALID\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "UpdatePrivilegeInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to update privilege\",\n      \"description\": \"Database unavailable while updating privilege USER_VIEW\"\n    }\n  ]\n}")))
    })
    PrivilegeResponse update(String privilegeCode, @Valid PrivilegeUpdateRequest request);

    @Operation(operationId = "deletePrivilege", summary = "Delete privilege", description = "Soft-deletes a privilege record.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "privilegeCode", description = "Privilege code", required = true, example = "USER_VIEW")})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Privilege deleted successfully",
            content = @Content(schema = @Schema(implementation = PrivilegeResponse.class))),
        @ApiResponse(responseCode = "404", description = "Privilege not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "DeletePrivilegeNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Privilege not found for code=INVALID\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "DeletePrivilegeInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to delete privilege\",\n      \"description\": \"Database unavailable while soft deleting privilege USER_VIEW\"\n    }\n  ]\n}")))
    })
    PrivilegeResponse softDelete(String privilegeCode);
}
