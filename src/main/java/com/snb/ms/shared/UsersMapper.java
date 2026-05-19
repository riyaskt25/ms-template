package com.snb.ms.shared;

import com.snb.ms.shared.UsersDto;
import com.snb.ms.shared.UsersRequest;
import com.snb.ms.shared.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    UsersDto toDto(Users users);

    List<UsersDto> toDtoList(List<Users> users);

    Users toEntity(UsersRequest request);

    void updateEntity(UsersRequest request, @MappingTarget Users users);
}
