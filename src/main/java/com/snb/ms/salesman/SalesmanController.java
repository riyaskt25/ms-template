package com.snb.ms.salesman;

import com.snb.ms.exception.ResourceNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salesmen")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SalesmanController implements SalesmanApi {

    private final SalesmanService salesmanService;

    @Override
    @GetMapping
    public List<SalesmanResponse> findAll() {
        log.debug("Received request to fetch all salesmen");
        List<SalesmanResponse> salesmen = salesmanService.findAll();
        log.info("Fetched {} salesmen", salesmen.size());
        return salesmen;
    }

    @Override
    @GetMapping("/{id}")
    public SalesmanResponse findById(@PathVariable Long id) {
        log.debug("Received request to fetch salesman by id={}", id);
        SalesmanResponse result = salesmanService.findById(id)
            .orElseThrow(() -> ResourceNotFoundException.salesmanById(id));
        log.info("Salesman found for id={}", id);
        return result;
    }

    @Override
    @PostMapping
    public ResponseEntity<SalesmanResponse> create(@RequestBody SalesmanCreateRequest request) {
        log.debug("Received request to create salesman for companyId={}", request.getCompanyId());
        SalesmanResponse created = salesmanService.create(request);
        log.info("Created salesman with id={}", created.getSalesmanId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PutMapping("/{id}")
    public SalesmanResponse update(@PathVariable Long id,
                                   @RequestBody SalesmanUpdateRequest request) {
        log.debug("Received request to update salesman id={}", id);
        SalesmanResponse updated = salesmanService.update(id, request)
            .orElseThrow(() -> ResourceNotFoundException.salesmanById(id));
        log.info("Updated salesman id={}", id);
        return updated;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        log.debug("Received request to soft-delete salesman id={}", id);
        salesmanService.softDelete(id)
            .orElseThrow(() -> ResourceNotFoundException.salesmanById(id));
        log.info("Soft-deleted salesman id={}", id);
        return ResponseEntity.noContent().build();
    }
}
