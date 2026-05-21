package com.snb.ms.rbac.role;

import com.snb.ms.shared.request.RequestContextAccessor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RequestContextAccessor contextAccessor;

    @Transactional(readOnly = true)
    public List<RoleResponse> findAll() {
        log.debug("Fetching all active roles");
        List<RoleResponse> roles = roleMapper.toDtoList(roleRepository.findAllActive());
        log.info("Fetched {} roles", roles.size());
        return roles;
    }

    @Transactional(readOnly = true)
    public Optional<RoleResponse> findByCode(String roleCode) {
        log.debug("Fetching role by code={}", roleCode);
        Optional<RoleResponse> result = roleRepository.findActiveByRoleCode(roleCode).map(roleMapper::toDto);
        log.info("Role lookup code={} found={}", roleCode, result.isPresent());
        return result;
    }

    @Transactional
    public RoleResponse create(RoleCreateRequest request) {
        log.debug("Creating role roleCode={}", request.getRoleCode());
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Role role = roleMapper.toEntity(request);
        role.setCreatedAt(now);
        role.setCreatedBy(callerId);
        role.setDeletedFlag("N");
        role.setVersionNumber(0L);
        RoleResponse created = roleMapper.toDto(roleRepository.save(role));
        log.info("Created role id={} roleCode={}", created.getRoleId(), created.getRoleCode());
        return created;
    }

    @Transactional
    public List<RoleResponse> createBulk(RoleBulkCreateRequest request) {
        log.debug("Creating roles in bulk count={}", request.getRoles().size());
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();

        List<Role> roles = request.getRoles().stream().map(roleRequest -> {
            Role role = roleMapper.toEntity(roleRequest);
            role.setCreatedAt(now);
            role.setCreatedBy(callerId);
            role.setDeletedFlag("N");
            role.setVersionNumber(0L);
            return role;
        }).collect(Collectors.toList());

        List<RoleResponse> created = roleMapper.toDtoList(roleRepository.saveAll(roles));
        log.info("Created {} roles in bulk", created.size());
        return created;
    }

    @Transactional
    public Optional<RoleResponse> update(String roleCode, RoleUpdateRequest request) {
        log.debug("Updating role code={}", roleCode);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<RoleResponse> updated = roleRepository.findActiveByRoleCode(roleCode).map(existing -> {
            roleMapper.updateEntity(request, existing);
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return roleMapper.toDto(roleRepository.save(existing));
        });
        log.info("Role update code={} success={}", roleCode, updated.isPresent());
        return updated;
    }

    @Transactional
    public Optional<RoleResponse> softDelete(String roleCode) {
        log.debug("Soft-deleting role code={}", roleCode);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<RoleResponse> deleted = roleRepository.findActiveByRoleCode(roleCode).map(existing -> {
            existing.setDeletedFlag("Y");
            existing.setDeletedAt(now);
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return roleMapper.toDto(roleRepository.save(existing));
        });
        log.info("Role soft-delete code={} success={}", roleCode, deleted.isPresent());
        return deleted;
    }
}
