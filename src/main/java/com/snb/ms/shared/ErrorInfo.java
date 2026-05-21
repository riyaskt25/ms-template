package com.snb.ms.shared;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detailed error information")
public class ErrorInfo {
    @Schema(description = "Error category", example = "VALIDATION_ERROR")
    private String type;

    @Schema(description = "Application-specific error code", example = "INVALID_INPUT")
    private String code;

    @Schema(description = "Localized, user-friendly error message", example = "Input value is invalid")
    private String message;

    @Schema(description = "Additional diagnostic details", example = "Rejected value: invalid-input")
    private String description;
}