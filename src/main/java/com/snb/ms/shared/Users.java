package com.snb.ms.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Users extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "EMAIL_ADDRESS", nullable = false, unique = true, length = 150)
  private String emailAddress;

  @Column(name = "MOBILE_NUMBER", nullable = false, unique = true, length = 20)
  private String mobileNumber;

  @Column(name = "PASSWORD_HASH", length = 255)
  private String passwordHash;

  @Column(name = "USER_TYPE", length = 20)
  private String userType;

  @Column(name = "FAILED_ATTEMPTS")
  private Integer failedAttempts;

  @Column(name = "ACCOUNT_LOCKED_FLAG", length = 1)
  private String accountLockedFlag;

  @Column(name = "LAST_LOGIN_AT")
  private LocalDateTime lastLoginAt;

  @Column(name = "ACCOUNT_STATUS", length = 20)
  private String accountStatus;
}
