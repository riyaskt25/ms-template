package com.snb.ms.shared.request;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Locale;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component("appRequestContextFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestContextFilter extends OncePerRequestFilter {

  public static final String REQUEST_ID_HEADER = "X-Request-Id";
  public static final String TENANT_ID_HEADER = "X-Tenant-Id";
  public static final String USER_ID_HEADER = "USER_ID";

  @Value("${app.logging.request-boundary-enabled:false}")
  private boolean requestBoundaryEnabled;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return "/favicon.ico".equals(request.getRequestURI());
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    long startedAt = System.currentTimeMillis();
    String requestId = resolveRequestId(request);
    String userId = resolveUserId(request);
    String language = resolveLanguage(request.getLocale());
    String tenantId = resolveHeader(request, TENANT_ID_HEADER);
    String headerUserId = resolveHeaderUserId(request);

    RequestContext context =
        RequestContext.builder()
            .requestId(requestId)
            .userId(userId)
            .language(language)
            .tenantId(tenantId)
            .headerUserId(headerUserId)
            .build();

    RequestContextHolder.set(context);
    response.setHeader(REQUEST_ID_HEADER, requestId);
    putMdc(context);

    try {
      if (requestBoundaryEnabled && log.isDebugEnabled()) {
        log.debug(
            "---------------------------------------------------------------------------------------");
        log.debug(
            "Incoming request method={} uri={} requestId={}",
            request.getMethod(),
            request.getRequestURI(),
            requestId);
      }
      filterChain.doFilter(request, response);
    } finally {
      if (requestBoundaryEnabled && log.isDebugEnabled()) {
        long durationMs = System.currentTimeMillis() - startedAt;
        log.debug(
            "Completed request method={} uri={} status={} durationMs={} requestId={}",
            request.getMethod(),
            request.getRequestURI(),
            response.getStatus(),
            durationMs,
            requestId);
        log.debug(
            "---------------------------------------------------------------------------------------");
      }
      RequestContextHolder.clear();
      MDC.remove("requestId");
      MDC.remove("userId");
      MDC.remove("language");
      MDC.remove("tenantId");
    }
  }

  private String resolveRequestId(HttpServletRequest request) {
    String requestId = resolveHeader(request, REQUEST_ID_HEADER);
    return StringUtils.hasText(requestId) ? requestId : UUID.randomUUID().toString();
  }

  private String resolveUserId(HttpServletRequest request) {
    String resolved = resolvePrincipalName(request.getUserPrincipal());
    if (StringUtils.hasText(resolved)) {
      return resolved;
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return "anonymous";
    }

    resolved = resolvePrincipalName(authentication);
    return StringUtils.hasText(resolved) ? resolved : "anonymous";
  }

  private String resolvePrincipalName(Object principal) {
    if (principal == null) {
      return null;
    }
    if (principal instanceof Principal securityPrincipal) {
      return StringUtils.hasText(securityPrincipal.getName()) ? securityPrincipal.getName() : null;
    }
    if (principal instanceof Authentication authentication) {
      return StringUtils.hasText(authentication.getName()) ? authentication.getName() : null;
    }
    return null;
  }

  private String resolveHeaderUserId(HttpServletRequest request) {
    String headerUserId = resolveHeader(request, USER_ID_HEADER);
    if (StringUtils.hasText(headerUserId)) {
      return headerUserId;
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof com.snb.ms.auth.AuthenticatedUserPrincipal authenticatedUser
        && authenticatedUser.getUserId() != null) {
      return String.valueOf(authenticatedUser.getUserId());
    }

    return StringUtils.hasText(authentication.getName()) ? authentication.getName() : null;
  }

  private String resolveLanguage(Locale locale) {
    if (locale == null || !StringUtils.hasText(locale.toLanguageTag())) {
      return Locale.ENGLISH.toLanguageTag();
    }
    return locale.toLanguageTag();
  }

  private String resolveHeader(HttpServletRequest request, String headerName) {
    String headerValue = request.getHeader(headerName);
    return StringUtils.hasText(headerValue) ? headerValue.trim() : null;
  }

  private void putMdc(RequestContext context) {
    MDC.put("requestId", context.getRequestId());
    if (context.getUserId() != null) {
      MDC.put("userId", context.getUserId());
    }
    if (context.getLanguage() != null) {
      MDC.put("language", context.getLanguage());
    }
    if (context.getTenantId() != null) {
      MDC.put("tenantId", context.getTenantId());
    }
  }
}
