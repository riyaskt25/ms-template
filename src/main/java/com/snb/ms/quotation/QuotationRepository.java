package com.snb.ms.quotation;

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
public interface QuotationRepository
    extends JpaRepository<Quotation, Long>, JpaSpecificationExecutor<Quotation> {

  @Override
  @EntityGraph(attributePaths = {"company", "salesman"})
  Page<Quotation> findAll(@Nullable Specification<Quotation> spec, @NonNull Pageable pageable);

  @Query(
      "SELECT q FROM Quotation q JOIN FETCH q.company JOIN FETCH q.salesman WHERE q.quotationId = :id AND q.deletedFlag = 'N'")
  Optional<Quotation> findActiveById(@Param("id") Long id);

  @Query("SELECT q FROM Quotation q WHERE q.quotationNo = :quotationNo AND q.deletedFlag = 'N'")
  Optional<Quotation> findActiveByQuotationNo(@Param("quotationNo") String quotationNo);

  @Query(
      "SELECT CASE WHEN COUNT(q) > 0 THEN true ELSE false END FROM Quotation q WHERE q.quotationNo = :quotationNo AND q.deletedFlag = 'N'")
  boolean existsActiveByQuotationNo(@Param("quotationNo") String quotationNo);
}
