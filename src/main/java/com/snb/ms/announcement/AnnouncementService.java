package com.snb.ms.announcement;

import com.snb.ms.auth.authorization.Privileges;
import com.snb.ms.auth.authorization.RequirePrivilege;
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
public class AnnouncementService {

  private final AnnouncementRepository announcementRepository;
  private final AnnouncementMapper announcementMapper;
  private final RequestContextAccessor contextAccessor;

  @Transactional(readOnly = true)
  @RequirePrivilege(Privileges.ANNOUNCEMENT_VIEW)
  public Page<AnnouncementResponse> findAll(AnnouncementListQuery query) {
    Page<Announcement> announcementsPage =
        announcementRepository.findAll(
            AnnouncementQueryBuilder.buildSpecification(
                query.getTitle(),
                query.getPriority(),
                query.getType(),
                query.getStatus(),
                query.getDescription()),
            AnnouncementQueryBuilder.buildPageable(
                query.getPage(), query.getSize(), query.getSortBy(), query.getSortDirection()));
    return announcementsPage.map(announcementMapper::toDto);
  }

  @Transactional(readOnly = true)
  @RequirePrivilege(Privileges.ANNOUNCEMENT_VIEW)
  public Optional<AnnouncementResponse> findById(Long id) {
    return announcementRepository.findActiveById(id).map(announcementMapper::toDto);
  }

  @Transactional
  @RequirePrivilege(Privileges.ANNOUNCEMENT_MANAGE)
  public AnnouncementResponse create(AnnouncementCreateRequest request) {
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    Announcement announcement = announcementMapper.toEntity(request);
    announcement.setCreatedAt(LocalDateTime.now());
    announcement.setCreatedBy(callerId);
    announcement.setDeletedFlag("N");
    announcement.setVersionNumber(0L);
    return announcementMapper.toDto(announcementRepository.save(announcement));
  }

  @Transactional
  @RequirePrivilege(Privileges.ANNOUNCEMENT_MANAGE)
  public Optional<AnnouncementResponse> update(Long id, AnnouncementUpdateRequest request) {
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    return announcementRepository
        .findActiveById(id)
        .map(
            existing -> {
              announcementMapper.updateEntity(request, existing);
              existing.setUpdatedAt(now);
              existing.setUpdatedBy(callerId);
              existing.setVersionNumber(existing.getVersionNumber() + 1);
              return announcementMapper.toDto(announcementRepository.save(existing));
            });
  }

  @Transactional
  @RequirePrivilege(Privileges.ANNOUNCEMENT_MANAGE)
  public Optional<AnnouncementResponse> softDelete(Long id) {
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    return announcementRepository
        .findActiveById(id)
        .map(
            existing -> {
              existing.setDeletedFlag("Y");
              existing.setDeletedAt(now);
              existing.setUpdatedAt(now);
              existing.setUpdatedBy(callerId);
              existing.setVersionNumber(existing.getVersionNumber() + 1);
              return announcementMapper.toDto(announcementRepository.save(existing));
            });
  }
}
