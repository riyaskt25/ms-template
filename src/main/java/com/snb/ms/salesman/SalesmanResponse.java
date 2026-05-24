package com.snb.ms.salesman;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Salesman response payload")
public class SalesmanResponse {
    @Schema(description = "Unique salesman identifier", example = "1001")
    private Long salesmanId;

    @Schema(description = "First name", example = "Ahamed")
    private String firstName;

    @Schema(description = "Middle name", example = "Ibrahim")
    private String middleName;

    @Schema(description = "Last name", example = "Khan")
    private String lastName;

    @Schema(description = "Account number", example = "ACC-9911")
    private String accountNumber;

    @Schema(description = "Customer information file number", example = "CIF-1022")
    private String cifNumber;

    @Schema(description = "National or identity number", example = "784-1986-0000001-1")
    private String idNumber;

    @Schema(description = "Available incentive amount", example = "1250.75")
    private BigDecimal availableIncentiveAmount;

    @Schema(description = "Identifier of actor who created the record", example = "1001")
    private Long createdBy;

    @Schema(description = "Record creation timestamp in ISO-8601 format", example = "2026-05-04T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Identifier of actor who last updated the record", example = "1002")
    private Long updatedBy;

    @Schema(description = "Last update timestamp in ISO-8601 format", example = "2026-05-04T11:20:45")
    private LocalDateTime updatedAt;
}
