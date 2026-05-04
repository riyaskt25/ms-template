package com.snb.ms.shared.request;

import org.springframework.stereotype.Component;

import java.util.Optional;

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
     * Parses the current userId as a Long.
     * Returns empty if userId is null, "anonymous", or non-numeric.
     * Once Spring Security is added, the principal name will be a numeric user id.
     */
    public Optional<Long> currentUserIdAsLong() {
        return currentUserId().flatMap(uid -> {
            try {
                return Optional.of(Long.parseLong(uid));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });
    }
}
