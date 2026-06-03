package com.snb.ms.companysalesman;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snb.ms.company.Company;
import com.snb.ms.salesman.Salesman;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "COMPANY_SALESMAN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanySalesman {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "COMPANY_SALESMAN_ID")
  private Long companySalesmanId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMPANY_ID", nullable = false)
  private Company company;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SALESMAN_ID", nullable = false)
  private Salesman salesman;

  @Column(name = "CURRENT_TARGET", precision = 15, scale = 2)
  private BigDecimal currentTarget;

  @Column(name = "ACHIEVED_TARGET", precision = 15, scale = 2)
  private BigDecimal achievedTarget;

  @Column(name = "ASSOCIATION_STATUS", length = 20)
  private String associationStatus;

  @Column(name = "JOINED_AT")
  private LocalDateTime joinedAt;

  @Column(name = "EXITED_AT")
  private LocalDateTime exitedAt;
}
