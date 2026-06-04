package com.snb.ms.auth;

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
public class AuthResponse {

  private String accessToken;
  private String tokenType;
  private Instant expiresAt;
  private Long userId;
  private String username;
  private List<String> authorities;
}
