package com.snb.ms.shared.request;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;
import java.util.Locale;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestContextFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    public static final String TENANT_ID_HEADER = "X-Tenant-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestId = resolveRequestId(request);
        String userId = resolveUserId(request.getUserPrincipal());
        String language = resolveLanguage(request.getLocale());
        String tenantId = resolveHeader(request, TENANT_ID_HEADER);

        RequestContext context = RequestContext.builder()
            .requestId(requestId)
            .userId(userId)
            .language(language)
            .tenantId(tenantId)
            .build();

        RequestContextHolder.set(context);
        response.setHeader(REQUEST_ID_HEADER, requestId);
        putMdc(context);

        try {
            filterChain.doFilter(request, response);
        } finally {
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

    private String resolveUserId(Principal principal) {
        if (principal == null || !StringUtils.hasText(principal.getName())) {
            return "anonymous";
        }
        return principal.getName();
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
