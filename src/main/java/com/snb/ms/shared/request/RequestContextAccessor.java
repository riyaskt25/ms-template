package com.snb.ms.shared.request;

import java.util.Optional;
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
    return current()
        .map(RequestContext::getHeaderUserId)
        .flatMap(
            uid -> {
              try {
                return Optional.of(Long.parseLong(uid));
              } catch (NumberFormatException e) {
                return Optional.empty();
              }
            });
  }
}
