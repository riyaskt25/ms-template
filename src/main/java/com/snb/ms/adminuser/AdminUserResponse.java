package com.snb.ms.adminuser;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Admin user response payload")
public class AdminUserResponse {
  @Schema(description = "Unique admin user identifier", example = "1001")
  private Long adminUserId;

  @Schema(description = "First name", example = "Sara")
  private String firstName;

  @Schema(description = "Middle name", example = "M")
  private String middleName;

  @Schema(description = "Last name", example = "Naseer")
  private String lastName;

  @Schema(description = "Extension number", example = "EXT-1001")
  private String extensionNumber;

  @Schema(description = "Organization employee identifier", example = "SNB1001")
  private String snbId;

  @Schema(description = "Admin status", example = "ACTIVE")
  private String adminStatus;

  @Schema(description = "Identifier of actor who created the record", example = "1001")
  private Long createdBy;

  @Schema(
      description = "Record creation timestamp in ISO-8601 format",
      example = "2026-05-04T10:15:30")
  private LocalDateTime createdAt;

  @Schema(description = "Identifier of actor who last updated the record", example = "1002")
  private Long updatedBy;

  @Schema(description = "Last update timestamp in ISO-8601 format", example = "2026-05-04T11:20:45")
  private LocalDateTime updatedAt;
}
