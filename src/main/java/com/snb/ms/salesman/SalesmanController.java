package com.snb.ms.salesman;

import com.snb.ms.salesman.SalesmanResponse;
import com.snb.ms.salesman.SalesmanCreateRequest;
import com.snb.ms.salesman.SalesmanUpdateRequest;
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
@Slf4j
public class SalesmanController implements SalesmanApi {

    private final SalesmanService salesmanService;

    @Override
    @GetMapping
    public ResponseEntity<List<SalesmanResponse>> findAll() {
        log.debug("Received request to fetch all salesmen");
        List<SalesmanResponse> salesmen = salesmanService.findAll();
        log.info("Fetched {} salesmen", salesmen.size());
        return ResponseEntity.ok(salesmen);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SalesmanResponse> findById(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to fetch salesman by id={}", id);
        Optional<SalesmanResponse> result = salesmanService.findById(id);
        if (result.isPresent()) {
            log.info("Salesman found for id={}", id);
            return ResponseEntity.ok(result.get());
        }
        log.info("Salesman not found for id={}", id);
        return ResponseEntity.notFound().build();
    }

    @Override
    @PostMapping
    public ResponseEntity<SalesmanResponse> create(@Valid @RequestBody SalesmanCreateRequest request) {
        log.debug("Received request to create salesman for companyId={}", request.getCompanyId());
        SalesmanResponse created = salesmanService.create(request);
        log.info("Created salesman with id={}", created.getSalesmanId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<SalesmanResponse> update(@PathVariable @Positive(message = "id must be positive") Long id,
                                              @Valid @RequestBody SalesmanUpdateRequest request) {
        log.debug("Received request to update salesman id={}", id);
        Optional<SalesmanResponse> updated = salesmanService.update(id, request);
        if (updated.isPresent()) {
            log.info("Updated salesman id={}", id);
            return ResponseEntity.ok(updated.get());
        }
        log.info("Salesman not found for update id={}", id);
        return ResponseEntity.notFound().build();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<SalesmanResponse> softDelete(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to soft-delete salesman id={}", id);
        Optional<SalesmanResponse> deleted = salesmanService.softDelete(id);
        if (deleted.isPresent()) {
            log.info("Soft-deleted salesman id={}", id);
            return ResponseEntity.ok(deleted.get());
        }
        log.info("Salesman not found for soft-delete id={}", id);
        return ResponseEntity.notFound().build();
    }
}
