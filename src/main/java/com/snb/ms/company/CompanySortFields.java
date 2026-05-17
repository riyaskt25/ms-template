package com.snb.ms.company;

import java.util.Map;

/**
 * Whitelist of sortable fields for Company entities.
 * Maps API field names to JPA entity paths for safe dynamic sorting.
 */
public final class CompanySortFields {

    public static final Map<String, String> ALLOWED_SORTS = Map.of(
        "companyId", "companyId",
        "registrationNumber", "registrationNumber",
        "companyStatus", "companyStatus",
        "companyType", "companyType",
        "emailAddress", "user.emailAddress",
        "mobileNumber", "user.mobileNumber",
        "createdAt", "createdAt",
        "updatedAt", "updatedAt"
    );

    private CompanySortFields() {
        // Utility class
    }
}
