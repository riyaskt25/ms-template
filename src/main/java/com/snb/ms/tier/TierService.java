package com.snb.ms.tier;

import com.snb.ms.auth.authorization.Privileges;
import com.snb.ms.auth.authorization.RequirePrivilege;
import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.shared.request.RequestContextAccessor;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TierService {

  private final TierRepository tierRepository;
  private final TierMapper tierMapper;
  private final RequestContextAccessor contextAccessor;

  @Transactional(readOnly = true)
  @RequirePrivilege(Privileges.TIER_VIEW)
  public Page<TierResponse> findAll(TierListQuery query) {
    Page<Tier> tiersPage =
        tierRepository.findAll(
            TierQueryBuilder.buildSpecification(),
            TierQueryBuilder.buildPageable(
                query.getPage(), query.getSize(), query.getSortBy(), query.getSortDirection()));
    return tiersPage.map(tierMapper::toDto);
  }

  @Transactional(readOnly = true)
  @RequirePrivilege(Privileges.TIER_VIEW)
  public Optional<TierResponse> findById(Long id) {
    return tierRepository.findActiveById(id).map(tierMapper::toDto);
  }

  @Transactional
  @RequirePrivilege(Privileges.TIER_MANAGE)
  public TierResponse create(TierCreateRequest request) {
    if (tierRepository.existsActiveByTierCode(request.getTierCode())) {
      throw BusinessValidationException.tierCodeAlreadyExists(request.getTierCode());
    }

    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    Tier tier = tierMapper.toEntity(request);
    tier.setCreatedAt(LocalDateTime.now());
    tier.setCreatedBy(callerId);
    tier.setDeletedFlag("N");
    tier.setVersionNumber(0L);
    return tierMapper.toDto(tierRepository.save(tier));
  }

  @Transactional
  @RequirePrivilege(Privileges.TIER_MANAGE)
  public Optional<TierResponse> update(Long id, TierUpdateRequest request) {
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    return tierRepository
        .findActiveById(id)
        .map(
            existing -> {
              if (!existing.getTierCode().equals(request.getTierCode())
                  && tierRepository.existsActiveByTierCode(request.getTierCode())) {
                throw BusinessValidationException.tierCodeAlreadyExists(request.getTierCode());
              }

              tierMapper.updateEntity(request, existing);
              existing.setUpdatedAt(now);
              existing.setUpdatedBy(callerId);
              existing.setVersionNumber(existing.getVersionNumber() + 1);
              return tierMapper.toDto(tierRepository.save(existing));
            });
  }

  @Transactional
  @RequirePrivilege(Privileges.TIER_MANAGE)
  public Optional<TierResponse> softDelete(Long id) {
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    return tierRepository
        .findActiveById(id)
        .map(
            existing -> {
              existing.setDeletedFlag("Y");
              existing.setDeletedAt(now);
              existing.setUpdatedAt(now);
              existing.setUpdatedBy(callerId);
              existing.setVersionNumber(existing.getVersionNumber() + 1);
              return tierMapper.toDto(tierRepository.save(existing));
            });
  }
}
