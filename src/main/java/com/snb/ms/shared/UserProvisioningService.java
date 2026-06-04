package com.snb.ms.shared;

import com.snb.ms.shared.request.RequestContextAccessor;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProvisioningService {

  private final UsersRepository usersRepository;
  private final UsersMapper usersMapper;
  private final RequestContextAccessor contextAccessor;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.security.default-password:Ksa@123}")
  private String defaultPassword;

  @Transactional
  public Users createUser(UsersRequest request) {
    log.debug("Provisioning user for userType={}", request.getUserType());
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    Users user = usersMapper.toEntity(request);
    user.setCreatedAt(LocalDateTime.now());
    user.setCreatedBy(callerId);
    user.setPasswordHash(passwordEncoder.encode(defaultPassword));
    user.setDeletedFlag("N");
    user.setAccountLockedFlag("N");
    user.setFailedAttempts(0);
    user.setVersionNumber(0L);
    Users saved = usersRepository.save(user);
    log.info("Provisioned user id={} userType={}", saved.getUserId(), saved.getUserType());
    return saved;
  }
}
