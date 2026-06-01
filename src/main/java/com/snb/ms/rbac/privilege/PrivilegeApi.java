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
import java.util.List;

@Tag(name = "[503] - Privileges", description = "RBAC privilege management operations")
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
}
