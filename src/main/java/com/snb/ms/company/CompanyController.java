package com.snb.ms.company;

import com.snb.ms.company.CompanyDto;
import com.snb.ms.company.CompanyCreateRequest;
import com.snb.ms.company.CompanyUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Companies", description = "Operations for company resources")
@Slf4j
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyDto>> findAll() {
        log.debug("Received request to fetch all companies");
        List<CompanyDto> companies = companyService.findAll();
        log.info("Fetched {} companies", companies.size());
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> findById(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to fetch company by id={}", id);
        Optional<CompanyDto> result = companyService.findById(id);
        if (result.isPresent()) {
            log.info("Company found for id={}", id);
            return ResponseEntity.ok(result.get());
        }
        log.info("Company not found for id={}", id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create company with linked user")
    @ApiResponse(responseCode = "201", description = "Company created successfully")
    public ResponseEntity<CompanyDto> create(@Valid @RequestBody CompanyCreateRequest request) {
        log.debug("Received request to create company registrationNumber={}", request.getRegistrationNumber());
        CompanyDto created = companyService.create(request);
        log.info("Created company with id={}", created.getCompanyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> update(@PathVariable @Positive(message = "id must be positive") Long id,
                                             @Valid @RequestBody CompanyUpdateRequest request) {
        log.debug("Received request to update company id={}", id);
        Optional<CompanyDto> updated = companyService.update(id, request);
        if (updated.isPresent()) {
            log.info("Updated company id={}", id);
            return ResponseEntity.ok(updated.get());
        }
        log.info("Company not found for update id={}", id);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CompanyDto> softDelete(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to soft-delete company id={}", id);
        Optional<CompanyDto> deleted = companyService.softDelete(id);
        if (deleted.isPresent()) {
            log.info("Soft-deleted company id={}", id);
            return ResponseEntity.ok(deleted.get());
        }
        log.info("Company not found for soft-delete id={}", id);
        return ResponseEntity.notFound().build();
    }
}
