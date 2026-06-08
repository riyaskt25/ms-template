package com.snb.ms.announcement;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Announcement response payload")
public class AnnouncementResponse {

  @Schema(description = "Unique announcement identifier", example = "1001")
  private Long announcementId;

  @Schema(description = "Announcement title", example = "Summer campaign starts now")
  private String title;

  @Schema(
      description = "Announcement description",
      example = "Enjoy exclusive offers during the summer campaign.")
  private String description;

  @Schema(
      description = "Announcement image URL",
      example = "https://cdn.example.com/announcements/summer.png")
  private String imageUrl;

  @Schema(description = "Announcement priority", example = "HIGH")
  private AnnouncementPriority priority;

  @Schema(description = "Announcement type", example = "CAMPAIGN")
  private AnnouncementType type;

  @Schema(
      description = "Announcement start date",
      example = "2026-06-01",
      type = "string",
      format = "date")
  private LocalDate startDate;

  @Schema(
      description = "Announcement end date",
      example = "2026-06-30",
      type = "string",
      format = "date")
  private LocalDate endDate;

  @Schema(description = "Announcement status", example = "DRAFT")
  private AnnouncementStatus status;

  @Schema(description = "Call-to-action URL", example = "https://example.com/campaign/summer")
  private String actionUrl;

  @Schema(description = "Identifier of actor who created the record", example = "1001")
  private Long createdBy;

  @Schema(
      description = "Record creation timestamp in ISO-8601 format",
      example = "2026-06-01T10:15:30")
  private LocalDateTime createdAt;

  @Schema(description = "Identifier of actor who last updated the record", example = "1002")
  private Long updatedBy;

  @Schema(description = "Last update timestamp in ISO-8601 format", example = "2026-06-05T11:20:45")
  private LocalDateTime updatedAt;
}
