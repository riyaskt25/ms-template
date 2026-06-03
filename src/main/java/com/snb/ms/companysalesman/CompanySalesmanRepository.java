package com.snb.ms.companysalesman;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanySalesmanRepository extends JpaRepository<CompanySalesman, Long> {

  @Query(
      "SELECT cs FROM CompanySalesman cs JOIN cs.company c JOIN cs.salesman s WHERE c.deletedFlag = 'N' AND s.deletedFlag = 'N'")
  List<CompanySalesman> findAllActive();

  @Query(
      "SELECT cs FROM CompanySalesman cs JOIN cs.company c JOIN cs.salesman s WHERE cs.companySalesmanId = :id AND c.deletedFlag = 'N' AND s.deletedFlag = 'N'")
  Optional<CompanySalesman> findActiveById(@Param("id") Long id);

  @Query(
      "SELECT cs FROM CompanySalesman cs JOIN cs.company c JOIN cs.salesman s WHERE c.companyId = :companyId AND c.deletedFlag = 'N' AND s.deletedFlag = 'N'")
  List<CompanySalesman> findByActiveCompanyId(@Param("companyId") Long companyId);

  @Query(
      "SELECT cs FROM CompanySalesman cs JOIN cs.company c JOIN cs.salesman s WHERE s.salesmanId = :salesmanId AND c.deletedFlag = 'N' AND s.deletedFlag = 'N'")
  List<CompanySalesman> findByActiveSalesmanId(@Param("salesmanId") Long salesmanId);

  @Query(
      "SELECT cs FROM CompanySalesman cs JOIN cs.company c JOIN cs.salesman s WHERE c.companyId = :companyId AND s.salesmanId = :salesmanId AND c.deletedFlag = 'N' AND s.deletedFlag = 'N'")
  Optional<CompanySalesman> findByActiveCompanyIdAndActiveSalesmanId(
      @Param("companyId") Long companyId, @Param("salesmanId") Long salesmanId);

  @Query(
      "SELECT cs FROM CompanySalesman cs JOIN FETCH cs.salesman s JOIN cs.company c WHERE c.companyId IN :companyIds AND c.deletedFlag = 'N' AND s.deletedFlag = 'N'")
  List<CompanySalesman> findActiveByCompanyIds(@Param("companyIds") List<Long> companyIds);
}
