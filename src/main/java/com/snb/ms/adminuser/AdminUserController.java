package com.snb.ms.adminuser;

import com.snb.ms.exception.ResourceNotFoundException;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
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
  @GetMapping("/{userId}")
  public AdminUserResponse findById(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long userId) {
    log.debug("Received request to fetch admin user by userId={}", userId);
    AdminUserResponse result =
        adminUserService
            .findByUserId(userId)
            .orElseThrow(() -> ResourceNotFoundException.adminUserByUserId(userId));
    log.info("Admin user found for userId={}", userId);
    return result;
  }

  @Override
  @PostMapping
  public ResponseEntity<AdminUserResponse> create(@RequestBody AdminUserCreateRequest request) {
    log.debug(
        "Received request to create admin user extensionNumber={}", request.getExtensionNumber());
    AdminUserResponse created = adminUserService.create(request);
    log.info("Created admin user with id={}", created.getAdminUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @Override
  @PutMapping("/{userId}")
  public AdminUserResponse update(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long userId,
      @RequestBody AdminUserUpdateRequest request) {
    log.debug("Received request to update admin user userId={}", userId);
    AdminUserResponse updated =
        adminUserService
            .updateByUserId(userId, request)
            .orElseThrow(() -> ResourceNotFoundException.adminUserByUserId(userId));
    log.info("Updated admin user userId={}", userId);
    return updated;
  }

  @Override
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> softDelete(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long userId) {
    log.debug("Received request to soft-delete admin user userId={}", userId);
    adminUserService
        .softDeleteByUserId(userId)
        .orElseThrow(() -> ResourceNotFoundException.adminUserByUserId(userId));
    log.info("Soft-deleted admin user userId={}", userId);
    return ResponseEntity.noContent().build();
  }
}
