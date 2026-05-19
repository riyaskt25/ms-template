package com.snb.ms.rbac.role;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toDto(Role role);

    List<RoleResponse> toDtoList(List<Role> roles);

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedFlag", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "versionNumber", ignore = true)
    Role toEntity(RoleCreateRequest request);

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "roleCode", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedFlag", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "versionNumber", ignore = true)
    void updateEntity(RoleUpdateRequest request, @MappingTarget Role role);
}
