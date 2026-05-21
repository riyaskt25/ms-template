package com.snb.ms.company;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(source = "user.emailAddress", target = "emailAddress")
    @Mapping(source = "user.mobileNumber", target = "mobileNumber")
    CompanyResponse toDto(Company company);

    @Mapping(source = "user.emailAddress", target = "emailAddress")
    @Mapping(source = "user.mobileNumber", target = "mobileNumber")
    CompanyWriteResponse toWriteDto(Company company);

    List<CompanyResponse> toDtoList(List<Company> companies);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "companyId", ignore = true)
    @Mapping(target = "companyStatus", ignore = true)
    @Mapping(target = "companyType", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedFlag", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "versionNumber", ignore = true)
    Company toEntity(CompanyCreateRequest request);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "companyId", ignore = true)
    @Mapping(target = "companyStatus", ignore = true)
    @Mapping(target = "companyType", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedFlag", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "versionNumber", ignore = true)
    void updateEntity(CompanyUpdateRequest request, @MappingTarget Company company);
}
