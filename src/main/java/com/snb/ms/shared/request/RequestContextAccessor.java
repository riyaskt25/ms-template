package com.snb.ms.shared.request;

import com.snb.ms.auth.AuthenticatedUserPrincipal;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class RequestContextAccessor {

  public Optional<RequestContext> current() {
    return RequestContextHolder.getOptional();
  }

  public RequestContext requireCurrent() {
    return RequestContextHolder.getRequired();
  }

  public Optional<String> currentUserId() {
    return current().map(RequestContext::getUserId);
  }

  public Optional<String> currentLanguage() {
    return current().map(RequestContext::getLanguage);
  }

  public Optional<String> currentRequestId() {
    return current().map(RequestContext::getRequestId);
  }

  /**
   * Returns the USER_ID from the USER_ID request header. Returns empty if the header is not present
   * or not numeric.
   */
  public Optional<Long> headerUserIdAsLong() {
    Optional<Long> headerUserId =
        current()
            .map(RequestContext::getHeaderUserId)
            .flatMap(
                uid -> {
                  try {
                    return Optional.of(Long.parseLong(uid));
                  } catch (NumberFormatException e) {
                    return Optional.empty();
                  }
                });

    if (headerUserId.isPresent()) {
      return headerUserId;
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof AuthenticatedUserPrincipal authenticatedUser) {
      return Optional.ofNullable(authenticatedUser.getUserId());
    }

    try {
      return Optional.of(Long.parseLong(authentication.getName()));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }
}
