package com.snb.ms.company;

import com.snb.ms.company.CompanyResponse;
import com.snb.ms.company.CompanyCreateRequest;
import com.snb.ms.company.CompanyStatusDecisionRequest;
import com.snb.ms.company.CompanyUpdateRequest;
import com.snb.ms.exception.ResourceNotFoundException;
import com.snb.ms.company.CompanyService;
import com.snb.ms.shared.PaginatedResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompanyController implements CompanyApi {

    private final CompanyService companyService;

    @Override
    @GetMapping
    public PaginatedResponseDTO<CompanyResponse> findAll(@Valid @ModelAttribute CompanyListQuery query) {
        log.debug("Received request to fetch companies: page={}, size={}, sortBy={}, sortDirection={}, includeSalesmen={}",
            query.getPage(), query.getSize(), query.getSortBy(), query.getSortDirection(), query.getIncludeSalesmen());
        Page<CompanyResponse> companies = companyService.findAll(query);
        log.info("Fetched companies: page={}, size={}, returned={}, total={}",
            companies.getNumber(), companies.getSize(), companies.getNumberOfElements(), companies.getTotalElements());
        return PaginatedResponseDTO.fromPage(companies);
    }

    @Override
    @GetMapping("/{id}")
    public CompanyResponse findById(@Positive(message = "{validation.common.id.positive}") @PathVariable Long id) {
        log.debug("Received request to fetch company by id={}", id);
        CompanyResponse result = companyService.findById(id)
            .orElseThrow(() -> ResourceNotFoundException.companyById(id));
        log.info("Company found for id={}", id);
        return result;
    }

    @Override
    @PostMapping
    public ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyCreateRequest request) {
        log.debug("Received request to create company registrationNumber={}", request.getRegistrationNumber());
        CompanyResponse created = companyService.create(request);
        log.info("Created company with id={}", created.getCompanyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PutMapping("/{id}")
    public CompanyResponse update(@Positive(message = "{validation.common.id.positive}") @PathVariable Long id,
                                  @Valid @RequestBody CompanyUpdateRequest request) {
        log.debug("Received request to update company id={}", id);
        CompanyResponse updated = companyService.update(id, request)
            .orElseThrow(() -> ResourceNotFoundException.companyById(id));
        log.info("Updated company id={}", id);
        return updated;
    }

    @Override
    @DeleteMapping("/{id}")
    public CompanyResponse softDelete(@Positive(message = "{validation.common.id.positive}") @PathVariable Long id) {
        log.debug("Received request to soft-delete company id={}", id);
        CompanyResponse deleted = companyService.softDelete(id)
            .orElseThrow(() -> ResourceNotFoundException.companyById(id));
        log.info("Soft-deleted company id={}", id);
        return deleted;
    }

    @Override
    @PatchMapping("/{id}/status")
    public CompanyResponse decideStatus(@Positive(message = "{validation.common.id.positive}") @PathVariable Long id,
                                        @Valid @RequestBody CompanyStatusDecisionRequest request) {
        log.debug("Received status decision for company id={} targetStatus={}", id, request.getStatus());
        CompanyResponse updated = companyService.decideStatus(id, request)
            .orElseThrow(() -> ResourceNotFoundException.companyById(id));
        log.info("Updated company status id={} to={}", id, updated.getCompanyStatus());
        return updated;
    }
}
