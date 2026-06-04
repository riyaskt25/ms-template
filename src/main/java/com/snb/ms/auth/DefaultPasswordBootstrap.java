package com.snb.ms.auth;

import com.snb.ms.shared.Users;
import com.snb.ms.shared.UsersRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class DefaultPasswordBootstrap implements ApplicationRunner {

  private final UsersRepository usersRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.security.default-password:Ksa@123}")
  private String defaultPassword;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    LocalDateTime now = LocalDateTime.now();
    for (Users user : usersRepository.findAll()) {
      if (!StringUtils.hasText(user.getPasswordHash())) {
        user.setPasswordHash(passwordEncoder.encode(defaultPassword));
        user.setUpdatedAt(now);
        user.setVersionNumber((user.getVersionNumber() == null ? 0L : user.getVersionNumber()) + 1);
      }
    }
    usersRepository.flush();
  }
}
