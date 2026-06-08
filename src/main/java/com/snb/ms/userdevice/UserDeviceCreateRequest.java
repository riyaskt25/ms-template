package com.snb.ms.userdevice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User device creation request payload")
public class UserDeviceCreateRequest {

  @NotNull(message = "{validation.userDevice.userId.required}") @Positive(message = "{validation.userDevice.userId.positive}") @Schema(
      description = "User identifier",
      example = "1001",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Long userId;

  @NotBlank(message = "{validation.userDevice.deviceId.required}")
  @Size(max = 100, message = "{validation.userDevice.deviceId.size}")
  @Schema(
      description = "Device identifier",
      example = "device-abc-123",
      maxLength = 100,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String deviceId;

  @NotBlank(message = "{validation.userDevice.deviceType.required}")
  @Size(max = 50, message = "{validation.userDevice.deviceType.size}")
  @Schema(
      description = "Device type",
      example = "ANDROID",
      maxLength = 50,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String deviceType;

  @Size(max = 100, message = "{validation.userDevice.deviceModel.size}")
  @Schema(description = "Device model", example = "Pixel 8", maxLength = 100)
  private String deviceModel;

  @Size(max = 50, message = "{validation.userDevice.osVersion.size}")
  @Schema(description = "Operating system version", example = "14", maxLength = 50)
  private String osVersion;

  @Size(max = 50, message = "{validation.userDevice.appVersion.size}")
  @Schema(description = "Application version", example = "1.0.0", maxLength = 50)
  private String appVersion;

  @Size(max = 500, message = "{validation.userDevice.pushToken.size}")
  @Schema(description = "Push notification token", example = "push-token-value", maxLength = 500)
  private String pushToken;

  @Schema(description = "Push token updated at", example = "2026-06-01T10:15:30")
  private LocalDateTime pushTokenUpdatedAt;

  @NotBlank(message = "{validation.userDevice.status.required}")
  @Size(max = 30, message = "{validation.userDevice.status.size}")
  @Schema(
      description = "Device status",
      example = "ACTIVE",
      maxLength = 30,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String status;

  @NotNull(message = "{validation.userDevice.registeredAt.required}") @Schema(
      description = "Registration timestamp",
      example = "2026-06-01T10:15:30",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDateTime registeredAt;

  @NotNull(message = "{validation.userDevice.lastActiveAt.required}") @Schema(
      description = "Last active timestamp",
      example = "2026-06-01T10:20:30",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDateTime lastActiveAt;

  @NotNull(message = "{validation.userDevice.lastLoginAt.required}") @Schema(
      description = "Last login timestamp",
      example = "2026-06-01T10:25:30",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDateTime lastLoginAt;

  @Size(max = 45, message = "{validation.userDevice.ipAddress.size}")
  @Schema(description = "IP address", example = "192.168.1.10", maxLength = 45)
  private String ipAddress;

  @Size(max = 255, message = "{validation.userDevice.location.size}")
  @Schema(description = "Device location", example = "Dubai, UAE", maxLength = 255)
  private String location;

  @Schema(description = "Whether the device is trusted", example = "true")
  private Boolean trusted;

  @PositiveOrZero(message = "{validation.userDevice.failedAttempts.positiveOrZero}")
  @Schema(description = "Failed login attempts", example = "0")
  private Integer failedAttempts;

  @Size(max = 100, message = "{validation.userDevice.deviceName.size}")
  @Schema(description = "Device friendly name", example = "John's Pixel", maxLength = 100)
  private String deviceName;

  @NotNull(message = "{validation.userDevice.notificationsEnabled.required}") @Schema(
      description = "Whether notifications are enabled",
      example = "true",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Boolean notificationsEnabled;
}
