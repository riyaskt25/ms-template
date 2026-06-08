package com.snb.ms.tier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snb.ms.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "TIER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Tier extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "TIER_ID")
  private Long tierId;

  @Column(name = "TIER_CODE", nullable = false, unique = true, length = 50)
  private String tierCode;

  @Column(name = "TIER_NAME", nullable = false, length = 150)
  private String tierName;

  @Column(name = "MIN_ACHIEVEMENT_PERCENTAGE", nullable = false, precision = 9, scale = 2)
  private BigDecimal minAchievementPercentage;

  @Column(name = "MAX_ACHIEVEMENT_PERCENTAGE", nullable = false, precision = 9, scale = 2)
  private BigDecimal maxAchievementPercentage;

  @Column(name = "INCENTIVE_TYPE", nullable = false, length = 50)
  private String incentiveType;

  @Column(name = "INCENTIVE_VALUE", nullable = false, precision = 15, scale = 2)
  private BigDecimal incentiveValue;

  @Column(name = "DISPLAY_ORDER", nullable = false)
  private Integer displayOrder;

  @Column(name = "IS_ACTIVE", nullable = false)
  private Boolean active;

  @Column(name = "EFFECTIVE_FROM", nullable = false)
  private LocalDate effectiveFrom;

  @Column(name = "EFFECTIVE_TO", nullable = false)
  private LocalDate effectiveTo;
}
