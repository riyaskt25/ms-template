package com.snb.ms.rbac.role;

import com.snb.ms.exception.ResourceNotFoundException;
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
    @GetMapping("/{id}")
    public RoleResponse findById(@PathVariable Long id) {
        log.debug("Received request to fetch role by id={}", id);
        RoleResponse result = roleService.findById(id)
            .orElseThrow(() -> ResourceNotFoundException.roleById(id));
        log.info("Role found for id={}", id);
        return result;
    }

    @Override
    @PostMapping
    public ResponseEntity<RoleResponse> create(@RequestBody RoleCreateRequest request) {
        log.debug("Received request to create role roleCode={}", request.getRoleCode());
        RoleResponse created = roleService.create(request);
        log.info("Created role id={}", created.getRoleId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PutMapping("/{id}")
    public RoleResponse update(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        log.debug("Received request to update role id={}", id);
        RoleResponse updated = roleService.update(id, request)
            .orElseThrow(() -> ResourceNotFoundException.roleById(id));
        log.info("Updated role id={}", id);
        return updated;
    }

    @Override
    @DeleteMapping("/{id}")
    public RoleResponse softDelete(@PathVariable Long id) {
        log.debug("Received request to soft-delete role id={}", id);
        RoleResponse deleted = roleService.softDelete(id)
            .orElseThrow(() -> ResourceNotFoundException.roleById(id));
        log.info("Soft-deleted role id={}", id);
        return deleted;
    }
}
