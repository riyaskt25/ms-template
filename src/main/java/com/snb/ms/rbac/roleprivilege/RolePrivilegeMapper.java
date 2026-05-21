package com.snb.ms.rbac.roleprivilege;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolePrivilegeMapper {

    @Mapping(target = "roleId", source = "role.roleId")
    @Mapping(target = "roleCode", source = "role.roleCode")
    @Mapping(target = "privilegeId", source = "privilege.privilegeId")
    @Mapping(target = "privilegeCode", source = "privilege.privilegeCode")
    @Mapping(target = "privilegeName", source = "privilege.privilegeName")
    RolePrivilegeResponse toDto(RolePrivilege rolePrivilege);

    List<RolePrivilegeResponse> toDtoList(List<RolePrivilege> rolePrivileges);
}
