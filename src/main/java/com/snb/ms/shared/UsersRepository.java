package com.snb.ms.shared;

import com.snb.ms.shared.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmailAddress(String emailAddress);

    Optional<Users> findByMobileNumber(String mobileNumber);

    boolean existsByEmailAddress(String emailAddress);

    boolean existsByMobileNumber(String mobileNumber);
}
