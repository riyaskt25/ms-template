package com.snb.ms.rbac.roleprivilege;

import com.snb.ms.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rbac/role-privileges")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RolePrivilegeController implements RolePrivilegeApi {

    private final RolePrivilegeService rolePrivilegeService;

    @Override
    @GetMapping
    public List<RolePrivilegeResponse> findAll() {
        log.debug("Received request to fetch all role-privilege grants");
        return rolePrivilegeService.findAll();
    }

    @Override
    @GetMapping("/role/{roleId}")
    public List<RolePrivilegeResponse> findByRoleId(@PathVariable Long roleId) {
        log.debug("Received request to fetch privileges for roleId={}", roleId);
        return rolePrivilegeService.findByRoleId(roleId);
    }

    @Override
    @PostMapping
    public ResponseEntity<RolePrivilegeResponse> grant(@RequestBody RolePrivilegeRequest request) {
        log.debug("Received request to grant privilegeId={} to roleId={}", request.getPrivilegeId(), request.getRoleId());
        RolePrivilegeResponse created = rolePrivilegeService.grant(request);
        log.info("Granted privilegeId={} to roleId={}", request.getPrivilegeId(), request.getRoleId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @DeleteMapping("/{id}")
    public RolePrivilegeResponse revoke(@PathVariable Long id) {
        log.debug("Received request to revoke role-privilege grant id={}", id);
        RolePrivilegeResponse revoked = rolePrivilegeService.revoke(id)
            .orElseThrow(() -> ResourceNotFoundException.rolePrivilegeById(id));
        log.info("Revoked role-privilege grant id={}", id);
        return revoked;
    }
}
