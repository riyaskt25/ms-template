package com.snb.ms.adminuser;

import com.snb.ms.adminuser.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    @Query("SELECT a FROM AdminUser a JOIN FETCH a.user")
    List<AdminUser> findAllWithUser();

    @Query("SELECT a FROM AdminUser a JOIN FETCH a.user WHERE a.adminUserId = :id")
    Optional<AdminUser> findByIdWithUser(@Param("id") Long id);

    Optional<AdminUser> findByUser_UserId(Long userId);
}
