package com.snb.ms.announcement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Announcement update request payload")
public class AnnouncementUpdateRequest {

  @NotBlank(message = "{validation.announcement.title.required}")
  @Size(max = 200, message = "{validation.announcement.title.size}")
  @Schema(
      description = "Announcement title",
      example = "Summer campaign updated",
      requiredMode = Schema.RequiredMode.REQUIRED,
      maxLength = 200)
  private String title;

  @NotBlank(message = "{validation.announcement.description.required}")
  @Size(max = 2000, message = "{validation.announcement.description.size}")
  @Schema(
      description = "Announcement description",
      example = "Updated campaign description.",
      requiredMode = Schema.RequiredMode.REQUIRED,
      maxLength = 2000)
  private String description;

  @Size(max = 500, message = "{validation.announcement.imageUrl.size}")
  @Schema(
      description = "Announcement image URL",
      example = "https://cdn.example.com/announcements/summer-updated.png",
      maxLength = 500)
  private String imageUrl;

  @NotNull(message = "{validation.announcement.priority.required}") @Schema(
      description = "Announcement priority",
      example = "CRITICAL",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private AnnouncementPriority priority;

  @NotNull(message = "{validation.announcement.type.required}") @Schema(
      description = "Announcement type",
      example = "NEWS",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private AnnouncementType type;

  @NotNull(message = "{validation.announcement.startDate.required}") @Schema(
      description = "Announcement start date",
      example = "2026-06-02",
      requiredMode = Schema.RequiredMode.REQUIRED,
      type = "string",
      format = "date")
  private LocalDate startDate;

  @NotNull(message = "{validation.announcement.endDate.required}") @Schema(
      description = "Announcement end date",
      example = "2026-07-05",
      requiredMode = Schema.RequiredMode.REQUIRED,
      type = "string",
      format = "date")
  private LocalDate endDate;

  @NotNull(message = "{validation.announcement.status.required}") @Schema(
      description = "Announcement status",
      example = "PUBLISHED",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private AnnouncementStatus status;

  @Size(max = 500, message = "{validation.announcement.actionUrl.size}")
  @Schema(
      description = "Call-to-action URL",
      example = "https://example.com/campaign/summer-updated",
      maxLength = 500)
  private String actionUrl;
}
