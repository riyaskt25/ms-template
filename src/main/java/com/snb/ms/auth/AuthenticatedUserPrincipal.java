package com.snb.ms.auth;

import java.util.Collection;
import java.util.Collections;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
public class AuthenticatedUserPrincipal implements UserDetails {

  private final Long userId;
  private final String username;
  private final String password;
  private final boolean enabled;
  private final boolean accountNonLocked;
  private final Collection<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities == null ? Collections.emptyList() : authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
