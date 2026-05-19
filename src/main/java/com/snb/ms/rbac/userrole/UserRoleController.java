package com.snb.ms.rbac.userrole;

import com.snb.ms.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rbac/user-roles")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserRoleController implements UserRoleApi {

    private final UserRoleService userRoleService;

    @Override
    @GetMapping
    public List<UserRoleResponse> findAll() {
        log.debug("Received request to fetch all user-role assignments");
        return userRoleService.findAll();
    }

    @Override
    @GetMapping("/user/{userId}")
    public List<UserRoleResponse> findByUserId(@PathVariable Long userId) {
        log.debug("Received request to fetch roles for userId={}", userId);
        return userRoleService.findByUserId(userId);
    }

    @Override
    @PostMapping
    public ResponseEntity<UserRoleResponse> assign(@RequestBody UserRoleRequest request) {
        log.debug("Received request to assign roleId={} to userId={}", request.getRoleId(), request.getUserId());
        UserRoleResponse created = userRoleService.assign(request);
        log.info("Assigned roleId={} to userId={}", request.getRoleId(), request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @DeleteMapping("/{id}")
    public UserRoleResponse revoke(@PathVariable Long id) {
        log.debug("Received request to revoke user-role assignment id={}", id);
        UserRoleResponse revoked = userRoleService.revoke(id)
            .orElseThrow(() -> ResourceNotFoundException.userRoleById(id));
        log.info("Revoked user-role assignment id={}", id);
        return revoked;
    }
}
