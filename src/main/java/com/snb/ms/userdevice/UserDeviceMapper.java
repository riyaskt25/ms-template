package com.snb.ms.userdevice;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserDeviceMapper {

  @Mapping(source = "user.userId", target = "userId")
  UserDeviceResponse toDto(UserDevice userDevice);

  List<UserDeviceResponse> toDtoList(List<UserDevice> userDevices);

  @Mapping(target = "userDeviceId", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "deletedFlag", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "versionNumber", ignore = true)
  UserDevice toEntity(UserDeviceCreateRequest request);

  @Mapping(target = "userDeviceId", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "deletedFlag", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "versionNumber", ignore = true)
  void updateEntity(UserDeviceUpdateRequest request, @MappingTarget UserDevice userDevice);
}
