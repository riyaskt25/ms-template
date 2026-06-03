package com.snb.ms.shared.request;

import java.util.Optional;

public final class RequestContextHolder {

  private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

  private RequestContextHolder() {}

  public static void set(RequestContext context) {
    CONTEXT.set(context);
  }

  public static Optional<RequestContext> getOptional() {
    return Optional.ofNullable(CONTEXT.get());
  }

  public static RequestContext getRequired() {
    RequestContext context = CONTEXT.get();
    if (context == null) {
      throw new IllegalStateException("RequestContext is not initialized for current thread");
    }
    return context;
  }

  public static void clear() {
    CONTEXT.remove();
  }
}
