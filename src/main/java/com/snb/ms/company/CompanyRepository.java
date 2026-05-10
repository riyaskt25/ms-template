package com.snb.ms.company;

import com.snb.ms.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c FROM Company c JOIN FETCH c.user")
    List<Company> findAllWithUser();

    @Query("SELECT c FROM Company c JOIN FETCH c.user WHERE c.companyId = :id")
    Optional<Company> findByIdWithUser(@Param("id") Long id);

    Optional<Company> findByUser_UserId(Long userId);

    Optional<Company> findByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumber(String registrationNumber);
}
