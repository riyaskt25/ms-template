package com.snb.ms.adminuser;

import com.snb.ms.exception.ResourceNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin-users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminUserController implements AdminUserApi {

    private final AdminUserService adminUserService;

    @Override
    @GetMapping
    public List<AdminUserResponse> findAll() {
        log.debug("Received request to fetch all admin users");
        List<AdminUserResponse> adminUsers = adminUserService.findAll();
        log.info("Fetched {} admin users", adminUsers.size());
        return adminUsers;
    }

    @Override
    @GetMapping("/{snbId}")
    public AdminUserResponse findById(@PathVariable String snbId) {
        log.debug("Received request to fetch admin user by snbId={}", snbId);
        AdminUserResponse result = adminUserService.findBySnbId(snbId)
            .orElseThrow(() -> ResourceNotFoundException.adminUserBySnbId(snbId));
        log.info("Admin user found for snbId={}", snbId);
        return result;
    }

    @Override
    @PostMapping
    public ResponseEntity<AdminUserResponse> create(@RequestBody AdminUserCreateRequest request) {
        log.debug("Received request to create admin user extensionNumber={}", request.getExtensionNumber());
        AdminUserResponse created = adminUserService.create(request);
        log.info("Created admin user with id={}", created.getAdminUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PutMapping("/{snbId}")
    public AdminUserResponse update(@PathVariable String snbId,
                                    @RequestBody AdminUserUpdateRequest request) {
        log.debug("Received request to update admin user snbId={}", snbId);
        AdminUserResponse updated = adminUserService.updateBySnbId(snbId, request)
            .orElseThrow(() -> ResourceNotFoundException.adminUserBySnbId(snbId));
        log.info("Updated admin user snbId={}", snbId);
        return updated;
    }

    @Override
    @DeleteMapping("/{snbId}")
    public AdminUserResponse softDelete(@PathVariable String snbId) {
        log.debug("Received request to soft-delete admin user snbId={}", snbId);
        AdminUserResponse deleted = adminUserService.softDeleteBySnbId(snbId)
            .orElseThrow(() -> ResourceNotFoundException.adminUserBySnbId(snbId));
        log.info("Soft-deleted admin user snbId={}", snbId);
        return deleted;
    }
}
