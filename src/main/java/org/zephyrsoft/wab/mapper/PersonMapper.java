package org.zephyrsoft.wab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;

@Mapper
public interface PersonMapper {
    @Mapping(target = "family", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    void map(Person source, @MappingTarget Person target);

    @Mapping(target = "family", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    @Mapping(target = "id", ignore = true)
    void mapWithoutId(Person source, @MappingTarget Person target);
}
