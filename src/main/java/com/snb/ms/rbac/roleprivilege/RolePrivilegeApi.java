package com.snb.ms.rbac.roleprivilege;

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

@Tag(name = "Role Privileges", description = "RBAC role-privilege grant operations")
public interface RolePrivilegeApi {

    @Operation(operationId = "getPrivilegesByRoleCode", summary = "Get privileges by role code", description = "Returns all active privilege grants for a role.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role privileges fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RolePrivilegeResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GetPrivilegesByRoleCodeInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch role privileges\",\n      \"description\": \"Database unavailable while loading privileges for roleCode=SUPER_ADMIN\"\n    }\n  ]\n}")))
    })
    List<RolePrivilegeResponse> findByRoleCode(String roleCode);

    @Operation(operationId = "grantPrivilegeToRole", summary = "Grant privilege to role", description = "Creates a role-privilege grant.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")})
    @RequestBody(required = true, description = "Grant payload",
        content = @Content(schema = @Schema(implementation = RolePrivilegeRequest.class),
            examples = @ExampleObject(name = "GrantPrivilegeToRoleRequestExample",
                value = "{\n  \"privilegeCode\": \"USER_VIEW\"\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Privilege granted successfully",
            content = @Content(schema = @Schema(implementation = RolePrivilegeResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed or privilege already granted",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "RolePrivilegeAlreadyExists",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"ROLEPRIVILEGE_ALREADY_EXISTS\",\n      \"message\": \"Role already has this privilege granted\",\n      \"description\": \"Grant already exists for roleCode=SUPER_ADMIN and privilegeCode=USER_VIEW\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "404", description = "Role or privilege not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GrantPrivilegeToRoleNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role not found for code=INVALID_ROLE\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GrantRolePrivilegeInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to grant privilege to role\",\n      \"description\": \"Database unavailable while creating grant for roleCode=SUPER_ADMIN\"\n    }\n  ]\n}")))
    })
    ResponseEntity<RolePrivilegeResponse> grant(String roleCode, @Valid RolePrivilegeRequest request);

    @Operation(operationId = "grantPrivilegesToRoleBulk", summary = "Grant privileges to role in bulk", description = "Creates multiple role-privilege grants in one request.")
    @CommonApiParameters
    @Parameters({@Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN")})
    @RequestBody(required = true, description = "Bulk grant payload",
        content = @Content(schema = @Schema(implementation = RolePrivilegeBulkRequest.class),
            examples = @ExampleObject(name = "GrantPrivilegesToRoleBulkRequestExample",
                value = "{\n  \"privilegeCodes\": [\"USER_VIEW\", \"USER_EDIT\"]\n}")))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Privileges granted successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RolePrivilegeResponse.class)))),
        @ApiResponse(responseCode = "400", description = "Validation failed or privilege already granted",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "RolePrivilegeBulkValidationError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"VALIDATION_ERROR\",\n      \"code\": \"PRIVILEGECODES_REQUIRED\",\n      \"message\": \"at least one privilegeCode is required\",\n      \"description\": \"Field 'privilegeCodes' must contain at least one value\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "404", description = "Role or privilege not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GrantPrivilegesToRoleBulkNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Privilege not found for code=INVALID_PRIVILEGE\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "GrantRolePrivilegesBulkInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to grant privileges in bulk\",\n      \"description\": \"Database unavailable while saving bulk grants for roleCode=SUPER_ADMIN\"\n    }\n  ]\n}")))
    })
    ResponseEntity<List<RolePrivilegeResponse>> grantBulk(String roleCode, @Valid RolePrivilegeBulkRequest request);

    @Operation(operationId = "revokeRolePrivilege", summary = "Revoke role-privilege grant", description = "Soft-deletes a role-privilege grant by roleCode and privilegeCode.")
    @CommonApiParameters
    @Parameters({
        @Parameter(name = "roleCode", description = "Role code", required = true, example = "SUPER_ADMIN"),
        @Parameter(name = "privilegeCode", description = "Privilege code", required = true, example = "USER_VIEW")
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Privilege revoked successfully",
            content = @Content(schema = @Schema(implementation = RolePrivilegeResponse.class))),
        @ApiResponse(responseCode = "404", description = "Grant not found",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "RevokeRolePrivilegeNotFoundError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Role-privilege grant not found for roleCode=SUPER_ADMIN and privilegeCode=USER_VIEW\"\n    }\n  ]\n}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class),
                examples = @ExampleObject(name = "RevokeRolePrivilegeInternalError",
                    value = "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to revoke role privilege\",\n      \"description\": \"Database unavailable while revoking grant for roleCode=SUPER_ADMIN and privilegeCode=USER_VIEW\"\n    }\n  ]\n}")))
    })
    RolePrivilegeResponse revoke(
        String roleCode,
        String privilegeCode
    );
}
