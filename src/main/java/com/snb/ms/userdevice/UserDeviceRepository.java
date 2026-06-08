package com.snb.ms.userdevice;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeviceRepository
    extends JpaRepository<UserDevice, Long>, JpaSpecificationExecutor<UserDevice> {

  @Override
  @EntityGraph(attributePaths = "user")
  Page<UserDevice> findAll(@Nullable Specification<UserDevice> spec, @NonNull Pageable pageable);

  @Query(
      "SELECT ud FROM UserDevice ud JOIN FETCH ud.user WHERE ud.userDeviceId = :id AND ud.deletedFlag = 'N'")
  Optional<UserDevice> findActiveById(@Param("id") Long id);

  @Query("SELECT ud FROM UserDevice ud WHERE ud.deviceId = :deviceId AND ud.deletedFlag = 'N'")
  Optional<UserDevice> findActiveByDeviceId(@Param("deviceId") String deviceId);

  @Query(
      "SELECT CASE WHEN COUNT(ud) > 0 THEN true ELSE false END FROM UserDevice ud WHERE ud.deviceId = :deviceId AND ud.deletedFlag = 'N'")
  boolean existsActiveByDeviceId(@Param("deviceId") String deviceId);
}
