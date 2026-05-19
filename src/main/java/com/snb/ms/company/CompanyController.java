package com.snb.ms.company;

import com.snb.ms.exception.ResourceNotFoundException;
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
    public CompanyPageResponse findAll(@Valid @ModelAttribute CompanyOffsetListQuery query) {
        log.debug("Received request to fetch companies (offset pagination): page={}, size={}, sortBy={}, sortDirection={}, includeSalesmen={}",
            query.getPage(), query.getSize(), query.getSortBy(), query.getSortDirection(), query.getIncludeSalesmen());

        Page<CompanyResponse> companies = companyService.findAll(query);
        log.info("Fetched companies: page={}, size={}, returned={}, total={}",
            companies.getNumber(), companies.getSize(), companies.getNumberOfElements(), companies.getTotalElements());
        return CompanyPageResponse.fromOffsetPage(companies);
    }

    @GetMapping("/lazy")
    public CompanyPageResponse findAllLazy(@Valid @ModelAttribute CompanyListQuery query) {
        log.debug("Received request to fetch companies (cursor lazy loading): cursor={}, limit={}, sortBy={}, sortDirection={}, includeSalesmen={}",
            query.getCursor(), query.getLimit(), query.getSortBy(), query.getSortDirection(), query.getIncludeSalesmen());

        CompanyService.CompanyCursorSlice companies = companyService.findAllLazy(query);
        log.info("Fetched companies (cursor mode): limit={}, returned={}, hasNext={}, nextCursor={}",
            companies.getLimit(), companies.getData().size(), companies.isHasNext(), companies.getNextCursor());
        return CompanyPageResponse.fromCursor(
            companies.getData(),
            companies.getLimit(),
            companies.isHasNext(),
            companies.getNextCursor()
        );
    }

    @Override
    @GetMapping("/{id}")
    public CompanyResponse findById(@Positive(message = "{validation.common.id.positive}") @PathVariable Long id,
                                    @RequestParam(defaultValue = "false") Boolean includeSalesmen) {
        log.debug("Received request to fetch company by id={} includeSalesmen={}", id, includeSalesmen);
        CompanyResponse result = companyService.findById(id, includeSalesmen)
            .orElseThrow(() -> ResourceNotFoundException.companyById(id));
        log.info("Company found for id={}", id);
        return result;
    }

    @Override
    @PostMapping
    public ResponseEntity<CompanyWriteResponse> create(@Valid @RequestBody CompanyCreateRequest request) {
        log.debug("Received request to create company registrationNumber={}", request.getRegistrationNumber());
        CompanyWriteResponse created = companyService.create(request);
        log.info("Created company with id={}", created.getCompanyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PutMapping("/{id}")
    public CompanyWriteResponse update(@Positive(message = "{validation.common.id.positive}") @PathVariable Long id,
                                       @Valid @RequestBody CompanyUpdateRequest request) {
        log.debug("Received request to update company id={}", id);
        CompanyWriteResponse updated = companyService.update(id, request)
            .orElseThrow(() -> ResourceNotFoundException.companyById(id));
        log.info("Updated company id={}", id);
        return updated;
    }

    @Override
    @DeleteMapping("/{id}")
    public CompanyWriteResponse softDelete(@Positive(message = "{validation.common.id.positive}") @PathVariable Long id) {
        log.debug("Received request to soft-delete company id={}", id);
        CompanyWriteResponse deleted = companyService.softDelete(id)
            .orElseThrow(() -> ResourceNotFoundException.companyById(id));
        log.info("Soft-deleted company id={}", id);
        return deleted;
    }

    @Override
    @PatchMapping("/{id}/status")
    public CompanyWriteResponse decideStatus(@Positive(message = "{validation.common.id.positive}") @PathVariable Long id,
                                             @Valid @RequestBody CompanyStatusDecisionRequest request) {
        log.debug("Received status decision for company id={} targetStatus={}", id, request.getStatus());
        CompanyWriteResponse updated = companyService.decideStatus(id, request)
            .orElseThrow(() -> ResourceNotFoundException.companyById(id));
        log.info("Updated company status id={} to={}", id, updated.getCompanyStatus());
        return updated;
    }
}
