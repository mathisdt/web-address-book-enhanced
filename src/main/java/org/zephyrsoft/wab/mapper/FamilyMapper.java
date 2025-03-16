package org.zephyrsoft.wab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.zephyrsoft.wab.model.Family;

@Mapper
public interface FamilyMapper {
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    void map(Family source, @MappingTarget Family target);

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    @Mapping(target = "id", ignore = true)
    void mapWithoutId(Family source, @MappingTarget Family target);
}
