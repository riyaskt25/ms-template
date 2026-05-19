package com.snb.ms.rbac.roleprivilege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {

    @Query("SELECT rp FROM RolePrivilege rp JOIN FETCH rp.role JOIN FETCH rp.privilege WHERE rp.deletedFlag = 'N'")
    List<RolePrivilege> findAllActive();

    @Query("SELECT rp FROM RolePrivilege rp JOIN FETCH rp.role JOIN FETCH rp.privilege WHERE rp.id = :id AND rp.deletedFlag = 'N'")
    Optional<RolePrivilege> findActiveById(@Param("id") Long id);

    @Query("SELECT rp FROM RolePrivilege rp JOIN FETCH rp.role JOIN FETCH rp.privilege WHERE rp.role.roleId = :roleId AND rp.deletedFlag = 'N'")
    List<RolePrivilege> findActiveByRoleId(@Param("roleId") Long roleId);

    boolean existsByRole_RoleIdAndPrivilege_PrivilegeIdAndDeletedFlag(Long roleId, Long privilegeId, String deletedFlag);
}
