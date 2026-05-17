package com.snb.ms.shared.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation that groups common API headers.
 * Reduces boilerplate by combining X-Request-Id, X-Tenant-Id, and Accept-Language headers.
 * Use this on your @Operation methods to automatically include these parameters.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Parameters({
    @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
    @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
    @Parameter(ref = "#/components/parameters/AcceptLanguageHeader")
})
public @interface CommonApiParameters {
}
