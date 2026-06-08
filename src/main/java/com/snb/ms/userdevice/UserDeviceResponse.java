package com.snb.ms.userdevice;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User device response payload")
public class UserDeviceResponse {

  @Schema(description = "Unique user device identifier", example = "1001")
  private Long userDeviceId;

  @Schema(description = "User identifier", example = "1001")
  private Long userId;

  @Schema(description = "Device identifier", example = "device-abc-123")
  private String deviceId;

  @Schema(description = "Device type", example = "ANDROID")
  private String deviceType;

  @Schema(description = "Device model", example = "Pixel 8")
  private String deviceModel;

  @Schema(description = "Operating system version", example = "14")
  private String osVersion;

  @Schema(description = "Application version", example = "1.0.0")
  private String appVersion;

  @Schema(description = "Push notification token", example = "push-token-value")
  private String pushToken;

  @Schema(description = "Push token updated at", example = "2026-06-01T10:15:30")
  private LocalDateTime pushTokenUpdatedAt;

  @Schema(description = "Device status", example = "ACTIVE")
  private String status;

  @Schema(description = "Registration timestamp", example = "2026-06-01T10:15:30")
  private LocalDateTime registeredAt;

  @Schema(description = "Last active timestamp", example = "2026-06-01T10:20:30")
  private LocalDateTime lastActiveAt;

  @Schema(description = "Last login timestamp", example = "2026-06-01T10:25:30")
  private LocalDateTime lastLoginAt;

  @Schema(description = "IP address", example = "192.168.1.10")
  private String ipAddress;

  @Schema(description = "Device location", example = "Dubai, UAE")
  private String location;

  @Schema(description = "Whether the device is trusted", example = "true")
  private Boolean trusted;

  @Schema(description = "Failed login attempts", example = "0")
  private Integer failedAttempts;

  @Schema(description = "Device friendly name", example = "John's Pixel")
  private String deviceName;

  @Schema(description = "Whether notifications are enabled", example = "true")
  private Boolean notificationsEnabled;
}
