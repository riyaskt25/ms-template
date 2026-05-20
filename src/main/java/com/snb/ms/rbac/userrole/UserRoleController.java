package com.snb.ms.rbac.userrole;

import com.snb.ms.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/roles")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserRoleController implements UserRoleApi {

    private final UserRoleService userRoleService;

    @Override
    @GetMapping
    public List<UserRoleResponse> findByUserId(@PathVariable Long userId) {
        log.debug("Received request to fetch roles for userId={}", userId);
        return userRoleService.findByUserId(userId);
    }

    @Override
    @PostMapping
    public ResponseEntity<List<UserRoleResponse>> assign(@PathVariable Long userId, @Valid @RequestBody UserRoleRequest request) {
        log.debug("Received request to assign roleIds={} to userId={}", request.getRoleIds(), userId);
        List<UserRoleResponse> created = userRoleService.assign(userId, request);
        log.info("Assigned {} roles to userId={}", created.size(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PutMapping
    public List<UserRoleResponse> replace(@PathVariable Long userId, @Valid @RequestBody UserRoleRequest request) {
        log.debug("Received request to replace roles with roleIds={} for userId={}", request.getRoleIds(), userId);
        List<UserRoleResponse> updated = userRoleService.replace(userId, request);
        log.info("Replaced roles for userId={} with {} roles", userId, updated.size());
        return updated;
    }

    @Override
    @DeleteMapping("/{roleId}")
    public UserRoleResponse revoke(@PathVariable Long userId, @PathVariable Long roleId) {
        log.debug("Received request to revoke roleId={} for userId={}", roleId, userId);
        UserRoleResponse revoked = userRoleService.revoke(userId, roleId)
            .orElseThrow(() -> ResourceNotFoundException.userRoleByUserIdAndRoleId(userId, roleId));
        log.info("Revoked roleId={} for userId={}", roleId, userId);
        return revoked;
    }
}
