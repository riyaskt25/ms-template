package com.snb.ms.adminuser;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    @Query("SELECT a FROM AdminUser a JOIN FETCH a.user WHERE a.deletedFlag = 'N' AND a.user.deletedFlag = 'N'")
    List<AdminUser> findAllWithUser();

    @Query("SELECT a FROM AdminUser a JOIN FETCH a.user WHERE a.adminUserId = :id AND a.deletedFlag = 'N' AND a.user.deletedFlag = 'N'")
    Optional<AdminUser> findByIdWithUser(@Param("id") Long id);

    @Query("SELECT a FROM AdminUser a WHERE a.user.userId = :userId AND a.deletedFlag = 'N' AND a.user.deletedFlag = 'N'")
    Optional<AdminUser> findActiveByUserId(@Param("userId") Long userId);
}
