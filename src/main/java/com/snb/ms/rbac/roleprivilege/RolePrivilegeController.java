package com.snb.ms.rbac.roleprivilege;

import com.snb.ms.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles/{roleCode}/privileges")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RolePrivilegeController implements RolePrivilegeApi {

  private final RolePrivilegeService rolePrivilegeService;

  @Override
  @GetMapping
  public RolePrivilegesAggregateResponse findByRoleCode(@PathVariable String roleCode) {
    log.debug("Received request to fetch privileges for roleCode={}", roleCode);
    return rolePrivilegeService.findByRoleCode(roleCode);
  }

  @Override
  @PostMapping
  public ResponseEntity<RolePrivilegesAggregateResponse> grant(
      @PathVariable String roleCode, @Valid @RequestBody RolePrivilegeRequest request) {
    log.debug(
        "Received request to grant privilegeCode={} to roleCode={}",
        request.getPrivilegeCode(),
        roleCode);
    RolePrivilegesAggregateResponse created = rolePrivilegeService.grant(roleCode, request);
    log.info("Granted privilegeCode={} to roleCode={}", request.getPrivilegeCode(), roleCode);
    return ResponseEntity.ok(created);
  }

  @Override
  @PostMapping("/bulk")
  public ResponseEntity<RolePrivilegesAggregateResponse> grantBulk(
      @PathVariable String roleCode, @Valid @RequestBody RolePrivilegeBulkRequest request) {
    log.debug(
        "Received request to bulk grant {} privileges to roleCode={}",
        request.getPrivilegeCodes().size(),
        roleCode);
    RolePrivilegesAggregateResponse created = rolePrivilegeService.grantBulk(roleCode, request);
    log.info(
        "Bulk granted privileges to roleCode={} now total={}",
        roleCode,
        created.getPrivileges().size());
    return ResponseEntity.ok(created);
  }

  @Override
  @DeleteMapping("/{privilegeCode}")
  public ResponseEntity<Void> revoke(
      @PathVariable String roleCode, @PathVariable String privilegeCode) {
    log.debug(
        "Received request to revoke privilegeCode={} for roleCode={}", privilegeCode, roleCode);
    rolePrivilegeService
        .revoke(roleCode, privilegeCode)
        .orElseThrow(
            () ->
                ResourceNotFoundException.rolePrivilegeByRoleCodeAndPrivilegeCode(
                    roleCode, privilegeCode));
    log.info("Revoked privilegeCode={} for roleCode={}", privilegeCode, roleCode);
    return ResponseEntity.noContent().build();
  }
}
