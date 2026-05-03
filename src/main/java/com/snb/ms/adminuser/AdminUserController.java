package com.snb.ms.adminuser;

import com.snb.ms.adminuser.AdminUserDto;
import com.snb.ms.adminuser.AdminUserCreateRequest;
import com.snb.ms.adminuser.AdminUserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin Users", description = "Operations for admin user resources")
@Slf4j
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<AdminUserDto>> findAll() {
        log.debug("Received request to fetch all admin users");
        List<AdminUserDto> adminUsers = adminUserService.findAll();
        log.info("Fetched {} admin users", adminUsers.size());
        return ResponseEntity.ok(adminUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDto> findById(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to fetch admin user by id={}", id);
        Optional<AdminUserDto> result = adminUserService.findById(id);
        if (result.isPresent()) {
            log.info("Admin user found for id={}", id);
            return ResponseEntity.ok(result.get());
        }
        log.info("Admin user not found for id={}", id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create admin user with linked user")
    @ApiResponse(responseCode = "201", description = "Admin user created successfully")
    public ResponseEntity<AdminUserDto> create(@Valid @RequestBody AdminUserCreateRequest request) {
        log.debug("Received request to create admin user extensionNumber={}", request.getExtensionNumber());
        AdminUserDto created = adminUserService.create(request);
        log.info("Created admin user with id={}", created.getAdminUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUserDto> update(@PathVariable @Positive(message = "id must be positive") Long id,
                                               @Valid @RequestBody AdminUserUpdateRequest request) {
        log.debug("Received request to update admin user id={}", id);
        Optional<AdminUserDto> updated = adminUserService.update(id, request);
        if (updated.isPresent()) {
            log.info("Updated admin user id={}", id);
            return ResponseEntity.ok(updated.get());
        }
        log.info("Admin user not found for update id={}", id);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AdminUserDto> softDelete(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to soft-delete admin user id={}", id);
        Optional<AdminUserDto> deleted = adminUserService.softDelete(id);
        if (deleted.isPresent()) {
            log.info("Soft-deleted admin user id={}", id);
            return ResponseEntity.ok(deleted.get());
        }
        log.info("Admin user not found for soft-delete id={}", id);
        return ResponseEntity.notFound().build();
    }
}
