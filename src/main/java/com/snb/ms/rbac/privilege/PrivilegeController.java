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
    @GetMapping("/{privilegeCode}")
    public PrivilegeResponse findByCode(@PathVariable String privilegeCode) {
        log.debug("Received request to fetch privilege by code={}", privilegeCode);
        PrivilegeResponse result = privilegeService.findByCode(privilegeCode)
            .orElseThrow(() -> ResourceNotFoundException.privilegeByCode(privilegeCode));
        log.info("Privilege found for code={}", privilegeCode);
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
    @PutMapping("/{privilegeCode}")
    public PrivilegeResponse update(@PathVariable String privilegeCode, @RequestBody PrivilegeUpdateRequest request) {
        log.debug("Received request to update privilege code={}", privilegeCode);
        PrivilegeResponse updated = privilegeService.update(privilegeCode, request)
            .orElseThrow(() -> ResourceNotFoundException.privilegeByCode(privilegeCode));
        log.info("Updated privilege code={}", privilegeCode);
        return updated;
    }

    @Override
    @DeleteMapping("/{privilegeCode}")
    public PrivilegeResponse softDelete(@PathVariable String privilegeCode) {
        log.debug("Received request to soft-delete privilege code={}", privilegeCode);
        PrivilegeResponse deleted = privilegeService.softDelete(privilegeCode)
            .orElseThrow(() -> ResourceNotFoundException.privilegeByCode(privilegeCode));
        log.info("Soft-deleted privilege code={}", privilegeCode);
        return deleted;
    }
}
