package com.snb.ms.tier;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface TierRepository extends JpaRepository<Tier, Long>, JpaSpecificationExecutor<Tier> {

  @Override
  Page<Tier> findAll(@Nullable Specification<Tier> spec, @NonNull Pageable pageable);

  @Query("SELECT t FROM Tier t WHERE t.tierId = :id AND t.deletedFlag = 'N'")
  Optional<Tier> findActiveById(@Param("id") Long id);

  @Query("SELECT t FROM Tier t WHERE t.tierCode = :tierCode AND t.deletedFlag = 'N'")
  Optional<Tier> findActiveByTierCode(@Param("tierCode") String tierCode);

  @Query(
      "SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Tier t WHERE t.tierCode = :tierCode AND t.deletedFlag = 'N'")
  boolean existsActiveByTierCode(@Param("tierCode") String tierCode);
}
