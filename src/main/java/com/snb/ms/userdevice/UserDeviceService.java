package com.snb.ms.userdevice;

import com.snb.ms.auth.authorization.Privileges;
import com.snb.ms.auth.authorization.RequirePrivilege;
import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.exception.ResourceNotFoundException;
import com.snb.ms.shared.Users;
import com.snb.ms.shared.UsersRepository;
import com.snb.ms.shared.request.RequestContextAccessor;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDeviceService {

  private final UserDeviceRepository userDeviceRepository;
  private final UserDeviceMapper userDeviceMapper;
  private final UsersRepository usersRepository;
  private final RequestContextAccessor contextAccessor;

  @Transactional(readOnly = true)
  @RequirePrivilege(Privileges.USER_DEVICE_VIEW)
  public Page<UserDeviceResponse> findAll(UserDeviceListQuery query) {
    Page<UserDevice> userDevicesPage =
        userDeviceRepository.findAll(
            UserDeviceQueryBuilder.buildSpecification(),
            UserDeviceQueryBuilder.buildPageable(
                query.getPage(), query.getSize(), query.getSortBy(), query.getSortDirection()));
    return userDevicesPage.map(userDeviceMapper::toDto);
  }

  @Transactional(readOnly = true)
  @RequirePrivilege(Privileges.USER_DEVICE_VIEW)
  public Optional<UserDeviceResponse> findById(Long id) {
    return userDeviceRepository.findActiveById(id).map(userDeviceMapper::toDto);
  }

  @Transactional
  @RequirePrivilege(Privileges.USER_DEVICE_MANAGE)
  public UserDeviceResponse create(UserDeviceCreateRequest request) {
    if (userDeviceRepository.existsActiveByDeviceId(request.getDeviceId())) {
      throw BusinessValidationException.userDeviceIdAlreadyExists(request.getDeviceId());
    }

    Users user =
        usersRepository
            .findActiveById(request.getUserId())
            .orElseThrow(() -> ResourceNotFoundException.userById(request.getUserId()));

    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    UserDevice userDevice = userDeviceMapper.toEntity(request);
    userDevice.setUser(user);
    userDevice.setCreatedAt(LocalDateTime.now());
    userDevice.setCreatedBy(callerId);
    userDevice.setDeletedFlag("N");
    userDevice.setVersionNumber(0L);
    return userDeviceMapper.toDto(userDeviceRepository.save(userDevice));
  }

  @Transactional
  @RequirePrivilege(Privileges.USER_DEVICE_MANAGE)
  public Optional<UserDeviceResponse> update(Long id, UserDeviceUpdateRequest request) {
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    return userDeviceRepository
        .findActiveById(id)
        .map(
            existing -> {
              if (!existing.getDeviceId().equals(request.getDeviceId())
                  && userDeviceRepository.existsActiveByDeviceId(request.getDeviceId())) {
                throw BusinessValidationException.userDeviceIdAlreadyExists(request.getDeviceId());
              }

              Users user =
                  usersRepository
                      .findActiveById(request.getUserId())
                      .orElseThrow(() -> ResourceNotFoundException.userById(request.getUserId()));

              userDeviceMapper.updateEntity(request, existing);
              existing.setUser(user);
              existing.setUpdatedAt(now);
              existing.setUpdatedBy(callerId);
              existing.setVersionNumber(existing.getVersionNumber() + 1);
              return userDeviceMapper.toDto(userDeviceRepository.save(existing));
            });
  }

  @Transactional
  @RequirePrivilege(Privileges.USER_DEVICE_MANAGE)
  public Optional<UserDeviceResponse> softDelete(Long id) {
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    return userDeviceRepository
        .findActiveById(id)
        .map(
            existing -> {
              existing.setDeletedFlag("Y");
              existing.setDeletedAt(now);
              existing.setUpdatedAt(now);
              existing.setUpdatedBy(callerId);
              existing.setVersionNumber(existing.getVersionNumber() + 1);
              return userDeviceMapper.toDto(userDeviceRepository.save(existing));
            });
  }
}
