package com.snb.ms.tier;

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
@RequestMapping("/api/tiers")
@RequiredArgsConstructor
@Validated
@Slf4j
public class TierController implements TierApi {

  private final TierService tierService;

  @Override
  @GetMapping
  public TierPageResponse findAll(@Valid @ModelAttribute TierListQuery query) {
    Page<TierResponse> tiers = tierService.findAll(query);
    return TierPageResponse.fromOffsetPage(tiers);
  }

  @Override
  @GetMapping("/{id}")
  public TierResponse findById(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id) {
    return tierService.findById(id).orElseThrow(() -> ResourceNotFoundException.tierById(id));
  }

  @Override
  @PostMapping
  public ResponseEntity<TierResponse> create(@Valid @RequestBody TierCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(tierService.create(request));
  }

  @Override
  @PutMapping("/{id}")
  public TierResponse update(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id,
      @Valid @RequestBody TierUpdateRequest request) {
    return tierService
        .update(id, request)
        .orElseThrow(() -> ResourceNotFoundException.tierById(id));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> softDelete(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id) {
    tierService.softDelete(id).orElseThrow(() -> ResourceNotFoundException.tierById(id));
    return ResponseEntity.noContent().build();
  }
}
