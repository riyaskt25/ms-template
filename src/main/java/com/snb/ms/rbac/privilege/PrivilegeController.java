package com.snb.ms.rbac.privilege;

import com.snb.ms.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rbac/privileges")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivilegeController implements PrivilegeApi {

    private final PrivilegeService privilegeService;

    @Override
    @GetMapping
    public List<PrivilegeResponse> findAll() {
        log.debug("Received request to fetch all privileges");
        List<PrivilegeResponse> privileges = privilegeService.findAll();
        log.info("Fetched {} privileges", privileges.size());
        return privileges;
    }

    @Override
    @GetMapping("/{id}")
    public PrivilegeResponse findById(@PathVariable Long id) {
        log.debug("Received request to fetch privilege by id={}", id);
        PrivilegeResponse result = privilegeService.findById(id)
            .orElseThrow(() -> ResourceNotFoundException.privilegeById(id));
        log.info("Privilege found for id={}", id);
        return result;
    }

    @Override
    @PostMapping
    public ResponseEntity<PrivilegeResponse> create(@RequestBody PrivilegeCreateRequest request) {
        log.debug("Received request to create privilege privilegeCode={}", request.getPrivilegeCode());
        PrivilegeResponse created = privilegeService.create(request);
        log.info("Created privilege id={}", created.getPrivilegeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PutMapping("/{id}")
    public PrivilegeResponse update(@PathVariable Long id, @RequestBody PrivilegeUpdateRequest request) {
        log.debug("Received request to update privilege id={}", id);
        PrivilegeResponse updated = privilegeService.update(id, request)
            .orElseThrow(() -> ResourceNotFoundException.privilegeById(id));
        log.info("Updated privilege id={}", id);
        return updated;
    }

    @Override
    @DeleteMapping("/{id}")
    public PrivilegeResponse softDelete(@PathVariable Long id) {
        log.debug("Received request to soft-delete privilege id={}", id);
        PrivilegeResponse deleted = privilegeService.softDelete(id)
            .orElseThrow(() -> ResourceNotFoundException.privilegeById(id));
        log.info("Soft-deleted privilege id={}", id);
        return deleted;
    }
}
