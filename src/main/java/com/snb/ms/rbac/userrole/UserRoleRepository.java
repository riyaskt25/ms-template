package com.snb.ms.rbac.userrole;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.user JOIN FETCH ur.role WHERE ur.user.userId = :userId AND ur.deletedFlag = 'N'")
    List<UserRole> findActiveByUserId(@Param("userId") Long userId);

    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.user JOIN FETCH ur.role WHERE ur.user.userId = :userId AND ur.role.roleCode = :roleCode AND ur.deletedFlag = 'N'")
    Optional<UserRole> findActiveByUserIdAndRoleCode(@Param("userId") Long userId, @Param("roleCode") String roleCode);

    boolean existsByUser_UserIdAndRole_RoleIdAndDeletedFlag(Long userId, Long roleId, String deletedFlag);

    boolean existsByUser_UserIdAndRole_RoleCodeAndDeletedFlag(Long userId, String roleCode, String deletedFlag);
}
