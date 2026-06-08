package com.snb.ms.announcement;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {

  AnnouncementResponse toDto(Announcement announcement);

  List<AnnouncementResponse> toDtoList(List<Announcement> announcements);

  @Mapping(target = "announcementId", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "deletedFlag", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "versionNumber", ignore = true)
  Announcement toEntity(AnnouncementCreateRequest request);

  @Mapping(target = "announcementId", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "deletedFlag", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "versionNumber", ignore = true)
  void updateEntity(AnnouncementUpdateRequest request, @MappingTarget Announcement announcement);
}
