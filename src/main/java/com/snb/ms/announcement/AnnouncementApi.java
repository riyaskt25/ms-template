package com.snb.ms.announcement;

import com.snb.ms.shared.BaseResponseDTO;
import com.snb.ms.shared.api.CommonApiParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "[031] - Announcements", description = "Operations for announcement resources")
public interface AnnouncementApi {

  @Operation(
      operationId = "getAllAnnouncements",
      summary = "List announcements",
      description = "Returns active announcements using offset-based pagination.")
  @CommonApiParameters
  @Parameters({
    @Parameter(name = "page", description = "Zero-based page index", example = "0"),
    @Parameter(name = "size", description = "Page size", example = "20"),
    @Parameter(name = "sortBy", description = "Sort fields", example = "createdAt"),
    @Parameter(name = "sortDirection", description = "Sort direction", example = "DESC")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Announcements fetched successfully",
        content = @Content(schema = @Schema(implementation = AnnouncementPageResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  AnnouncementPageResponse findAll(@Valid @ParameterObject AnnouncementListQuery query);

  @Operation(
      operationId = "getAnnouncementById",
      summary = "Get announcement by id",
      description = "Finds a single announcement by its identifier.")
  @CommonApiParameters
  @Parameters({
    @Parameter(
        name = "id",
        description = "Announcement identifier",
        required = true,
        example = "1001")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Announcement fetched successfully",
        content = @Content(schema = @Schema(implementation = AnnouncementResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Announcement not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  AnnouncementResponse findById(@Positive(message = "{validation.common.id.positive}") Long id);

  @Operation(
      operationId = "createAnnouncement",
      summary = "Create announcement",
      description = "Creates a new announcement record.")
  @CommonApiParameters
  @RequestBody(
      required = true,
      description = "Announcement payload to create",
      content = @Content(schema = @Schema(implementation = AnnouncementCreateRequest.class)))
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Announcement created successfully",
        content = @Content(schema = @Schema(implementation = AnnouncementResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Validation failed",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  ResponseEntity<AnnouncementResponse> create(@Valid AnnouncementCreateRequest request);

  @Operation(
      operationId = "updateAnnouncement",
      summary = "Update announcement",
      description = "Updates an existing announcement record by identifier.")
  @CommonApiParameters
  @Parameters({
    @Parameter(
        name = "id",
        description = "Announcement identifier",
        required = true,
        example = "1001")
  })
  @RequestBody(
      required = true,
      description = "Announcement payload to update",
      content = @Content(schema = @Schema(implementation = AnnouncementUpdateRequest.class)))
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Announcement updated successfully",
        content = @Content(schema = @Schema(implementation = AnnouncementResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Announcement not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  AnnouncementResponse update(
      @Positive(message = "{validation.common.id.positive}") Long id,
      @Valid AnnouncementUpdateRequest request);

  @Operation(
      operationId = "softDeleteAnnouncement",
      summary = "Soft delete announcement",
      description = "Marks an announcement as deleted.")
  @CommonApiParameters
  @Parameters({
    @Parameter(
        name = "id",
        description = "Announcement identifier",
        required = true,
        example = "1001")
  })
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Announcement soft-deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "Announcement not found",
        content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))
  })
  ResponseEntity<Void> softDelete(@Positive(message = "{validation.common.id.positive}") Long id);
}
