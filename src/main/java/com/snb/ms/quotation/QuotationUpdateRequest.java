package com.snb.ms.quotation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Quotation update request payload")
public class QuotationUpdateRequest {

  @NotBlank(message = "{validation.quotation.quotationNo.required}")
  @Size(max = 100, message = "{validation.quotation.quotationNo.size}")
  @Schema(
      description = "Quotation number",
      example = "QUO-2026-0001",
      maxLength = 100,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String quotationNo;

  @NotNull(message = "{validation.quotation.companyId.required}") @Positive(message = "{validation.quotation.companyId.positive}") @Schema(
      description = "Company identifier",
      example = "1001",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Long companyId;

  @NotNull(message = "{validation.quotation.salesmanId.required}") @Positive(message = "{validation.quotation.salesmanId.positive}") @Schema(
      description = "Salesman identifier",
      example = "1001",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Long salesmanId;

  @NotBlank(message = "{validation.quotation.customerNationalId.required}")
  @Size(max = 50, message = "{validation.quotation.customerNationalId.size}")
  @Schema(
      description = "Customer national identifier",
      example = "784-1986-0000001-1",
      maxLength = 50,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String customerNationalId;

  @NotBlank(message = "{validation.quotation.customerMobile.required}")
  @Size(max = 20, message = "{validation.quotation.customerMobile.size}")
  @Schema(
      description = "Customer mobile number",
      example = "+971555011000",
      maxLength = 20,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String customerMobile;

  @NotBlank(message = "{validation.quotation.customerEmail.required}")
  @Email(message = "{validation.quotation.customerEmail.email}")
  @Size(max = 150, message = "{validation.quotation.customerEmail.size}")
  @Schema(
      description = "Customer email address",
      example = "customer.updated@example.com",
      maxLength = 150,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String customerEmail;

  @NotNull(message = "{validation.quotation.vehicleId.required}") @Positive(message = "{validation.quotation.vehicleId.positive}") @Schema(
      description = "Vehicle identifier",
      example = "2001",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Long vehicleId;

  @NotBlank(message = "{validation.quotation.status.required}")
  @Size(max = 30, message = "{validation.quotation.status.size}")
  @Schema(
      description = "Quotation status",
      example = "SENT",
      maxLength = 30,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String status;

  @NotNull(message = "{validation.quotation.vehiclePrice.required}") @Schema(
      description = "Vehicle price",
      example = "120000.00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal vehiclePrice;

  @NotNull(message = "{validation.quotation.downPayment.required}") @PositiveOrZero(message = "{validation.quotation.downPayment.positiveOrZero}")
  @Schema(
      description = "Down payment",
      example = "10000.00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal downPayment;

  @NotNull(message = "{validation.quotation.leaseTenureMonths.required}") @Positive(message = "{validation.quotation.leaseTenureMonths.positive}") @Schema(
      description = "Lease tenure in months",
      example = "36",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer leaseTenureMonths;

  @NotNull(message = "{validation.quotation.monthlyInstallment.required}") @Positive(message = "{validation.quotation.monthlyInstallment.positive}") @Schema(
      description = "Monthly installment",
      example = "3150.00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal monthlyInstallment;

  @NotNull(message = "{validation.quotation.totalQuotationAmount.required}") @Positive(message = "{validation.quotation.totalQuotationAmount.positive}") @Schema(
      description = "Total quotation amount",
      example = "126000.00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal totalQuotationAmount;

  @NotNull(message = "{validation.quotation.isIncentiveEligible.required}") @Schema(
      description = "Whether the quotation is incentive eligible",
      example = "true",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Boolean incentiveEligible;

  @Schema(description = "Incentive campaign identifier", example = "3001")
  @Positive(message = "{validation.quotation.incentiveCampaignId.positive}") private Long incentiveCampaignId;

  @NotNull(message = "{validation.quotation.validFrom.required}") @Schema(
      description = "Quotation valid from date",
      example = "2026-06-01",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate validFrom;

  @NotNull(message = "{validation.quotation.validUntil.required}") @Schema(
      description = "Quotation valid until date",
      example = "2026-06-30",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate validUntil;
}
