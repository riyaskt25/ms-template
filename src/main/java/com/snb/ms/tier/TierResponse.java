package com.snb.ms.tier;

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
@Schema(description = "Tier response payload")
public class TierResponse {

  @Schema(description = "Unique tier identifier", example = "1001")
  private Long tierId;

  @Schema(description = "Tier code", example = "GOLD")
  private String tierCode;

  @Schema(description = "Tier name", example = "Gold Tier")
  private String tierName;

  @Schema(description = "Minimum achievement percentage", example = "75.00")
  private BigDecimal minAchievementPercentage;

  @Schema(description = "Maximum achievement percentage", example = "90.00")
  private BigDecimal maxAchievementPercentage;

  @Schema(description = "Incentive type", example = "PERCENTAGE")
  private String incentiveType;

  @Schema(description = "Incentive value", example = "5.00")
  private BigDecimal incentiveValue;

  @Schema(description = "Display order", example = "1")
  private Integer displayOrder;

  @Schema(description = "Whether the tier is active", example = "true")
  private Boolean active;

  @Schema(description = "Effective from date", example = "2026-01-01")
  private LocalDate effectiveFrom;

  @Schema(description = "Effective to date", example = "2026-12-31")
  private LocalDate effectiveTo;

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
