package com.snb.ms.adminuser;

import com.snb.ms.adminuser.AdminUserResponse;
import com.snb.ms.adminuser.AdminUserCreateRequest;
import com.snb.ms.adminuser.AdminUserUpdateRequest;
import com.snb.ms.adminuser.AdminUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminUserMapper {

    @Mapping(target = "errors", ignore = true)
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
