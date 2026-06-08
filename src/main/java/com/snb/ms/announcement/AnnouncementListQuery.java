package com.snb.ms.announcement;

import com.snb.ms.shared.request.BaseListQuery;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementListQuery extends BaseListQuery {

  private String title;

  private AnnouncementPriority priority;

  private AnnouncementType type;

  private AnnouncementStatus status;

  private String description;
}
