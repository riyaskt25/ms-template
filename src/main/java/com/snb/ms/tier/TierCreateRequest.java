package com.snb.ms.tier;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Tier creation request payload")
public class TierCreateRequest {

  @NotBlank(message = "{validation.tier.tierCode.required}")
  @Size(max = 50, message = "{validation.tier.tierCode.size}")
  @Schema(
      description = "Tier code",
      example = "GOLD",
      maxLength = 50,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String tierCode;

  @NotBlank(message = "{validation.tier.tierName.required}")
  @Size(max = 150, message = "{validation.tier.tierName.size}")
  @Schema(
      description = "Tier name",
      example = "Gold Tier",
      maxLength = 150,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String tierName;

  @NotNull(message = "{validation.tier.minAchievementPercentage.required}") @Schema(
      description = "Minimum achievement percentage",
      example = "75.00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal minAchievementPercentage;

  @NotNull(message = "{validation.tier.maxAchievementPercentage.required}") @Schema(
      description = "Maximum achievement percentage",
      example = "90.00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal maxAchievementPercentage;

  @NotBlank(message = "{validation.tier.incentiveType.required}")
  @Size(max = 50, message = "{validation.tier.incentiveType.size}")
  @Schema(
      description = "Incentive type",
      example = "PERCENTAGE",
      maxLength = 50,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String incentiveType;

  @NotNull(message = "{validation.tier.incentiveValue.required}") @Schema(
      description = "Incentive value",
      example = "5.00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal incentiveValue;

  @NotNull(message = "{validation.tier.displayOrder.required}") @PositiveOrZero(message = "{validation.tier.displayOrder.positiveOrZero}")
  @Schema(description = "Display order", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer displayOrder;

  @NotNull(message = "{validation.tier.isActive.required}") @Schema(
      description = "Whether the tier is active",
      example = "true",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Boolean active;

  @NotNull(message = "{validation.tier.effectiveFrom.required}") @Schema(
      description = "Effective from date",
      example = "2026-01-01",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate effectiveFrom;

  @NotNull(message = "{validation.tier.effectiveTo.required}") @Schema(
      description = "Effective to date",
      example = "2026-12-31",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate effectiveTo;
}
