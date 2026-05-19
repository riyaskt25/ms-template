package com.snb.ms.rbac.role;

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
    public Optional<RoleResponse> findById(Long id) {
        log.debug("Fetching role by id={}", id);
        Optional<RoleResponse> result = roleRepository.findActiveById(id).map(roleMapper::toDto);
        log.info("Role lookup id={} found={}", id, result.isPresent());
        return result;
    }

    @Transactional
    public RoleResponse create(RoleCreateRequest request) {
        log.debug("Creating role roleCode={}", request.getRoleCode());
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        Role role = roleMapper.toEntity(request);
        role.setCreatedAt(LocalDateTime.now());
        role.setCreatedBy(callerId);
        role.setDeletedFlag("N");
        role.setVersionNumber(0L);
        RoleResponse created = roleMapper.toDto(roleRepository.save(role));
        log.info("Created role id={} roleCode={}", created.getRoleId(), created.getRoleCode());
        return created;
    }

    @Transactional
    public Optional<RoleResponse> update(Long id, RoleUpdateRequest request) {
        log.debug("Updating role id={}", id);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<RoleResponse> updated = roleRepository.findActiveById(id).map(existing -> {
            roleMapper.updateEntity(request, existing);
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return roleMapper.toDto(roleRepository.save(existing));
        });
        log.info("Role update id={} success={}", id, updated.isPresent());
        return updated;
    }

    @Transactional
    public Optional<RoleResponse> softDelete(Long id) {
        log.debug("Soft-deleting role id={}", id);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<RoleResponse> deleted = roleRepository.findActiveById(id).map(existing -> {
            existing.setDeletedFlag("Y");
            existing.setDeletedAt(now);
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return roleMapper.toDto(roleRepository.save(existing));
        });
        log.info("Role soft-delete id={} success={}", id, deleted.isPresent());
        return deleted;
    }
}
