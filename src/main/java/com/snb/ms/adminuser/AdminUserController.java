package com.snb.ms.adminuser;

import com.snb.ms.adminuser.AdminUserResponse;
import com.snb.ms.adminuser.AdminUserCreateRequest;
import com.snb.ms.adminuser.AdminUserUpdateRequest;
import com.snb.ms.exception.ResourceNotFoundException;
import com.snb.ms.adminuser.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/{id}")
    public AdminUserResponse findById(@PathVariable Long id) {
        log.debug("Received request to fetch admin user by id={}", id);
        AdminUserResponse result = adminUserService.findById(id)
            .orElseThrow(() -> ResourceNotFoundException.adminUserById(id));
        log.info("Admin user found for id={}", id);
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
    @PutMapping("/{id}")
    public AdminUserResponse update(@PathVariable Long id,
                                    @RequestBody AdminUserUpdateRequest request) {
        log.debug("Received request to update admin user id={}", id);
        AdminUserResponse updated = adminUserService.update(id, request)
            .orElseThrow(() -> ResourceNotFoundException.adminUserById(id));
        log.info("Updated admin user id={}", id);
        return updated;
    }

    @Override
    @DeleteMapping("/{id}")
    public AdminUserResponse softDelete(@PathVariable Long id) {
        log.debug("Received request to soft-delete admin user id={}", id);
        AdminUserResponse deleted = adminUserService.softDelete(id)
            .orElseThrow(() -> ResourceNotFoundException.adminUserById(id));
        log.info("Soft-deleted admin user id={}", id);
        return deleted;
    }
}
