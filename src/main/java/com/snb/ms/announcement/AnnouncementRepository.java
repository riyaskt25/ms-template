package com.snb.ms.announcement;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository
    extends JpaRepository<Announcement, Long>, JpaSpecificationExecutor<Announcement> {

  @Query("SELECT a FROM Announcement a WHERE a.announcementId = :id AND a.deletedFlag = 'N'")
  Optional<Announcement> findActiveById(@Param("id") Long id);
}
