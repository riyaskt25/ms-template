package com.snb.ms.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication login request payload")
public class AuthLoginRequest {

  @NotBlank
  @Schema(
      description = "Username, email address, or mobile number used to identify the user",
      example = "admin1@snb.com")
  private String identifier;

  @NotBlank
  @Schema(description = "Plain text password", example = "Ksa@123")
  private String password;
}
