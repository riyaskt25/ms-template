package com.snb.ms.auth;

import com.snb.ms.shared.Users;
import com.snb.ms.shared.UsersRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

  private final UsersRepository usersRepository;
  private final UserAuthorityService userAuthorityService;

  @Override
  public UserDetails loadUserByUsername(String username) {
    Users user = resolveUser(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found for identifier: " + username);
    }
    if (!"ACTIVE".equalsIgnoreCase(user.getAccountStatus())) {
      throw new DisabledException("Account is inactive for identifier: " + username);
    }
    if ("Y".equalsIgnoreCase(user.getAccountLockedFlag())) {
      throw new LockedException("Account is locked for identifier: " + username);
    }

    Set<SimpleGrantedAuthority> authorities =
        userAuthorityService.resolveAuthorities(user.getUserId());
    return AuthenticatedUserPrincipal.builder()
        .userId(user.getUserId())
        .username(user.getEmailAddress())
        .password(user.getPasswordHash())
        .enabled(true)
        .accountNonLocked(true)
        .authorities(authorities)
        .build();
  }

  private Users resolveUser(String username) {
    if (!StringUtils.hasText(username)) {
      return null;
    }
    return usersRepository
        .findActiveByEmailAddress(username)
        .orElseGet(() -> usersRepository.findActiveByMobileNumber(username).orElse(null));
  }
}
