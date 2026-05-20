package com.snb.ms.rbac.userrole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.user JOIN FETCH ur.role WHERE ur.user.userId = :userId AND ur.deletedFlag = 'N'")
    List<UserRole> findActiveByUserId(@Param("userId") Long userId);

    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.user JOIN FETCH ur.role WHERE ur.user.userId = :userId AND ur.role.roleId = :roleId AND ur.deletedFlag = 'N'")
    Optional<UserRole> findActiveByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    boolean existsByUser_UserIdAndRole_RoleIdAndDeletedFlag(Long userId, Long roleId, String deletedFlag);
}
