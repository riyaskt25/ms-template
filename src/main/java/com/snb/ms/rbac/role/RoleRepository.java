package com.snb.ms.rbac.role;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.deletedFlag = 'N'")
    List<Role> findAllActive();

    @Query("SELECT r FROM Role r WHERE r.roleId = :id AND r.deletedFlag = 'N'")
    Optional<Role> findActiveById(@Param("id") Long id);

    @Query("SELECT r FROM Role r WHERE r.roleCode = :roleCode AND r.deletedFlag = 'N'")
    Optional<Role> findActiveByRoleCode(@Param("roleCode") String roleCode);

    boolean existsByRoleCodeAndDeletedFlag(String roleCode, String deletedFlag);

    boolean existsByRoleNameAndDeletedFlag(String roleName, String deletedFlag);
}
