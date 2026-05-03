package com.snb.ms.salesman;

import com.snb.ms.salesman.SalesmanDto;
import com.snb.ms.salesman.SalesmanCreateRequest;
import com.snb.ms.salesman.SalesmanUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import com.snb.ms.salesman.SalesmanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/salesmen")
@RequiredArgsConstructor
@Validated
@Tag(name = "Salesmen", description = "Operations for salesman resources")
@Slf4j
public class SalesmanController {

    private final SalesmanService salesmanService;

    @GetMapping
    public ResponseEntity<List<SalesmanDto>> findAll() {
        log.debug("Received request to fetch all salesmen");
        List<SalesmanDto> salesmen = salesmanService.findAll();
        log.info("Fetched {} salesmen", salesmen.size());
        return ResponseEntity.ok(salesmen);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesmanDto> findById(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to fetch salesman by id={}", id);
        Optional<SalesmanDto> result = salesmanService.findById(id);
        if (result.isPresent()) {
            log.info("Salesman found for id={}", id);
            return ResponseEntity.ok(result.get());
        }
        log.info("Salesman not found for id={}", id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create salesman with linked user and company association")
    @ApiResponse(responseCode = "201", description = "Salesman created successfully")
    public ResponseEntity<SalesmanDto> create(@Valid @RequestBody SalesmanCreateRequest request) {
        log.debug("Received request to create salesman for companyId={}", request.getCompanyId());
        SalesmanDto created = salesmanService.create(request);
        log.info("Created salesman with id={}", created.getSalesmanId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesmanDto> update(@PathVariable @Positive(message = "id must be positive") Long id,
                                              @Valid @RequestBody SalesmanUpdateRequest request) {
        log.debug("Received request to update salesman id={}", id);
        Optional<SalesmanDto> updated = salesmanService.update(id, request);
        if (updated.isPresent()) {
            log.info("Updated salesman id={}", id);
            return ResponseEntity.ok(updated.get());
        }
        log.info("Salesman not found for update id={}", id);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SalesmanDto> softDelete(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to soft-delete salesman id={}", id);
        Optional<SalesmanDto> deleted = salesmanService.softDelete(id);
        if (deleted.isPresent()) {
            log.info("Soft-deleted salesman id={}", id);
            return ResponseEntity.ok(deleted.get());
        }
        log.info("Salesman not found for soft-delete id={}", id);
        return ResponseEntity.notFound().build();
    }
}
