package com.snb.ms.announcement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snb.ms.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ANNOUNCEMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Announcement extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ANNOUNCEMENT_ID")
  private Long announcementId;

  @Column(name = "TITLE", nullable = false, length = 200)
  private String title;

  @Column(name = "DESCRIPTION", nullable = false, length = 2000)
  private String description;

  @Column(name = "IMAGE_URL", length = 500)
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "PRIORITY", nullable = false, length = 20)
  private AnnouncementPriority priority;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", nullable = false, length = 20)
  private AnnouncementType type;

  @Column(name = "START_DATE", nullable = false)
  private LocalDate startDate;

  @Column(name = "END_DATE", nullable = false)
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false, length = 20)
  private AnnouncementStatus status;

  @Column(name = "ACTION_URL", length = 500)
  private String actionUrl;
}
