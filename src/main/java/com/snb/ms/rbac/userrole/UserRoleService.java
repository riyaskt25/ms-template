package com.snb.ms.rbac.userrole;

import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.exception.ResourceNotFoundException;
import com.snb.ms.rbac.role.Role;
import com.snb.ms.rbac.role.RoleRepository;
import com.snb.ms.shared.Users;
import com.snb.ms.shared.UsersRepository;
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
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRoleMapper userRoleMapper;
    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final RequestContextAccessor contextAccessor;

    @Transactional(readOnly = true)
    public List<UserRoleResponse> findAll() {
        log.debug("Fetching all active user-role assignments");
        List<UserRoleResponse> results = userRoleMapper.toDtoList(userRoleRepository.findAllActive());
        log.info("Fetched {} user-role assignments", results.size());
        return results;
    }

    @Transactional(readOnly = true)
    public List<UserRoleResponse> findByUserId(Long userId) {
        log.debug("Fetching roles for userId={}", userId);
        List<UserRoleResponse> results = userRoleMapper.toDtoList(userRoleRepository.findActiveByUserId(userId));
        log.info("Fetched {} roles for userId={}", results.size(), userId);
        return results;
    }

    @Transactional(readOnly = true)
    public Optional<UserRoleResponse> findById(Long id) {
        log.debug("Fetching user-role assignment id={}", id);
        return userRoleRepository.findActiveById(id).map(userRoleMapper::toDto);
    }

    @Transactional
    public UserRoleResponse assign(UserRoleRequest request) {
        log.debug("Assigning roleId={} to userId={}", request.getRoleId(), request.getUserId());
        if (userRoleRepository.existsByUser_UserIdAndRole_RoleIdAndDeletedFlag(request.getUserId(), request.getRoleId(), "N")) {
            throw new BusinessValidationException("error.userRole.alreadyExists", new Object[]{request.getUserId(), request.getRoleId()}, "User already has this role assigned");
        }
        Users user = usersRepository.findById(request.getUserId())
            .orElseThrow(() -> ResourceNotFoundException.userById(request.getUserId()));
        Role role = roleRepository.findActiveById(request.getRoleId())
            .orElseThrow(() -> ResourceNotFoundException.roleById(request.getRoleId()));

        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setCreatedAt(LocalDateTime.now());
        userRole.setCreatedBy(callerId);
        userRole.setDeletedFlag("N");
        userRole.setVersionNumber(0L);
        UserRoleResponse created = userRoleMapper.toDto(userRoleRepository.save(userRole));
        log.info("Assigned roleId={} to userId={} assignmentId={}", request.getRoleId(), request.getUserId(), created.getId());
        return created;
    }

    @Transactional
    public Optional<UserRoleResponse> revoke(Long id) {
        log.debug("Revoking user-role assignment id={}", id);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<UserRoleResponse> revoked = userRoleRepository.findActiveById(id).map(existing -> {
            existing.setDeletedFlag("Y");
            existing.setDeletedAt(now);
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return userRoleMapper.toDto(userRoleRepository.save(existing));
        });
        log.info("User-role revoke id={} success={}", id, revoked.isPresent());
        return revoked;
    }
}
