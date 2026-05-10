package com.snb.ms.company;

import com.snb.ms.company.CompanyResponse;
import com.snb.ms.company.CompanyCreateRequest;
import com.snb.ms.company.CompanyUpdateRequest;
import com.snb.ms.exception.ResourceNotFoundException;
import com.snb.ms.company.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompanyController implements CompanyApi {

    private final CompanyService companyService;

    @Override
    @GetMapping
    public List<CompanyResponse> findAll() {
        log.debug("Received request to fetch all companies");
        List<CompanyResponse> companies = companyService.findAll();
        log.info("Fetched {} companies", companies.size());
        return companies;
    }

    @Override
    @GetMapping("/{id}")
    public CompanyResponse findById(@PathVariable Long id) {
        log.debug("Received request to fetch company by id={}", id);
        CompanyResponse result = companyService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("companyId=" + id));
        log.info("Company found for id={}", id);
        return result;
    }

    @Override
    @PostMapping
    public ResponseEntity<CompanyResponse> create(@RequestBody CompanyCreateRequest request) {
        log.debug("Received request to create company registrationNumber={}", request.getRegistrationNumber());
        CompanyResponse created = companyService.create(request);
        log.info("Created company with id={}", created.getCompanyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PutMapping("/{id}")
    public CompanyResponse update(@PathVariable Long id,
                                  @RequestBody CompanyUpdateRequest request) {
        log.debug("Received request to update company id={}", id);
        CompanyResponse updated = companyService.update(id, request)
            .orElseThrow(() -> new ResourceNotFoundException("companyId=" + id));
        log.info("Updated company id={}", id);
        return updated;
    }

    @Override
    @DeleteMapping("/{id}")
    public CompanyResponse softDelete(@PathVariable Long id) {
        log.debug("Received request to soft-delete company id={}", id);
        CompanyResponse deleted = companyService.softDelete(id)
            .orElseThrow(() -> new ResourceNotFoundException("companyId=" + id));
        log.info("Soft-deleted company id={}", id);
        return deleted;
    }
}
