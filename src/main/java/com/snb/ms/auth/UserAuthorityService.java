package com.snb.ms.auth;

import com.snb.ms.rbac.roleprivilege.RolePrivilege;
import com.snb.ms.rbac.roleprivilege.RolePrivilegeRepository;
import com.snb.ms.rbac.userrole.UserRole;
import com.snb.ms.rbac.userrole.UserRoleRepository;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthorityService {

  private final UserRoleRepository userRoleRepository;
  private final RolePrivilegeRepository rolePrivilegeRepository;

  public Set<SimpleGrantedAuthority> resolveAuthorities(Long userId) {
    Set<SimpleGrantedAuthority> authorities = new LinkedHashSet<>();
    for (UserRole userRole : userRoleRepository.findActiveByUserId(userId)) {
      for (RolePrivilege rolePrivilege :
          rolePrivilegeRepository.findActiveByRoleCode(userRole.getRole().getRoleCode())) {
        authorities.add(
            new SimpleGrantedAuthority(rolePrivilege.getPrivilege().getPrivilegeCode()));
      }
    }
    return authorities;
  }
}
