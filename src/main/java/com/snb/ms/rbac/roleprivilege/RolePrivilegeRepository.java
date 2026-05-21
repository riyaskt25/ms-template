package com.snb.ms.rbac.roleprivilege;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {

    @Query("SELECT rp FROM RolePrivilege rp JOIN FETCH rp.role JOIN FETCH rp.privilege WHERE rp.deletedFlag = 'N'")
    List<RolePrivilege> findAllActive();

    @Query("SELECT rp FROM RolePrivilege rp JOIN FETCH rp.role JOIN FETCH rp.privilege WHERE rp.id = :id AND rp.deletedFlag = 'N'")
    Optional<RolePrivilege> findActiveById(@Param("id") Long id);

    @Query("SELECT rp FROM RolePrivilege rp JOIN FETCH rp.role JOIN FETCH rp.privilege WHERE rp.role.roleCode = :roleCode AND rp.deletedFlag = 'N'")
    List<RolePrivilege> findActiveByRoleCode(@Param("roleCode") String roleCode);

    @Query("SELECT rp FROM RolePrivilege rp JOIN FETCH rp.role JOIN FETCH rp.privilege WHERE rp.role.roleCode = :roleCode AND rp.privilege.privilegeCode = :privilegeCode AND rp.deletedFlag = 'N'")
    Optional<RolePrivilege> findActiveByRoleCodeAndPrivilegeCode(@Param("roleCode") String roleCode, @Param("privilegeCode") String privilegeCode);

    boolean existsByRole_RoleCodeAndPrivilege_PrivilegeCodeAndDeletedFlag(String roleCode, String privilegeCode, String deletedFlag);
}
