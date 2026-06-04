package com.snb.ms.auth;

import com.snb.ms.shared.BaseResponseDTO;
import com.snb.ms.shared.api.CommonApiParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "[001] - Auth", description = "Authentication operations")
public interface AuthApi {

  @Operation(
      operationId = "login",
      summary = "Authenticate user",
      description =
          "Authenticates a user with identifier and password and returns a JWT access token.")
  @SecurityRequirements
  @CommonApiParameters
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Login successful",
        content = @Content(schema = @Schema(implementation = AuthResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid login request",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "LoginValidationError",
                        value =
                            "{\n"
                                + "  \"errors\": [\n"
                                + "    {\n"
                                + "      \"type\": \"VALIDATION_ERROR\",\n"
                                + "      \"code\": \"INVALID_INPUT\",\n"
                                + "      \"message\": \"Validation failed\",\n"
                                + "      \"description\": \"identifier and password must not be blank\"\n"
                                + "    }\n"
                                + "  ]\n"
                                + "}"))),
    @ApiResponse(
        responseCode = "401",
        description = "Invalid credentials",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "LoginUnauthorized",
                        value =
                            "{\n"
                                + "  \"errors\": [\n"
                                + "    {\n"
                                + "      \"type\": \"AUTH_ERROR\",\n"
                                + "      \"code\": \"INVALID_CREDENTIALS\",\n"
                                + "      \"message\": \"Authentication failed\",\n"
                                + "      \"description\": \"Invalid username or password\"\n"
                                + "    }\n"
                                + "  ]\n"
                                + "}"))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "LoginServerError",
                        value =
                            "{\n"
                                + "  \"errors\": [\n"
                                + "    {\n"
                                + "      \"type\": \"SERVER_ERROR\",\n"
                                + "      \"code\": \"INTERNAL_ERROR\",\n"
                                + "      \"message\": \"Login failed\",\n"
                                + "      \"description\": \"Unexpected error while processing login request\"\n"
                                + "    }\n"
                                + "  ]\n"
                                + "}")))
  })
  @PostMapping("/login")
  ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request);
}
