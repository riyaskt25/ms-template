package com.snb.ms.rbac.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.deletedFlag = 'N'")
    List<Role> findAllActive();

    @Query("SELECT r FROM Role r WHERE r.roleId = :id AND r.deletedFlag = 'N'")
    Optional<Role> findActiveById(@Param("id") Long id);

    boolean existsByRoleCodeAndDeletedFlag(String roleCode, String deletedFlag);

    boolean existsByRoleNameAndDeletedFlag(String roleName, String deletedFlag);
}
