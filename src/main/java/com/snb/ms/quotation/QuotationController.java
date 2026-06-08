package com.snb.ms.quotation;

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
@RequestMapping("/api/quotations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class QuotationController implements QuotationApi {

  private final QuotationService quotationService;

  @Override
  @GetMapping
  public QuotationPageResponse findAll(@Valid @ModelAttribute QuotationListQuery query) {
    Page<QuotationResponse> quotations = quotationService.findAll(query);
    return QuotationPageResponse.fromOffsetPage(quotations);
  }

  @Override
  @GetMapping("/{id}")
  public QuotationResponse findById(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id) {
    return quotationService
        .findById(id)
        .orElseThrow(() -> ResourceNotFoundException.quotationById(id));
  }

  @Override
  @PostMapping
  public ResponseEntity<QuotationResponse> create(
      @Valid @RequestBody QuotationCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(quotationService.create(request));
  }

  @Override
  @PutMapping("/{id}")
  public QuotationResponse update(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id,
      @Valid @RequestBody QuotationUpdateRequest request) {
    return quotationService
        .update(id, request)
        .orElseThrow(() -> ResourceNotFoundException.quotationById(id));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> softDelete(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id) {
    quotationService.softDelete(id).orElseThrow(() -> ResourceNotFoundException.quotationById(id));
    return ResponseEntity.noContent().build();
  }
}
