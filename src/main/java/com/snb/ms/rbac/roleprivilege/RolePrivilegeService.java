package com.snb.ms.rbac.roleprivilege;

import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.exception.ResourceNotFoundException;
import com.snb.ms.rbac.privilege.Privilege;
import com.snb.ms.rbac.privilege.PrivilegeRepository;
import com.snb.ms.rbac.role.Role;
import com.snb.ms.rbac.role.RoleRepository;
import com.snb.ms.shared.request.RequestContextAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public List<RolePrivilegeResponse> findAll() {
        log.debug("Fetching all active role-privilege grants");
        List<RolePrivilegeResponse> results = rolePrivilegeMapper.toDtoList(rolePrivilegeRepository.findAllActive());
        log.info("Fetched {} role-privilege grants", results.size());
        return results;
    }

    @Transactional(readOnly = true)
    public List<RolePrivilegeResponse> findByRoleId(Long roleId) {
        log.debug("Fetching privileges for roleId={}", roleId);
        List<RolePrivilegeResponse> results = rolePrivilegeMapper.toDtoList(rolePrivilegeRepository.findActiveByRoleId(roleId));
        log.info("Fetched {} privileges for roleId={}", results.size(), roleId);
        return results;
    }

    @Transactional(readOnly = true)
    public Optional<RolePrivilegeResponse> findById(Long id) {
        log.debug("Fetching role-privilege grant id={}", id);
        return rolePrivilegeRepository.findActiveById(id).map(rolePrivilegeMapper::toDto);
    }

    @Transactional
    public RolePrivilegeResponse grant(RolePrivilegeRequest request) {
        log.debug("Granting privilegeId={} to roleId={}", request.getPrivilegeId(), request.getRoleId());
        if (rolePrivilegeRepository.existsByRole_RoleIdAndPrivilege_PrivilegeIdAndDeletedFlag(
                request.getRoleId(), request.getPrivilegeId(), "N")) {
            throw new BusinessValidationException("error.rolePrivilege.alreadyExists", new Object[]{request.getRoleId(), request.getPrivilegeId()}, "Role already has this privilege granted");
        }
        Role role = roleRepository.findActiveById(request.getRoleId())
            .orElseThrow(() -> ResourceNotFoundException.roleById(request.getRoleId()));
        Privilege privilege = privilegeRepository.findActiveById(request.getPrivilegeId())
            .orElseThrow(() -> ResourceNotFoundException.privilegeById(request.getPrivilegeId()));

        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        RolePrivilege rolePrivilege = new RolePrivilege();
        rolePrivilege.setRole(role);
        rolePrivilege.setPrivilege(privilege);
        rolePrivilege.setCreatedAt(LocalDateTime.now());
        rolePrivilege.setCreatedBy(callerId);
        rolePrivilege.setDeletedFlag("N");
        rolePrivilege.setVersionNumber(0L);
        RolePrivilegeResponse created = rolePrivilegeMapper.toDto(rolePrivilegeRepository.save(rolePrivilege));
        log.info("Granted privilegeId={} to roleId={} grantId={}", request.getPrivilegeId(), request.getRoleId(), created.getId());
        return created;
    }

    @Transactional
    public Optional<RolePrivilegeResponse> revoke(Long id) {
        log.debug("Revoking role-privilege grant id={}", id);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<RolePrivilegeResponse> revoked = rolePrivilegeRepository.findActiveById(id).map(existing -> {
            existing.setDeletedFlag("Y");
            existing.setDeletedAt(now);
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return rolePrivilegeMapper.toDto(rolePrivilegeRepository.save(existing));
        });
        log.info("Role-privilege revoke id={} success={}", id, revoked.isPresent());
        return revoked;
    }
}
