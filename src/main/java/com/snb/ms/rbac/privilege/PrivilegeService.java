package com.snb.ms.rbac.privilege;

import com.snb.ms.auth.authorization.Privileges;
import com.snb.ms.auth.authorization.RequirePrivilege;
import com.snb.ms.shared.request.RequestContextAccessor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivilegeService {

  private final PrivilegeRepository privilegeRepository;
  private final PrivilegeMapper privilegeMapper;
  private final RequestContextAccessor contextAccessor;

  @Transactional(readOnly = true)
  @RequirePrivilege(Privileges.ROLE_MANAGE)
  public List<PrivilegeResponse> findAll() {
    log.debug("Fetching all active privileges");
    List<PrivilegeResponse> privileges =
        privilegeMapper.toDtoList(privilegeRepository.findAllActive());
    log.info("Fetched {} privileges", privileges.size());
    return privileges;
  }

  @Transactional(readOnly = true)
  @RequirePrivilege(Privileges.ROLE_MANAGE)
  public Optional<PrivilegeResponse> findByCode(String privilegeCode) {
    log.debug("Fetching privilege by code={}", privilegeCode);
    Optional<PrivilegeResponse> result =
        privilegeRepository.findActiveByPrivilegeCode(privilegeCode).map(privilegeMapper::toDto);
    log.info("Privilege lookup code={} found={}", privilegeCode, result.isPresent());
    return result;
  }

  @Transactional
  @RequirePrivilege(Privileges.ROLE_MANAGE)
  public PrivilegeResponse create(PrivilegeCreateRequest request) {
    log.debug("Creating privilege privilegeCode={}", request.getPrivilegeCode());
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    Privilege privilege = privilegeMapper.toEntity(request);
    privilege.setCreatedAt(LocalDateTime.now());
    privilege.setCreatedBy(callerId);
    privilege.setDeletedFlag("N");
    privilege.setVersionNumber(0L);
    PrivilegeResponse created = privilegeMapper.toDto(privilegeRepository.save(privilege));
    log.info(
        "Created privilege id={} privilegeCode={}",
        created.getPrivilegeId(),
        created.getPrivilegeCode());
    return created;
  }

  @Transactional
  @RequirePrivilege(Privileges.ROLE_MANAGE)
  public Optional<PrivilegeResponse> update(String privilegeCode, PrivilegeUpdateRequest request) {
    log.debug("Updating privilege code={}", privilegeCode);
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    Optional<PrivilegeResponse> updated =
        privilegeRepository
            .findActiveByPrivilegeCode(privilegeCode)
            .map(
                existing -> {
                  privilegeMapper.updateEntity(request, existing);
                  existing.setUpdatedAt(now);
                  existing.setUpdatedBy(callerId);
                  existing.setVersionNumber(existing.getVersionNumber() + 1);
                  return privilegeMapper.toDto(privilegeRepository.save(existing));
                });
    log.info("Privilege update code={} success={}", privilegeCode, updated.isPresent());
    return updated;
  }

  @Transactional
  @RequirePrivilege(Privileges.ROLE_MANAGE)
  public Optional<PrivilegeResponse> softDelete(String privilegeCode) {
    log.debug("Soft-deleting privilege code={}", privilegeCode);
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    Optional<PrivilegeResponse> deleted =
        privilegeRepository
            .findActiveByPrivilegeCode(privilegeCode)
            .map(
                existing -> {
                  existing.setDeletedFlag("Y");
                  existing.setDeletedAt(now);
                  existing.setUpdatedAt(now);
                  existing.setUpdatedBy(callerId);
                  existing.setVersionNumber(existing.getVersionNumber() + 1);
                  return privilegeMapper.toDto(privilegeRepository.save(existing));
                });
    log.info("Privilege soft-delete code={} success={}", privilegeCode, deleted.isPresent());
    return deleted;
  }
}
