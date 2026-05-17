package com.snb.ms.shared.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for common string parsing operations.
 */
public final class StringParsingUtils {

    private StringParsingUtils() {
        // Utility class
    }

    /**
     * Splits a comma-separated string into a list of trimmed, non-empty values.
     *
     * @param raw the CSV string to split (null or empty returns empty list)
     * @return list of trimmed values with empty strings filtered out
     */
    public static List<String> splitCsv(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return List.of();
        }

        List<String> values = new ArrayList<>();
        for (String item : raw.split(",")) {
            String trimmed = item.trim();
            if (!trimmed.isEmpty()) {
                values.add(trimmed);
            }
        }
        return values;
    }
}
