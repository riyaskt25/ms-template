package com.snb.ms.announcement;

import com.snb.ms.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AnnouncementController implements AnnouncementApi {

  private final AnnouncementService announcementService;

  @Override
  @GetMapping
  public AnnouncementPageResponse findAll(@Valid @ModelAttribute AnnouncementListQuery query) {
    Page<AnnouncementResponse> announcements = announcementService.findAll(query);
    return AnnouncementPageResponse.fromOffsetPage(announcements);
  }

  @Override
  @GetMapping("/{id}")
  public AnnouncementResponse findById(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id) {
    return announcementService
        .findById(id)
        .orElseThrow(() -> ResourceNotFoundException.announcementById(id));
  }

  @Override
  @PostMapping
  public ResponseEntity<AnnouncementResponse> create(
      @Valid @RequestBody AnnouncementCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(announcementService.create(request));
  }

  @Override
  @PutMapping("/{id}")
  public AnnouncementResponse update(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id,
      @Valid @RequestBody AnnouncementUpdateRequest request) {
    return announcementService
        .update(id, request)
        .orElseThrow(() -> ResourceNotFoundException.announcementById(id));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> softDelete(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id) {
    announcementService
        .softDelete(id)
        .orElseThrow(() -> ResourceNotFoundException.announcementById(id));
    return ResponseEntity.noContent().build();
  }
}
