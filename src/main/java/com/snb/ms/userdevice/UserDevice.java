package com.snb.ms.userdevice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snb.ms.shared.BaseEntity;
import com.snb.ms.shared.Users;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "USER_DEVICE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDevice extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_DEVICE_ID")
  private Long userDeviceId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  private Users user;

  @Column(name = "DEVICE_ID", nullable = false, unique = true, length = 100)
  private String deviceId;

  @Column(name = "DEVICE_TYPE", nullable = false, length = 50)
  private String deviceType;

  @Column(name = "DEVICE_MODEL", length = 100)
  private String deviceModel;

  @Column(name = "OS_VERSION", length = 50)
  private String osVersion;

  @Column(name = "APP_VERSION", length = 50)
  private String appVersion;

  @Column(name = "PUSH_TOKEN", length = 500)
  private String pushToken;

  @Column(name = "PUSH_TOKEN_UPDATED_AT")
  private LocalDateTime pushTokenUpdatedAt;

  @Column(name = "STATUS", nullable = false, length = 30)
  private String status;

  @Column(name = "REGISTERED_AT", nullable = false)
  private LocalDateTime registeredAt;

  @Column(name = "LAST_ACTIVE_AT", nullable = false)
  private LocalDateTime lastActiveAt;

  @Column(name = "LAST_LOGIN_AT", nullable = false)
  private LocalDateTime lastLoginAt;

  @Column(name = "IP_ADDRESS", length = 45)
  private String ipAddress;

  @Column(name = "LOCATION", length = 255)
  private String location;

  @Column(name = "IS_TRUSTED")
  private Boolean trusted;

  @Column(name = "FAILED_ATTEMPTS")
  private Integer failedAttempts;

  @Column(name = "DEVICE_NAME", length = 100)
  private String deviceName;

  @Column(name = "NOTIFICATIONS_ENABLED", nullable = false)
  private Boolean notificationsEnabled;
}
