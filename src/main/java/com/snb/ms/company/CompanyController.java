package com.snb.ms.company;

import com.snb.ms.company.CompanyResponse;
import com.snb.ms.company.CompanyCreateRequest;
import com.snb.ms.company.CompanyUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import com.snb.ms.company.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompanyController implements CompanyApi {

    private final CompanyService companyService;

    @Override
    @GetMapping
    public ResponseEntity<List<CompanyResponse>> findAll() {
        log.debug("Received request to fetch all companies");
        List<CompanyResponse> companies = companyService.findAll();
        log.info("Fetched {} companies", companies.size());
        return ResponseEntity.ok(companies);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> findById(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to fetch company by id={}", id);
        Optional<CompanyResponse> result = companyService.findById(id);
        if (result.isPresent()) {
            log.info("Company found for id={}", id);
            return ResponseEntity.ok(result.get());
        }
        log.info("Company not found for id={}", id);
        return ResponseEntity.notFound().build();
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
    public ResponseEntity<CompanyResponse> update(@PathVariable @Positive(message = "id must be positive") Long id,
                                             @Valid @RequestBody CompanyUpdateRequest request) {
        log.debug("Received request to update company id={}", id);
        Optional<CompanyResponse> updated = companyService.update(id, request);
        if (updated.isPresent()) {
            log.info("Updated company id={}", id);
            return ResponseEntity.ok(updated.get());
        }
        log.info("Company not found for update id={}", id);
        return ResponseEntity.notFound().build();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<CompanyResponse> softDelete(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to soft-delete company id={}", id);
        Optional<CompanyResponse> deleted = companyService.softDelete(id);
        if (deleted.isPresent()) {
            log.info("Soft-deleted company id={}", id);
            return ResponseEntity.ok(deleted.get());
        }
        log.info("Company not found for soft-delete id={}", id);
        return ResponseEntity.notFound().build();
    }
}
