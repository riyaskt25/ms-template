package com.snb.ms.rbac.userrole;

import com.snb.ms.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/roles")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserRoleController implements UserRoleApi {

    private final UserRoleService userRoleService;

    @Override
    @GetMapping
    public UserRolesAggregateResponse findByUserId(@PathVariable Long userId) {
        log.debug("Received request to fetch roles for userId={}", userId);
        return userRoleService.findByUserId(userId);
    }

    @Override
    @PostMapping
    public ResponseEntity<UserRolesAggregateResponse> assign(@PathVariable Long userId, @Valid @RequestBody UserRoleRequest request) {
        log.debug("Received request to assign roleCodes={} to userId={}", request.getRoleCodes(), userId);
        UserRolesAggregateResponse created = userRoleService.assign(userId, request);
        log.info("Assigned roles; userId={} now has {} roles", userId, created.getRoles().size());
        return ResponseEntity.ok(created);
    }

    @Override
    @PutMapping
    public UserRolesAggregateResponse replace(@PathVariable Long userId, @Valid @RequestBody UserRoleRequest request) {
        log.debug("Received request to replace roles with roleCodes={} for userId={}", request.getRoleCodes(), userId);
        UserRolesAggregateResponse updated = userRoleService.replace(userId, request);
        log.info("Replaced roles for userId={} with {} roles", userId, updated.getRoles().size());
        return updated;
    }

    @Override
    @DeleteMapping("/{roleCode}")
    public ResponseEntity<Void> revoke(@PathVariable Long userId, @PathVariable String roleCode) {
        log.debug("Received request to revoke roleCode={} for userId={}", roleCode, userId);
        userRoleService.revoke(userId, roleCode)
            .orElseThrow(() -> ResourceNotFoundException.userRoleByUserIdAndRoleCode(userId, roleCode));
        log.info("Revoked roleCode={} for userId={}", roleCode, userId);
        return ResponseEntity.noContent().build();
    }
}
