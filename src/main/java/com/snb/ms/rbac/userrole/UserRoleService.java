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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<UserRoleResponse> findByUserId(Long userId) {
        log.debug("Fetching roles for userId={}", userId);
        List<UserRoleResponse> results = userRoleMapper.toDtoList(userRoleRepository.findActiveByUserId(userId));
        log.info("Fetched {} roles for userId={}", results.size(), userId);
        return results;
    }

    @Transactional
    public List<UserRoleResponse> assign(Long userId, UserRoleRequest request) {
        Set<Long> requestedRoleIds = normalizeRoleIds(request.getRoleIds());
        log.debug("Assigning roleIds={} to userId={}", requestedRoleIds, userId);
        Users user = usersRepository.findActiveById(userId)
            .orElseThrow(() -> ResourceNotFoundException.userById(userId));
        List<UserRole> activeAssignments = userRoleRepository.findActiveByUserId(userId);
        Set<Long> existingRoleIds = activeAssignments.stream()
            .map(assignment -> assignment.getRole().getRoleId())
            .collect(Collectors.toSet());
        Set<Long> duplicateRoleIds = requestedRoleIds.stream()
            .filter(existingRoleIds::contains)
            .collect(Collectors.toSet());
        if (!duplicateRoleIds.isEmpty()) {
            throw new BusinessValidationException(
                "error.userRole.alreadyExists",
                new Object[]{userId, duplicateRoleIds.stream().findFirst().orElse(null)},
                "User already has this role assigned"
            );
        }

        Set<Long> roleIdsToAdd = requestedRoleIds.stream()
            .filter(roleId -> !existingRoleIds.contains(roleId))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        Map<Long, Role> roleById = loadRolesByIds(roleIdsToAdd);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();

        List<UserRole> createdAssignments = roleIdsToAdd.stream()
            .map(roleId -> newUserRole(user, roleById.get(roleId), callerId, now))
            .collect(Collectors.toList());

        List<UserRoleResponse> created = userRoleMapper.toDtoList(userRoleRepository.saveAll(createdAssignments));
        log.info("Assigned {} roles to userId={}", created.size(), userId);
        return created;
    }

    @Transactional
    public List<UserRoleResponse> replace(Long userId, UserRoleRequest request) {
        Set<Long> requestedRoleIds = normalizeRoleIds(request.getRoleIds());
        log.debug("Replacing roles for userId={} with roleIds={}", userId, requestedRoleIds);
        Users user = usersRepository.findActiveById(userId)
            .orElseThrow(() -> ResourceNotFoundException.userById(userId));
        Map<Long, Role> requestedRoleById = loadRolesByIds(requestedRoleIds);

        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();

        List<UserRole> existingAssignments = userRoleRepository.findActiveByUserId(userId);
        Map<Long, UserRole> existingByRoleId = existingAssignments.stream()
            .collect(Collectors.toMap(assignment -> assignment.getRole().getRoleId(), Function.identity()));

        existingAssignments.stream()
            .filter(assignment -> !requestedRoleIds.contains(assignment.getRole().getRoleId()))
            .forEach(assignment -> {
                assignment.setDeletedFlag("Y");
                assignment.setDeletedAt(now);
                assignment.setUpdatedAt(now);
                assignment.setUpdatedBy(callerId);
                assignment.setVersionNumber(assignment.getVersionNumber() + 1);
            });

        List<UserRole> assignmentsToAdd = requestedRoleIds.stream()
            .filter(roleId -> !existingByRoleId.containsKey(roleId))
            .map(roleId -> newUserRole(user, requestedRoleById.get(roleId), callerId, now))
            .collect(Collectors.toList());

        userRoleRepository.saveAll(existingAssignments);
        userRoleRepository.saveAll(assignmentsToAdd);

        List<UserRoleResponse> results = userRoleMapper.toDtoList(userRoleRepository.findActiveByUserId(userId));
        log.info("Replaced roles for userId={} with {} active roles", userId, results.size());
        return results;
    }

    @Transactional
    public Optional<UserRoleResponse> revoke(Long userId, Long roleId) {
        log.debug("Revoking roleId={} for userId={}", roleId, userId);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<UserRoleResponse> revoked = userRoleRepository.findActiveByUserIdAndRoleId(userId, roleId).map(existing -> {
            existing.setDeletedFlag("Y");
            existing.setDeletedAt(now);
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return userRoleMapper.toDto(userRoleRepository.save(existing));
        });
        log.info("User-role revoke userId={} roleId={} success={}", userId, roleId, revoked.isPresent());
        return revoked;
    }

    private Set<Long> normalizeRoleIds(List<Long> roleIds) {
        return roleIds.stream().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Map<Long, Role> loadRolesByIds(Set<Long> roleIds) {
        Map<Long, Role> roleById = roleIds.stream()
            .map(roleId -> roleRepository.findActiveById(roleId)
                .orElseThrow(() -> ResourceNotFoundException.roleById(roleId)))
            .collect(Collectors.toMap(Role::getRoleId, Function.identity()));
        return roleById;
    }

    private UserRole newUserRole(Users user, Role role, Long callerId, LocalDateTime now) {
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setCreatedAt(now);
        userRole.setCreatedBy(callerId);
        userRole.setDeletedFlag("N");
        userRole.setVersionNumber(0L);
        return userRole;
    }
}
