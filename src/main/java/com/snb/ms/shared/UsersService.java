package com.snb.ms.shared;

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
public class UsersService {

  private final UsersRepository usersRepository;
  private final UsersMapper usersMapper;
  private final RequestContextAccessor contextAccessor;

  @Transactional(readOnly = true)
  public List<UsersDto> findAll() {
    log.debug("Fetching all users");
    List<UsersDto> users = usersMapper.toDtoList(usersRepository.findAllActive());
    log.info("Fetched {} users", users.size());
    return users;
  }

  @Transactional(readOnly = true)
  public Optional<UsersDto> findById(Long id) {
    log.debug("Fetching user by id={}", id);
    Optional<UsersDto> result = usersRepository.findActiveById(id).map(usersMapper::toDto);
    log.info("User lookup id={} found={}", id, result.isPresent());
    return result;
  }

  @Transactional(readOnly = true)
  public Optional<UsersDto> findByEmail(String email) {
    log.debug("Fetching user by email={}", email);
    Optional<UsersDto> result =
        usersRepository.findActiveByEmailAddress(email).map(usersMapper::toDto);
    log.info("User lookup email={} found={}", email, result.isPresent());
    return result;
  }

  @Transactional
  public UsersDto create(UsersRequest request) {
    log.debug("Creating user email={}", request.getEmailAddress());
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    Users users = usersMapper.toEntity(request);
    users.setCreatedAt(LocalDateTime.now());
    users.setCreatedBy(callerId);
    users.setDeletedFlag("N");
    users.setAccountLockedFlag("N");
    users.setFailedAttempts(0);
    users.setVersionNumber(0L);
    UsersDto created = usersMapper.toDto(usersRepository.save(users));
    log.info("Created user id={} email={}", created.getUserId(), created.getEmailAddress());
    return created;
  }

  @Transactional
  public Optional<UsersDto> update(Long id, UsersRequest request) {
    log.debug("Updating user id={}", id);
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    Optional<UsersDto> updated =
        usersRepository
            .findActiveById(id)
            .map(
                existing -> {
                  existing.setEmailAddress(request.getEmailAddress());
                  existing.setMobileNumber(request.getMobileNumber());
                  existing.setUserType(request.getUserType());
                  existing.setAccountStatus(request.getAccountStatus());
                  existing.setAccountLockedFlag(request.getAccountLockedFlag());
                  existing.setUpdatedBy(callerId);
                  existing.setUpdatedAt(LocalDateTime.now());
                  existing.setVersionNumber(existing.getVersionNumber() + 1);
                  return usersMapper.toDto(usersRepository.save(existing));
                });
    log.info("User update id={} success={}", id, updated.isPresent());
    return updated;
  }

  @Transactional
  public Optional<UsersDto> softDelete(Long id, Long deletedBy) {
    log.debug("Soft-deleting user id={} deletedBy={}", id, deletedBy);
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(deletedBy);
    LocalDateTime now = LocalDateTime.now();
    Optional<UsersDto> deleted =
        usersRepository
            .findActiveById(id)
            .map(
                existing -> {
                  existing.setDeletedFlag("Y");
                  existing.setDeletedAt(now);
                  existing.setUpdatedBy(callerId);
                  existing.setUpdatedAt(now);
                  existing.setVersionNumber(existing.getVersionNumber() + 1);
                  return usersMapper.toDto(usersRepository.save(existing));
                });
    log.info("User soft-delete id={} success={}", id, deleted.isPresent());
    return deleted;
  }
}
