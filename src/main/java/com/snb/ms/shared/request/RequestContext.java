package com.snb.ms.shared.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestContext {

    private final String requestId;
    private final String userId;
    private final String language;
    private final String tenantId;
}
