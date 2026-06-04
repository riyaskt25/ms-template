package com.snb.ms.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request) {
    log.debug("Received login request identifier={}", request.getIdentifier());
    AuthResponse response = authService.login(request);
    log.info(
        "Login successful userId={} username={} authorities={}",
        response.getUserId(),
        response.getUsername(),
        response.getAuthorities());
    return ResponseEntity.ok(response);
  }
}
