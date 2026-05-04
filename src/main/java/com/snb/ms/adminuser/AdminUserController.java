package com.snb.ms.adminuser;

import com.snb.ms.adminuser.AdminUserResponse;
import com.snb.ms.adminuser.AdminUserCreateRequest;
import com.snb.ms.adminuser.AdminUserUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import com.snb.ms.adminuser.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin-users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminUserController implements AdminUserApi {

    private final AdminUserService adminUserService;

    @Override
    @GetMapping
    public ResponseEntity<List<AdminUserResponse>> findAll() {
        log.debug("Received request to fetch all admin users");
        List<AdminUserResponse> adminUsers = adminUserService.findAll();
        log.info("Fetched {} admin users", adminUsers.size());
        return ResponseEntity.ok(adminUsers);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<AdminUserResponse> findById(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to fetch admin user by id={}", id);
        Optional<AdminUserResponse> result = adminUserService.findById(id);
        if (result.isPresent()) {
            log.info("Admin user found for id={}", id);
            return ResponseEntity.ok(result.get());
        }
        log.info("Admin user not found for id={}", id);
        return ResponseEntity.notFound().build();
    }

    @Override
    @PostMapping
    public ResponseEntity<AdminUserResponse> create(@Valid @RequestBody AdminUserCreateRequest request) {
        log.debug("Received request to create admin user extensionNumber={}", request.getExtensionNumber());
        AdminUserResponse created = adminUserService.create(request);
        log.info("Created admin user with id={}", created.getAdminUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<AdminUserResponse> update(@PathVariable @Positive(message = "id must be positive") Long id,
                                               @Valid @RequestBody AdminUserUpdateRequest request) {
        log.debug("Received request to update admin user id={}", id);
        Optional<AdminUserResponse> updated = adminUserService.update(id, request);
        if (updated.isPresent()) {
            log.info("Updated admin user id={}", id);
            return ResponseEntity.ok(updated.get());
        }
        log.info("Admin user not found for update id={}", id);
        return ResponseEntity.notFound().build();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<AdminUserResponse> softDelete(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to soft-delete admin user id={}", id);
        Optional<AdminUserResponse> deleted = adminUserService.softDelete(id);
        if (deleted.isPresent()) {
            log.info("Soft-deleted admin user id={}", id);
            return ResponseEntity.ok(deleted.get());
        }
        log.info("Admin user not found for soft-delete id={}", id);
        return ResponseEntity.notFound().build();
    }
}
