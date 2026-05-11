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

    @Query("SELECT c FROM Company c JOIN FETCH c.user WHERE c.deletedFlag = 'N'")
    List<Company> findAllActiveWithUser();

    @Query("SELECT c FROM Company c JOIN FETCH c.user WHERE c.companyId = :id AND c.deletedFlag = 'N'")
    Optional<Company> findActiveById(@Param("id") Long id);

    @Query("SELECT c FROM Company c WHERE c.user.userId = :userId AND c.deletedFlag = 'N'")
    Optional<Company> findActiveByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Company c WHERE c.registrationNumber = :registrationNumber AND c.deletedFlag = 'N'")
    Optional<Company> findActiveByRegistrationNumber(@Param("registrationNumber") String registrationNumber);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Company c WHERE c.registrationNumber = :registrationNumber AND c.deletedFlag = 'N'")
    boolean existsByRegistrationNumber(@Param("registrationNumber") String registrationNumber);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Company c WHERE c.registrationNumber = :registrationNumber AND c.deletedFlag = 'N'")
    boolean existsActiveByRegistrationNumber(@Param("registrationNumber") String registrationNumber);
}
