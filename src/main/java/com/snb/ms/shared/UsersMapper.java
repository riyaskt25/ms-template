package com.snb.ms.shared;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsersMapper {

  UsersDto toDto(Users users);

  List<UsersDto> toDtoList(List<Users> users);

  Users toEntity(UsersRequest request);

  void updateEntity(UsersRequest request, @MappingTarget Users users);
}
