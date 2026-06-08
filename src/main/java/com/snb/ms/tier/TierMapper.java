package com.snb.ms.tier;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TierMapper {

  TierResponse toDto(Tier tier);

  List<TierResponse> toDtoList(List<Tier> tiers);

  @Mapping(target = "tierId", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "deletedFlag", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "versionNumber", ignore = true)
  Tier toEntity(TierCreateRequest request);

  @Mapping(target = "tierId", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "deletedFlag", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "versionNumber", ignore = true)
  void updateEntity(TierUpdateRequest request, @MappingTarget Tier tier);
}
