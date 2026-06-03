package com.snb.ms.shared.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation that groups common pagination parameters. Reduces boilerplate by combining page,
 * size, sortBy, and sortDirection parameters. Use this on paginated list operations to
 * automatically include these parameters. Combine with @Parameters for additional filters specific
 * to the endpoint.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Parameters({
  @Parameter(name = "page", description = "Zero-based page index", example = "0"),
  @Parameter(name = "size", description = "Page size (max 200)", example = "20"),
  @Parameter(name = "sortBy", description = "Sort fields (comma-separated)", example = "id"),
  @Parameter(
      name = "sortDirection",
      description = "Sort directions (single value for all or comma-separated per field)",
      example = "ASC")
})
public @interface PaginationParameters {}
