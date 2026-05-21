package com.snb.ms.companysalesman;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanySalesmanMapper {

    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "salesman.salesmanId", target = "salesmanId")
    CompanySalesmanDto toDto(CompanySalesman companySalesman);

    List<CompanySalesmanDto> toDtoList(List<CompanySalesman> list);

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "salesman", ignore = true)
    @Mapping(target = "companySalesmanId", ignore = true)
    CompanySalesman toEntity(CompanySalesmanRequest request);

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "salesman", ignore = true)
    @Mapping(target = "companySalesmanId", ignore = true)
    void updateEntity(CompanySalesmanRequest request, @MappingTarget CompanySalesman companySalesman);
}
