package com.snb.ms.auth.authorization;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationChecker {

  public void ensureHasPrivilege(Privileges privilege) {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AuthenticationCredentialsNotFoundException("Authentication is required");
    }

    final boolean granted =
        authentication.getAuthorities().stream()
            .map(authority -> authority.getAuthority())
            .filter(Objects::nonNull)
            .anyMatch(privilege.name()::equals);

    if (!granted) {
      final String principalName =
          authentication.getPrincipal() instanceof UserDetails userDetails
              ? userDetails.getUsername()
              : authentication.getName();
      log.warn("Access denied principal={} privilege={}", principalName, privilege);
      throw new AccessDeniedException("Missing required privilege: " + privilege.name());
    }
  }
}
