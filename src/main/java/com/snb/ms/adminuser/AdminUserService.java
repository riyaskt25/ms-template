package com.snb.ms.adminuser;

import com.snb.ms.adminuser.AdminUserResponse;
import com.snb.ms.adminuser.AdminUserCreateRequest;
import com.snb.ms.adminuser.AdminUserUpdateRequest;
import com.snb.ms.shared.UsersRequest;
import com.snb.ms.adminuser.AdminUser;
import com.snb.ms.shared.Users;
import com.snb.ms.adminuser.AdminUserMapper;
import com.snb.ms.adminuser.AdminUserRepository;
import com.snb.ms.shared.UserProvisioningService;
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
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final AdminUserMapper adminUserMapper;
    private final UserProvisioningService userProvisioningService;
    private final RequestContextAccessor contextAccessor;

    @Transactional(readOnly = true)
    public List<AdminUserResponse> findAll() {
        log.debug("Fetching all admin users");
        List<AdminUserResponse> adminUsers = adminUserMapper.toDtoList(adminUserRepository.findAllWithUser());
        log.info("Fetched {} admin users", adminUsers.size());
        return adminUsers;
    }

    @Transactional(readOnly = true)
    public Optional<AdminUserResponse> findById(Long id) {
        log.debug("Fetching admin user by id={}", id);
        Optional<AdminUserResponse> result = adminUserRepository.findByIdWithUser(id).map(adminUserMapper::toDto);
        log.info("Admin user lookup id={} found={}", id, result.isPresent());
        return result;
    }

    @Transactional
    public AdminUserResponse create(AdminUserCreateRequest request) {
        log.debug("Creating admin user with extensionNumber={}", request.getExtensionNumber());
        UsersRequest userRequest = new UsersRequest();
        userRequest.setEmailAddress(request.getEmailAddress());
        userRequest.setMobileNumber(request.getMobileNumber());
        userRequest.setUserType("ADMIN_USER");
        userRequest.setAccountStatus("ACTIVE");
        userRequest.setAccountLockedFlag("N");

        Users user = userProvisioningService.createUser(userRequest);
        Long callerId = contextAccessor.currentUserIdAsLong().orElse(null);
        AdminUser adminUser = adminUserMapper.toEntity(request);
        adminUser.setUser(user);
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setCreatedBy(callerId);
        adminUser.setDeletedFlag("N");
        adminUser.setVersionNumber(0L);
        AdminUserResponse created = adminUserMapper.toDto(adminUserRepository.save(adminUser));
        log.info("Created admin user id={} extensionNumber={}", created.getAdminUserId(), request.getExtensionNumber());
        return created;
    }

    @Transactional
    public Optional<AdminUserResponse> update(Long id, AdminUserUpdateRequest request) {
        log.debug("Updating admin user id={}", id);
        Long callerId = contextAccessor.currentUserIdAsLong().orElse(null);
        Optional<AdminUserResponse> updated = adminUserRepository.findByIdWithUser(id).map(existing -> {
            adminUserMapper.updateEntity(request, existing);
            Users user = existing.getUser();
            if (user != null) {
                user.setEmailAddress(request.getEmailAddress());
                user.setMobileNumber(request.getMobileNumber());
            }
            existing.setUpdatedAt(LocalDateTime.now());
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return adminUserMapper.toDto(adminUserRepository.save(existing));
        });
        log.info("Admin user update id={} success={}", id, updated.isPresent());
        return updated;
    }

    @Transactional
    public Optional<AdminUserResponse> softDelete(Long id) {
        log.debug("Soft-deleting admin user id={}", id);
        Long callerId = contextAccessor.currentUserIdAsLong().orElse(null);
        Optional<AdminUserResponse> deleted = adminUserRepository.findByIdWithUser(id).map(existing -> {
            existing.setDeletedFlag("Y");
            existing.setDeletedAt(LocalDateTime.now());
            existing.setUpdatedAt(LocalDateTime.now());
            existing.setUpdatedBy(callerId);
            return adminUserMapper.toDto(adminUserRepository.save(existing));
        });
        log.info("Admin user soft-delete id={} success={}", id, deleted.isPresent());
        return deleted;
    }
}
