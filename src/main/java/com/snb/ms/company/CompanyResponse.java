package com.snb.ms.company;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snb.ms.shared.BaseResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"errors"})
@Schema(description = "Company response payload")
public class CompanyResponse extends BaseResponseDTO {
    @Schema(description = "Unique company identifier", example = "1")
    private Long companyId;

    @Schema(description = "Primary email address", example = "company@example.com")
    private String emailAddress;

    @Schema(description = "Primary mobile number", example = "+971555010101")
    private String mobileNumber;

    @Schema(description = "Registration number", example = "REG-2026-0001")
    private String registrationNumber;

    @Schema(description = "Current status of company", example = "ACTIVE")
    private String companyStatus;

    @Schema(description = "Company type classification", example = "STANDARD")
    private String companyType;

    @Schema(description = "Identifier of actor who created the record", example = "1001")
    private Long createdBy;

    @Schema(description = "Record creation timestamp in ISO-8601 format", example = "2026-05-04T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Identifier of actor who last updated the record", example = "1002")
    private Long updatedBy;

    @Schema(description = "Last update timestamp in ISO-8601 format", example = "2026-05-04T11:20:45")
    private LocalDateTime updatedAt;

    @Schema(description = "Associated salesmen (only when includeSalesmen=true is requested)")
    private List<com.snb.ms.salesman.SalesmanResponse> salesmen;
}
