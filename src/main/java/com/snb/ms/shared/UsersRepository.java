package com.snb.ms.shared;

import com.snb.ms.shared.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.deletedFlag = 'N'")
    List<Users> findAllActive();

    @Query("SELECT u FROM Users u WHERE u.userId = :id AND u.deletedFlag = 'N'")
    Optional<Users> findActiveById(@Param("id") Long id);

    @Query("SELECT u FROM Users u WHERE u.emailAddress = :emailAddress AND u.deletedFlag = 'N'")
    Optional<Users> findActiveByEmailAddress(@Param("emailAddress") String emailAddress);

    @Query("SELECT u FROM Users u WHERE u.mobileNumber = :mobileNumber AND u.deletedFlag = 'N'")
    Optional<Users> findActiveByMobileNumber(@Param("mobileNumber") String mobileNumber);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Users u WHERE u.emailAddress = :emailAddress AND u.deletedFlag = 'N'")
    boolean existsActiveByEmailAddress(@Param("emailAddress") String emailAddress);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Users u WHERE u.mobileNumber = :mobileNumber AND u.deletedFlag = 'N'")
    boolean existsActiveByMobileNumber(@Param("mobileNumber") String mobileNumber);
}
