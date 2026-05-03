package com.snb.ms.companysalesman;

import com.snb.ms.companysalesman.CompanySalesmanDto;
import com.snb.ms.companysalesman.CompanySalesmanRequest;
import com.snb.ms.companysalesman.CompanySalesman;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanySalesmanMapper {

    @Mapping(target = "errors", ignore = true)
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
