package com.snb.ms.rbac.privilege;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

  @Query("SELECT p FROM Privilege p WHERE p.deletedFlag = 'N'")
  List<Privilege> findAllActive();

  @Query("SELECT p FROM Privilege p WHERE p.privilegeCode = :privilegeCode AND p.deletedFlag = 'N'")
  Optional<Privilege> findActiveByPrivilegeCode(@Param("privilegeCode") String privilegeCode);

  boolean existsByPrivilegeCodeAndDeletedFlag(String privilegeCode, String deletedFlag);
}
