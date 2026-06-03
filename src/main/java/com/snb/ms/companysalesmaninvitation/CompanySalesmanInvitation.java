package com.snb.ms.companysalesmaninvitation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snb.ms.company.Company;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "COMPANY_SALESMAN_INVITATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanySalesmanInvitation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "COMPANY_SALESMAN_INVITATION_ID")
  private Long companySalesmanInvitationId;

  @Column(name = "SALESMAN_ID")
  private Long salesmanId;

  @Column(name = "EMAIL_ADDRESS", nullable = false, length = 150)
  private String emailAddress;

  @Column(name = "MOBILE_NUMBER", nullable = false, length = 20)
  private String mobileNumber;

  @Column(name = "ID_NUMBER", nullable = false, length = 50)
  private String idNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMPANY_ID", nullable = false)
  private Company company;

  @Column(name = "STATUS", nullable = false, length = 20)
  private String status;

  @Column(name = "INVITED_AT", nullable = false)
  private LocalDateTime invitedAt;

  @Column(name = "RESPONDED_AT")
  private LocalDateTime respondedAt;

  @Column(name = "EXPIRY_DATE", nullable = false)
  private LocalDateTime expiryDate;
}
