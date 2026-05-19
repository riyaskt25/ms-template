package com.snb.ms.rbac.privilege;

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
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeMapper privilegeMapper;
    private final RequestContextAccessor contextAccessor;

    @Transactional(readOnly = true)
    public List<PrivilegeResponse> findAll() {
        log.debug("Fetching all active privileges");
        List<PrivilegeResponse> privileges = privilegeMapper.toDtoList(privilegeRepository.findAllActive());
        log.info("Fetched {} privileges", privileges.size());
        return privileges;
    }

    @Transactional(readOnly = true)
    public Optional<PrivilegeResponse> findById(Long id) {
        log.debug("Fetching privilege by id={}", id);
        Optional<PrivilegeResponse> result = privilegeRepository.findActiveById(id).map(privilegeMapper::toDto);
        log.info("Privilege lookup id={} found={}", id, result.isPresent());
        return result;
    }

    @Transactional
    public PrivilegeResponse create(PrivilegeCreateRequest request) {
        log.debug("Creating privilege privilegeCode={}", request.getPrivilegeCode());
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        Privilege privilege = privilegeMapper.toEntity(request);
        privilege.setCreatedAt(LocalDateTime.now());
        privilege.setCreatedBy(callerId);
        privilege.setDeletedFlag("N");
        privilege.setVersionNumber(0L);
        PrivilegeResponse created = privilegeMapper.toDto(privilegeRepository.save(privilege));
        log.info("Created privilege id={} privilegeCode={}", created.getPrivilegeId(), created.getPrivilegeCode());
        return created;
    }

    @Transactional
    public Optional<PrivilegeResponse> update(Long id, PrivilegeUpdateRequest request) {
        log.debug("Updating privilege id={}", id);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<PrivilegeResponse> updated = privilegeRepository.findActiveById(id).map(existing -> {
            privilegeMapper.updateEntity(request, existing);
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return privilegeMapper.toDto(privilegeRepository.save(existing));
        });
        log.info("Privilege update id={} success={}", id, updated.isPresent());
        return updated;
    }

    @Transactional
    public Optional<PrivilegeResponse> softDelete(Long id) {
        log.debug("Soft-deleting privilege id={}", id);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<PrivilegeResponse> deleted = privilegeRepository.findActiveById(id).map(existing -> {
            existing.setDeletedFlag("Y");
            existing.setDeletedAt(now);
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return privilegeMapper.toDto(privilegeRepository.save(existing));
        });
        log.info("Privilege soft-delete id={} success={}", id, deleted.isPresent());
        return deleted;
    }
}
