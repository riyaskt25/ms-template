package com.snb.ms.rbac.roleprivilege;

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
@RequestMapping("/api/roles/{roleCode}/privileges")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RolePrivilegeController implements RolePrivilegeApi {

    private final RolePrivilegeService rolePrivilegeService;

    @Override
    @GetMapping
    public List<RolePrivilegeResponse> findByRoleCode(@PathVariable String roleCode) {
        log.debug("Received request to fetch privileges for roleCode={}", roleCode);
        return rolePrivilegeService.findByRoleCode(roleCode);
    }

    @Override
    @PostMapping
    public ResponseEntity<RolePrivilegeResponse> grant(@PathVariable String roleCode, @Valid @RequestBody RolePrivilegeRequest request) {
        log.debug("Received request to grant privilegeCode={} to roleCode={}", request.getPrivilegeCode(), roleCode);
        RolePrivilegeResponse created = rolePrivilegeService.grant(roleCode, request);
        log.info("Granted privilegeCode={} to roleCode={}", request.getPrivilegeCode(), roleCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PostMapping("/bulk")
    public ResponseEntity<List<RolePrivilegeResponse>> grantBulk(@PathVariable String roleCode, @Valid @RequestBody RolePrivilegeBulkRequest request) {
        log.debug("Received request to bulk grant {} privileges to roleCode={}", request.getPrivilegeCodes().size(), roleCode);
        List<RolePrivilegeResponse> created = rolePrivilegeService.grantBulk(roleCode, request);
        log.info("Bulk granted {} privileges to roleCode={}", created.size(), roleCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @DeleteMapping("/{privilegeCode}")
    public RolePrivilegeResponse revoke(@PathVariable String roleCode, @PathVariable String privilegeCode) {
        log.debug("Received request to revoke privilegeCode={} for roleCode={}", privilegeCode, roleCode);
        RolePrivilegeResponse revoked = rolePrivilegeService.revoke(roleCode, privilegeCode)
            .orElseThrow(() -> ResourceNotFoundException.rolePrivilegeByRoleCodeAndPrivilegeCode(roleCode, privilegeCode));
        log.info("Revoked privilegeCode={} for roleCode={}", privilegeCode, roleCode);
        return revoked;
    }
}
