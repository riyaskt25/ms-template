package com.snb.ms.auth;

import com.snb.ms.shared.UsersRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;
  private final UsersRepository usersRepository;

  @Transactional
  public AuthResponse login(AuthLoginRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getIdentifier(), request.getPassword()));

    AuthenticatedUserPrincipal principal =
        (AuthenticatedUserPrincipal) authentication.getPrincipal();
    usersRepository
        .findById(principal.getUserId())
        .ifPresent(
            user -> {
              user.setLastLoginAt(LocalDateTime.now());
              user.setFailedAttempts(0);
              user.setUpdatedAt(LocalDateTime.now());
              user.setVersionNumber(
                  (user.getVersionNumber() == null ? 0L : user.getVersionNumber()) + 1);
              usersRepository.save(user);
            });

    String token = jwtTokenService.generateToken(principal);
    return AuthResponse.builder()
        .accessToken(token)
        .tokenType("Bearer")
        .expiresAt(jwtTokenService.extractExpiresAt(token))
        .userId(principal.getUserId())
        .username(principal.getUsername())
        .authorities(principal.getAuthorities().stream().map(auth -> auth.getAuthority()).toList())
        .build();
  }
}
