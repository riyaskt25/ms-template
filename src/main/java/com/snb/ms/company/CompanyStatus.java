package com.snb.ms.company;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum CompanyStatus {
    PENDING,
    ACTIVE,
    REJECTED;

    public static Optional<CompanyStatus> fromValue(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        String normalized = value.trim().toUpperCase(Locale.ROOT);
        return Arrays.stream(values())
            .filter(status -> status.name().equals(normalized))
            .findFirst();
    }
}