package com.snb.ms.company;

import java.util.List;
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
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    @Override
    @EntityGraph(attributePaths = "user")
    Page<Company> findAll(@Nullable Specification<Company> spec, @NonNull Pageable pageable);

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
