package com.snb.ms.rbac.roleprivilege;

import com.snb.ms.auth.authorization.Privileges;
import com.snb.ms.auth.authorization.RequirePrivilege;
import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.exception.ErrorCodeEnum;
import com.snb.ms.exception.ResourceNotFoundException;
import com.snb.ms.rbac.privilege.Privilege;
import com.snb.ms.rbac.privilege.PrivilegeRepository;
import com.snb.ms.rbac.role.Role;
import com.snb.ms.rbac.role.RoleRepository;
import com.snb.ms.shared.request.RequestContextAccessor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolePrivilegeService {

  private final RolePrivilegeRepository rolePrivilegeRepository;
  private final RolePrivilegeMapper rolePrivilegeMapper;
  private final RoleRepository roleRepository;
  private final PrivilegeRepository privilegeRepository;
  private final RequestContextAccessor contextAccessor;

  @Transactional(readOnly = true)
  @RequirePrivilege(Privileges.ROLE_MANAGE)
  public RolePrivilegesAggregateResponse findByRoleCode(String roleCode) {
    log.debug("Fetching privileges for roleCode={}", roleCode);
    Role role =
        roleRepository
            .findActiveByRoleCode(roleCode)
            .orElseThrow(() -> ResourceNotFoundException.roleByCode(roleCode));
    List<RolePrivilege> assignments = rolePrivilegeRepository.findActiveByRoleCode(roleCode);
    RolePrivilegesAggregateResponse response = toAggregateResponse(role, assignments);
    log.info("Fetched {} privileges for roleCode={}", response.getPrivileges().size(), roleCode);
    return response;
  }

  @Transactional
  @RequirePrivilege(Privileges.ROLE_MANAGE)
  public RolePrivilegesAggregateResponse grant(String roleCode, RolePrivilegeRequest request) {
    log.debug("Granting privilegeCode={} to roleCode={}", request.getPrivilegeCode(), roleCode);
    if (rolePrivilegeRepository.existsByRole_RoleCodeAndPrivilege_PrivilegeCodeAndDeletedFlag(
        roleCode, request.getPrivilegeCode(), "N")) {
      throw new BusinessValidationException(
          ErrorCodeEnum.CONFLICT,
          "error.rolePrivilege.alreadyExists",
          new Object[] {roleCode, request.getPrivilegeCode()},
          "Role already has this privilege granted");
    }
    Role role =
        roleRepository
            .findActiveByRoleCode(roleCode)
            .orElseThrow(() -> ResourceNotFoundException.roleByCode(roleCode));
    Privilege privilege =
        privilegeRepository
            .findActiveByPrivilegeCode(request.getPrivilegeCode())
            .orElseThrow(
                () -> ResourceNotFoundException.privilegeByCode(request.getPrivilegeCode()));

    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    RolePrivilege rolePrivilege = new RolePrivilege();
    rolePrivilege.setRole(role);
    rolePrivilege.setPrivilege(privilege);
    rolePrivilege.setCreatedAt(LocalDateTime.now());
    rolePrivilege.setCreatedBy(callerId);
    rolePrivilege.setDeletedFlag("N");
    rolePrivilege.setVersionNumber(0L);
    rolePrivilegeRepository.save(rolePrivilege);

    List<RolePrivilege> assignments = rolePrivilegeRepository.findActiveByRoleCode(roleCode);
    RolePrivilegesAggregateResponse response = toAggregateResponse(role, assignments);
    log.info(
        "Granted privilegeCode={} to roleCode={} now total={}",
        request.getPrivilegeCode(),
        roleCode,
        response.getPrivileges().size());
    return response;
  }

  @Transactional
  @RequirePrivilege(Privileges.ROLE_MANAGE)
  public RolePrivilegesAggregateResponse grantBulk(
      String roleCode, RolePrivilegeBulkRequest request) {
    Set<String> requestedPrivilegeCodes =
        request.getPrivilegeCodes().stream().collect(Collectors.toCollection(LinkedHashSet::new));
    log.debug("Bulk granting privilegeCodes={} to roleCode={}", requestedPrivilegeCodes, roleCode);

    Role role =
        roleRepository
            .findActiveByRoleCode(roleCode)
            .orElseThrow(() -> ResourceNotFoundException.roleByCode(roleCode));
    List<RolePrivilege> existingAssignments =
        rolePrivilegeRepository.findActiveByRoleCode(roleCode);
    Set<String> existingPrivilegeCodes =
        existingAssignments.stream()
            .map(assignment -> assignment.getPrivilege().getPrivilegeCode())
            .collect(Collectors.toSet());
    Set<String> duplicatePrivilegeCodes =
        requestedPrivilegeCodes.stream()
            .filter(existingPrivilegeCodes::contains)
            .collect(Collectors.toSet());
    if (!duplicatePrivilegeCodes.isEmpty()) {
      throw new BusinessValidationException(
          ErrorCodeEnum.CONFLICT,
          "error.rolePrivilege.alreadyExists",
          new Object[] {roleCode, duplicatePrivilegeCodes.stream().findFirst().orElse(null)},
          "Role already has this privilege granted");
    }

    Set<String> privilegeCodesToAdd =
        requestedPrivilegeCodes.stream()
            .filter(privilegeCode -> !existingPrivilegeCodes.contains(privilegeCode))
            .collect(Collectors.toCollection(LinkedHashSet::new));

    Map<String, Privilege> privilegeByCode =
        privilegeCodesToAdd.stream()
            .map(
                privilegeCode ->
                    privilegeRepository
                        .findActiveByPrivilegeCode(privilegeCode)
                        .orElseThrow(
                            () -> ResourceNotFoundException.privilegeByCode(privilegeCode)))
            .collect(Collectors.toMap(Privilege::getPrivilegeCode, Function.identity()));

    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    List<RolePrivilege> newAssignments =
        privilegeCodesToAdd.stream()
            .map(
                privilegeCode ->
                    newRolePrivilege(role, privilegeByCode.get(privilegeCode), callerId, now))
            .collect(Collectors.toList());

    rolePrivilegeRepository.saveAll(newAssignments);

    List<RolePrivilege> assignments = rolePrivilegeRepository.findActiveByRoleCode(roleCode);
    RolePrivilegesAggregateResponse response = toAggregateResponse(role, assignments);
    log.info(
        "Bulk granted {} privileges to roleCode={} now total={}",
        newAssignments.size(),
        roleCode,
        response.getPrivileges().size());
    return response;
  }

  @Transactional
  @RequirePrivilege(Privileges.ROLE_MANAGE)
  public Optional<RolePrivilegeResponse> revoke(String roleCode, String privilegeCode) {
    log.debug("Revoking privilegeCode={} for roleCode={}", privilegeCode, roleCode);
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    Optional<RolePrivilegeResponse> revoked =
        rolePrivilegeRepository
            .findActiveByRoleCodeAndPrivilegeCode(roleCode, privilegeCode)
            .map(
                existing -> {
                  existing.setDeletedFlag("Y");
                  existing.setDeletedAt(now);
                  existing.setUpdatedAt(now);
                  existing.setUpdatedBy(callerId);
                  existing.setVersionNumber(existing.getVersionNumber() + 1);
                  return rolePrivilegeMapper.toDto(rolePrivilegeRepository.save(existing));
                });
    log.info(
        "Role-privilege revoke roleCode={} privilegeCode={} success={}",
        roleCode,
        privilegeCode,
        revoked.isPresent());
    return revoked;
  }

  private RolePrivilege newRolePrivilege(
      Role role, Privilege privilege, Long callerId, LocalDateTime now) {
    RolePrivilege rolePrivilege = new RolePrivilege();
    rolePrivilege.setRole(role);
    rolePrivilege.setPrivilege(privilege);
    rolePrivilege.setCreatedAt(now);
    rolePrivilege.setCreatedBy(callerId);
    rolePrivilege.setDeletedFlag("N");
    rolePrivilege.setVersionNumber(0L);
    return rolePrivilege;
  }

  private RolePrivilegesAggregateResponse toAggregateResponse(
      Role role, List<RolePrivilege> assignments) {
    List<RoleAssociatedPrivilegeResponse> privileges =
        assignments.stream()
            .map(
                assignment -> {
                  Privilege privilege = assignment.getPrivilege();
                  return new RoleAssociatedPrivilegeResponse(
                      privilege.getPrivilegeId(),
                      privilege.getPrivilegeCode(),
                      privilege.getPrivilegeName(),
                      privilege.getDescription());
                })
            .collect(Collectors.toCollection(ArrayList::new));

    return new RolePrivilegesAggregateResponse(
        role.getRoleId(),
        role.getRoleCode(),
        role.getRoleName(),
        role.getDescription(),
        privileges);
  }
}
