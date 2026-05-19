package com.snb.ms.rbac.userrole;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "roleId", source = "role.roleId")
    @Mapping(target = "roleCode", source = "role.roleCode")
    @Mapping(target = "roleName", source = "role.roleName")
    UserRoleResponse toDto(UserRole userRole);

    List<UserRoleResponse> toDtoList(List<UserRole> userRoles);
}
