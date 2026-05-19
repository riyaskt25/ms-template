package com.snb.ms.salesman;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalesmanMapper {

    SalesmanResponse toDto(Salesman salesman);

    List<SalesmanResponse> toDtoList(List<Salesman> salesmen);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "salesmanId", ignore = true)
    @Mapping(target = "availableIncentiveAmount", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedFlag", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "versionNumber", ignore = true)
    Salesman toEntity(SalesmanCreateRequest request);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "salesmanId", ignore = true)
    @Mapping(target = "availableIncentiveAmount", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedFlag", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "versionNumber", ignore = true)
    void updateEntity(SalesmanUpdateRequest request, @MappingTarget Salesman salesman);
}
