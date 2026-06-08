package com.snb.ms.quotation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snb.ms.company.Company;
import com.snb.ms.salesman.Salesman;
import com.snb.ms.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "QUOTATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Quotation extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "QUOTATION_ID")
  private Long quotationId;

  @Column(name = "QUOTATION_NO", nullable = false, unique = true, length = 100)
  private String quotationNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMPANY_ID", nullable = false)
  private Company company;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SALESMAN_ID", nullable = false)
  private Salesman salesman;

  @Column(name = "CUSTOMER_NATIONAL_ID", nullable = false, length = 50)
  private String customerNationalId;

  @Column(name = "CUSTOMER_MOBILE", nullable = false, length = 20)
  private String customerMobile;

  @Column(name = "CUSTOMER_EMAIL", nullable = false, length = 150)
  private String customerEmail;

  @Column(name = "VEHICLE_ID", nullable = false)
  private Long vehicleId;

  @Column(name = "STATUS", nullable = false, length = 30)
  private String status;

  @Column(name = "VEHICLE_PRICE", precision = 15, scale = 2, nullable = false)
  private BigDecimal vehiclePrice;

  @Column(name = "DOWN_PAYMENT", precision = 15, scale = 2, nullable = false)
  private BigDecimal downPayment;

  @Column(name = "LEASE_TENURE_MONTHS", nullable = false)
  private Integer leaseTenureMonths;

  @Column(name = "MONTHLY_INSTALLMENT", precision = 15, scale = 2, nullable = false)
  private BigDecimal monthlyInstallment;

  @Column(name = "TOTAL_QUOTAION_AMOUNT", precision = 15, scale = 2, nullable = false)
  private BigDecimal totalQuotationAmount;

  @Column(name = "IS_INCENTIVE_ELIGIBLE", nullable = false)
  private Boolean incentiveEligible;

  @Column(name = "INCENTIVE_CAMPAIGN_ID")
  private Long incentiveCampaignId;

  @Column(name = "VALID_FROM", nullable = false)
  private LocalDate validFrom;

  @Column(name = "VALID_UNTIL", nullable = false)
  private LocalDate validUntil;
}
