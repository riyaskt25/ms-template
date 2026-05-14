package com.snb.ms.shared;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard paginated response wrapper")
public class PaginatedResponseDTO<T> {

    @ArraySchema(arraySchema = @Schema(description = "List of items in the current page"))
    private List<T> data;

    @Schema(description = "Pagination metadata")
    private PaginationMeta pagination;

    public static <T> PaginatedResponseDTO<T> fromPage(Page<T> page) {
        PaginationMeta meta = new PaginationMeta(
            page.getNumber(),
            page.getSize(),
            page.getNumberOfElements(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast(),
            page.hasNext(),
            page.hasPrevious(),
            page.getSort().isSorted(),
            mapSortFields(page.getSort())
        );
        return new PaginatedResponseDTO<>(page.getContent(), meta);
    }

    private static List<String> mapSortFields(Sort sort) {
        return sort.stream()
            .map(order -> order.getProperty() + "," + order.getDirection().name())
            .collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Pagination metadata")
    public static class PaginationMeta {
        @Schema(description = "Zero-based page index", example = "0")
        private int page;

        @Schema(description = "Requested page size", example = "10")
        private int size;

        @Schema(description = "Number of elements in current page", example = "7")
        private int numberOfElements;

        @Schema(description = "Total number of matching elements", example = "57")
        private long totalElements;

        @Schema(description = "Total number of pages", example = "6")
        private int totalPages;

        @Schema(description = "Whether this page is the first page", example = "true")
        private boolean first;

        @Schema(description = "Whether this page is the last page", example = "false")
        private boolean last;

        @Schema(description = "Whether there is a next page", example = "true")
        private boolean hasNext;

        @Schema(description = "Whether there is a previous page", example = "false")
        private boolean hasPrevious;

        @Schema(description = "Whether any sorting is applied", example = "true")
        private boolean sorted;

        @ArraySchema(arraySchema = @Schema(description = "Applied sort fields in property,DIRECTION format"))
        private List<String> sort;
    }
}