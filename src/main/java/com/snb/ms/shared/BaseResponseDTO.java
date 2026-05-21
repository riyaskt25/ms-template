package com.snb.ms.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Base API response wrapper with error details")
public class BaseResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ArraySchema(schema = @Schema(implementation = ErrorInfo.class), arraySchema = @Schema(description = "List of errors returned for failed requests"))
    private List<ErrorInfo> errors = new ArrayList<>();
}