package com.linktic.inventarios.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InventoryMapper {

    InventoryMapper mapper = Mappers.getMapper(InventoryMapper.class);

}
