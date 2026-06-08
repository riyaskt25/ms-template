package com.snb.ms.quotation;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Quotation response payload")
public class QuotationResponse {

  @Schema(description = "Unique quotation identifier", example = "1001")
  private Long quotationId;

  @Schema(description = "Quotation number", example = "QUO-2026-0001")
  private String quotationNo;

  @Schema(description = "Company identifier", example = "1001")
  private Long companyId;

  @Schema(description = "Salesman identifier", example = "1001")
  private Long salesmanId;

  @Schema(description = "Customer national identifier", example = "784-1986-0000001-1")
  private String customerNationalId;

  @Schema(description = "Customer mobile number", example = "+971555010999")
  private String customerMobile;

  @Schema(description = "Customer email address", example = "customer@example.com")
  private String customerEmail;

  @Schema(description = "Vehicle identifier", example = "2001")
  private Long vehicleId;

  @Schema(description = "Quotation status", example = "DRAFT")
  private String status;

  @Schema(description = "Vehicle price", example = "120000.00")
  private BigDecimal vehiclePrice;

  @Schema(description = "Down payment", example = "10000.00")
  private BigDecimal downPayment;

  @Schema(description = "Lease tenure in months", example = "36")
  private Integer leaseTenureMonths;

  @Schema(description = "Monthly installment", example = "3150.00")
  private BigDecimal monthlyInstallment;

  @Schema(description = "Total quotation amount", example = "126000.00")
  private BigDecimal totalQuotationAmount;

  @Schema(description = "Whether the quotation is incentive eligible", example = "true")
  private Boolean incentiveEligible;

  @Schema(description = "Incentive campaign identifier", example = "3001")
  private Long incentiveCampaignId;

  @Schema(description = "Quotation valid from date", example = "2026-06-01")
  private LocalDate validFrom;

  @Schema(description = "Quotation valid until date", example = "2026-06-30")
  private LocalDate validUntil;

  @Schema(description = "Identifier of actor who created the record", example = "1001")
  private Long createdBy;

  @Schema(
      description = "Record creation timestamp in ISO-8601 format",
      example = "2026-05-04T10:15:30")
  private LocalDateTime createdAt;

  @Schema(description = "Identifier of actor who last updated the record", example = "1002")
  private Long updatedBy;

  @Schema(description = "Last update timestamp in ISO-8601 format", example = "2026-05-04T11:20:45")
  private LocalDateTime updatedAt;
}
