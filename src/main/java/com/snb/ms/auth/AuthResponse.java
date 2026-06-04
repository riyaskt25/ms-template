package com.snb.ms.auth;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response containing JWT token details and user authorities")
public class AuthResponse {

  @Schema(
      description = "JWT access token",
      example =
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjFAc25iLmNvbSIsInVzZXJJZCI6MSwiZXhwIjoxNzAwMDAwMDAwfQ.signature")
  private String accessToken;

  @Schema(description = "Token type returned to the client", example = "Bearer")
  private String tokenType;

  @Schema(description = "Token expiry timestamp in UTC", example = "2026-06-04T12:30:00Z")
  private Instant expiresAt;

  @Schema(description = "Authenticated user identifier", example = "1")
  private Long userId;

  @Schema(description = "Authenticated username", example = "admin1@snb.com")
  private String username;

  @ArraySchema(schema = @Schema(description = "Granted authority code", example = "USER_VIEW"))
  private List<String> authorities;
}
