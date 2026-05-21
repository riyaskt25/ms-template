package com.snb.ms.rbac.role;

import com.snb.ms.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rbac/roles")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RoleController implements RoleApi {

    private final RoleService roleService;

    @Override
    @GetMapping
    public List<RoleResponse> findAll() {
        log.debug("Received request to fetch all roles");
        List<RoleResponse> roles = roleService.findAll();
        log.info("Fetched {} roles", roles.size());
        return roles;
    }

    @Override
    @GetMapping("/{roleCode}")
    public RoleResponse findByCode(@PathVariable String roleCode) {
        log.debug("Received request to fetch role by code={}", roleCode);
        RoleResponse result = roleService.findByCode(roleCode)
            .orElseThrow(() -> ResourceNotFoundException.roleByCode(roleCode));
        log.info("Role found for code={}", roleCode);
        return result;
    }

    @Override
    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleCreateRequest request) {
        log.debug("Received request to create role roleCode={}", request.getRoleCode());
        RoleResponse created = roleService.create(request);
        log.info("Created role id={}", created.getRoleId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PostMapping("/bulk")
    public ResponseEntity<RoleBulkResponse> createBulk(@Valid @RequestBody RoleBulkCreateRequest request) {
        log.debug("Received request to bulk create {} roles", request.getRoles().size());
        List<RoleResponse> created = roleService.createBulk(request);
        log.info("Created {} roles in bulk request", created.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(new RoleBulkResponse(created));
    }

    @Override
    @PutMapping("/{roleCode}")
    public RoleResponse update(@PathVariable String roleCode, @RequestBody RoleUpdateRequest request) {
        log.debug("Received request to update role code={}", roleCode);
        RoleResponse updated = roleService.update(roleCode, request)
            .orElseThrow(() -> ResourceNotFoundException.roleByCode(roleCode));
        log.info("Updated role code={}", roleCode);
        return updated;
    }

    @Override
    @DeleteMapping("/{roleCode}")
    public RoleResponse softDelete(@PathVariable String roleCode) {
        log.debug("Received request to soft-delete role code={}", roleCode);
        RoleResponse deleted = roleService.softDelete(roleCode)
            .orElseThrow(() -> ResourceNotFoundException.roleByCode(roleCode));
        log.info("Soft-deleted role code={}", roleCode);
        return deleted;
    }
}
