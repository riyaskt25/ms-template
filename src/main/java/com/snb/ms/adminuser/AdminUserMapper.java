package com.snb.ms.adminuser;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdminUserMapper {

    AdminUserResponse toDto(AdminUser adminUser);

    List<AdminUserResponse> toDtoList(List<AdminUser> adminUsers);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "adminUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedFlag", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "versionNumber", ignore = true)
    @Mapping(target = "adminType", ignore = true)
    @Mapping(target = "adminStatus", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    AdminUser toEntity(AdminUserCreateRequest request);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "adminUserId", ignore = true)
    @Mapping(target = "snbId", ignore = true)
    @Mapping(target = "adminType", ignore = true)
    @Mapping(target = "adminStatus", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedFlag", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "versionNumber", ignore = true)
    void updateEntity(AdminUserUpdateRequest request, @MappingTarget AdminUser adminUser);
}
