package com.snb.ms.userdevice;

import com.snb.ms.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-devices")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserDeviceController implements UserDeviceApi {

  private final UserDeviceService userDeviceService;

  @Override
  @GetMapping
  public UserDevicePageResponse findAll(@Valid @ModelAttribute UserDeviceListQuery query) {
    Page<UserDeviceResponse> userDevices = userDeviceService.findAll(query);
    return UserDevicePageResponse.fromOffsetPage(userDevices);
  }

  @Override
  @GetMapping("/{id}")
  public UserDeviceResponse findById(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id) {
    return userDeviceService
        .findById(id)
        .orElseThrow(() -> ResourceNotFoundException.userDeviceById(id));
  }

  @Override
  @PostMapping
  public ResponseEntity<UserDeviceResponse> create(
      @Valid @RequestBody UserDeviceCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userDeviceService.create(request));
  }

  @Override
  @PutMapping("/{id}")
  public UserDeviceResponse update(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id,
      @Valid @RequestBody UserDeviceUpdateRequest request) {
    return userDeviceService
        .update(id, request)
        .orElseThrow(() -> ResourceNotFoundException.userDeviceById(id));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> softDelete(
      @Positive(message = "{validation.common.id.positive}") @PathVariable Long id) {
    userDeviceService
        .softDelete(id)
        .orElseThrow(() -> ResourceNotFoundException.userDeviceById(id));
    return ResponseEntity.noContent().build();
  }
}
