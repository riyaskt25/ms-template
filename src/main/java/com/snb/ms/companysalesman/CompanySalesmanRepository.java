package com.snb.ms.companysalesman;

import com.snb.ms.companysalesman.CompanySalesman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanySalesmanRepository extends JpaRepository<CompanySalesman, Long> {

    List<CompanySalesman> findByCompany_CompanyId(Long companyId);

    List<CompanySalesman> findBySalesman_SalesmanId(Long salesmanId);

    Optional<CompanySalesman> findByCompany_CompanyIdAndSalesman_SalesmanId(Long companyId, Long salesmanId);
}
