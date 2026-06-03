package com.snb.ms.adminuser;

import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.shared.UserProvisioningService;
import com.snb.ms.shared.Users;
import com.snb.ms.shared.UsersRequest;
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
public class AdminUserService {

  private final AdminUserRepository adminUserRepository;
  private final AdminUserMapper adminUserMapper;
  private final UserProvisioningService userProvisioningService;
  private final RequestContextAccessor contextAccessor;

  @Transactional(readOnly = true)
  public List<AdminUserResponse> findAll() {
    log.debug("Fetching all admin users");
    List<AdminUserResponse> adminUsers =
        adminUserMapper.toDtoList(adminUserRepository.findAllWithUser());
    log.info("Fetched {} admin users", adminUsers.size());
    return adminUsers;
  }

  @Transactional(readOnly = true)
  public Optional<AdminUserResponse> findByUserId(Long userId) {
    log.debug("Fetching admin user by userId={}", userId);
    Optional<AdminUserResponse> result =
        adminUserRepository.findByUserIdWithUser(userId).map(adminUserMapper::toDto);
    log.info("Admin user lookup userId={} found={}", userId, result.isPresent());
    return result;
  }

  @Transactional
  public AdminUserResponse create(AdminUserCreateRequest request) {
    log.debug(
        "Creating admin user with snbId={} extensionNumber={}",
        request.getSnbId(),
        request.getExtensionNumber());
    if (adminUserRepository.existsBySnbIdAndDeletedFlag(request.getSnbId(), "N")) {
      throw new BusinessValidationException(
          "error.adminUser.snbId.alreadyExists",
          new Object[] {request.getSnbId()},
          "Admin user with snbId already exists: " + request.getSnbId());
    }
    UsersRequest userRequest = new UsersRequest();
    userRequest.setEmailAddress(request.getEmailAddress());
    userRequest.setMobileNumber(request.getMobileNumber());
    userRequest.setUserType("ADMIN_USER");
    userRequest.setAccountStatus("ACTIVE");
    userRequest.setAccountLockedFlag("N");

    Users user = userProvisioningService.createUser(userRequest);
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    AdminUser adminUser = adminUserMapper.toEntity(request);
    adminUser.setUser(user);
    adminUser.setCreatedAt(LocalDateTime.now());
    adminUser.setCreatedBy(callerId);
    adminUser.setDeletedFlag("N");
    adminUser.setVersionNumber(0L);
    AdminUserResponse created = adminUserMapper.toDto(adminUserRepository.save(adminUser));
    log.info(
        "Created admin user id={} extensionNumber={}",
        created.getAdminUserId(),
        request.getExtensionNumber());
    return created;
  }

  @Transactional
  public Optional<AdminUserResponse> updateByUserId(Long userId, AdminUserUpdateRequest request) {
    log.debug("Updating admin user userId={}", userId);
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    Optional<AdminUserResponse> updated =
        adminUserRepository
            .findByUserIdWithUser(userId)
            .map(
                existing -> {
                  if (adminUserRepository.existsBySnbIdAndDeletedFlagAndAdminUserIdNot(
                      request.getSnbId(), "N", existing.getAdminUserId())) {
                    throw new BusinessValidationException(
                        "error.adminUser.snbId.alreadyExists",
                        new Object[] {request.getSnbId()},
                        "Admin user with snbId already exists: " + request.getSnbId());
                  }
                  adminUserMapper.updateEntity(request, existing);
                  Users user = existing.getUser();
                  if (user != null) {
                    user.setEmailAddress(request.getEmailAddress());
                    user.setMobileNumber(request.getMobileNumber());
                    user.setUpdatedAt(now);
                    user.setUpdatedBy(callerId);
                    user.setVersionNumber(
                        (user.getVersionNumber() == null ? 0L : user.getVersionNumber()) + 1);
                  }
                  existing.setUpdatedAt(now);
                  existing.setUpdatedBy(callerId);
                  existing.setVersionNumber(existing.getVersionNumber() + 1);
                  return adminUserMapper.toDto(adminUserRepository.save(existing));
                });
    log.info("Admin user update userId={} success={}", userId, updated.isPresent());
    return updated;
  }

  @Transactional
  public Optional<AdminUserResponse> softDeleteByUserId(Long userId) {
    log.debug("Soft-deleting admin user userId={}", userId);
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    Optional<AdminUserResponse> deleted =
        adminUserRepository
            .findByUserIdWithUser(userId)
            .map(
                existing -> {
                  existing.setDeletedFlag("Y");
                  existing.setDeletedAt(now);
                  existing.setUpdatedAt(now);
                  existing.setUpdatedBy(callerId);
                  existing.setVersionNumber(existing.getVersionNumber() + 1);
                  return adminUserMapper.toDto(adminUserRepository.save(existing));
                });
    log.info("Admin user soft-delete userId={} success={}", userId, deleted.isPresent());
    return deleted;
  }
}
