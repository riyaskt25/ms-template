package com.snb.ms.rbac.userrole;

import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.exception.ErrorCodeEnum;
import com.snb.ms.exception.ResourceNotFoundException;
import com.snb.ms.rbac.role.Role;
import com.snb.ms.rbac.role.RoleRepository;
import com.snb.ms.shared.request.RequestContextAccessor;
import com.snb.ms.shared.Users;
import com.snb.ms.shared.UsersRepository;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UserRolesAggregateResponse findByUserId(Long userId) {
        log.debug("Fetching roles for userId={}", userId);
        Users user = usersRepository.findActiveById(userId)
            .orElseThrow(() -> ResourceNotFoundException.userById(userId));
        List<UserRole> assignments = userRoleRepository.findActiveByUserId(userId);
        UserRolesAggregateResponse results = toAggregateResponse(user, assignments);
        log.info("Fetched {} roles for userId={}", results.getRoles().size(), userId);
        return results;
    }

    @Transactional
    public UserRolesAggregateResponse assign(Long userId, UserRoleRequest request) {
        Set<String> requestedRoleCodes = normalizeRoleCodes(request.getRoleCodes());
        log.debug("Assigning roleCodes={} to userId={}", requestedRoleCodes, userId);
        Users user = usersRepository.findActiveById(userId)
            .orElseThrow(() -> ResourceNotFoundException.userById(userId));
        List<UserRole> activeAssignments = userRoleRepository.findActiveByUserId(userId);
        Set<String> existingRoleCodes = activeAssignments.stream()
            .map(assignment -> assignment.getRole().getRoleCode())
            .collect(Collectors.toSet());
        Set<String> duplicateRoleCodes = requestedRoleCodes.stream()
            .filter(existingRoleCodes::contains)
            .collect(Collectors.toSet());
        if (!duplicateRoleCodes.isEmpty()) {
            throw new BusinessValidationException(
                ErrorCodeEnum.CONFLICT,
                "error.userRole.alreadyExists",
                new Object[]{userId, duplicateRoleCodes.stream().findFirst().orElse(null)},
                "User already has this role assigned"
            );
        }

        Set<String> roleCodesToAdd = requestedRoleCodes.stream()
            .filter(roleCode -> !existingRoleCodes.contains(roleCode))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        Map<String, Role> roleByCode = loadRolesByCodes(roleCodesToAdd);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();

        List<UserRole> createdAssignments = roleCodesToAdd.stream()
            .map(roleCode -> newUserRole(user, roleByCode.get(roleCode), callerId, now))
            .collect(Collectors.toList());

        userRoleRepository.saveAll(createdAssignments);
        List<UserRole> activeAfterAssign = userRoleRepository.findActiveByUserId(userId);
        UserRolesAggregateResponse created = toAggregateResponse(user, activeAfterAssign);
        log.info("Assigned roles; userId={} now has {} active roles", userId, created.getRoles().size());
        return created;
    }

    @Transactional
    public UserRolesAggregateResponse replace(Long userId, UserRoleRequest request) {
        Set<String> requestedRoleCodes = normalizeRoleCodes(request.getRoleCodes());
        log.debug("Replacing roles for userId={} with roleCodes={}", userId, requestedRoleCodes);
        Users user = usersRepository.findActiveById(userId)
            .orElseThrow(() -> ResourceNotFoundException.userById(userId));
        Map<String, Role> requestedRoleByCode = loadRolesByCodes(requestedRoleCodes);

        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();

        List<UserRole> existingAssignments = userRoleRepository.findActiveByUserId(userId);
        Map<String, UserRole> existingByRoleCode = existingAssignments.stream()
            .collect(Collectors.toMap(assignment -> assignment.getRole().getRoleCode(), Function.identity()));

        existingAssignments.stream()
            .filter(assignment -> !requestedRoleCodes.contains(assignment.getRole().getRoleCode()))
            .forEach(assignment -> {
                assignment.setDeletedFlag("Y");
                assignment.setDeletedAt(now);
                assignment.setUpdatedAt(now);
                assignment.setUpdatedBy(callerId);
                assignment.setVersionNumber(assignment.getVersionNumber() + 1);
            });

        List<UserRole> assignmentsToAdd = requestedRoleCodes.stream()
            .filter(roleCode -> !existingByRoleCode.containsKey(roleCode))
            .map(roleCode -> newUserRole(user, requestedRoleByCode.get(roleCode), callerId, now))
            .collect(Collectors.toList());

        userRoleRepository.saveAll(existingAssignments);
        userRoleRepository.saveAll(assignmentsToAdd);

        List<UserRole> activeAfterReplace = userRoleRepository.findActiveByUserId(userId);
        UserRolesAggregateResponse results = toAggregateResponse(user, activeAfterReplace);
        log.info("Replaced roles for userId={} with {} active roles", userId, results.getRoles().size());
        return results;
    }

    @Transactional
    public Optional<UserRoleResponse> revoke(Long userId, String roleCode) {
        log.debug("Revoking roleCode={} for userId={}", roleCode, userId);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<UserRoleResponse> revoked = userRoleRepository.findActiveByUserIdAndRoleCode(userId, roleCode).map(existing -> {
            existing.setDeletedFlag("Y");
            existing.setDeletedAt(now);
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return userRoleMapper.toDto(userRoleRepository.save(existing));
        });
        log.info("User-role revoke userId={} roleCode={} success={}", userId, roleCode, revoked.isPresent());
        return revoked;
    }

    private Set<String> normalizeRoleCodes(List<String> roleCodes) {
        return roleCodes.stream().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Map<String, Role> loadRolesByCodes(Set<String> roleCodes) {
        Map<String, Role> roleByCode = roleCodes.stream()
            .map(roleCode -> roleRepository.findActiveByRoleCode(roleCode)
                .orElseThrow(() -> ResourceNotFoundException.roleByCode(roleCode)))
            .collect(Collectors.toMap(Role::getRoleCode, Function.identity()));
        return roleByCode;
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

    private UserRolesAggregateResponse toAggregateResponse(Users user, List<UserRole> assignments) {
        List<UserAssociatedRoleResponse> roles = assignments.stream()
            .map(assignment -> {
                Role role = assignment.getRole();
                return new UserAssociatedRoleResponse(
                    role.getRoleId(),
                    role.getRoleCode(),
                    role.getRoleName(),
                    role.getDescription()
                );
            })
            .collect(Collectors.toCollection(ArrayList::new));

        return new UserRolesAggregateResponse(
            user.getUserId(),
            user.getEmailAddress(),
            user.getMobileNumber(),
            user.getUserType(),
            user.getAccountStatus(),
            roles
        );
    }
}
