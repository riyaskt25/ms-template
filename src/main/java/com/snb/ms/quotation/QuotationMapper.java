package com.snb.ms.quotation;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuotationMapper {

  @Mapping(source = "company.companyId", target = "companyId")
  @Mapping(source = "salesman.salesmanId", target = "salesmanId")
  QuotationResponse toDto(Quotation quotation);

  List<QuotationResponse> toDtoList(List<Quotation> quotations);

  @Mapping(target = "quotationId", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "salesman", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "deletedFlag", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "versionNumber", ignore = true)
  Quotation toEntity(QuotationCreateRequest request);

  @Mapping(target = "quotationId", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "salesman", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "deletedFlag", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "versionNumber", ignore = true)
  void updateEntity(QuotationUpdateRequest request, @MappingTarget Quotation quotation);
}
