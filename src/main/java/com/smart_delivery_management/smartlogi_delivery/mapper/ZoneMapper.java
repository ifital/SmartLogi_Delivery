package com.smart_delivery_management.smartlogi_delivery.mapper;


import com.smart_delivery_management.smartlogi_delivery.dto.ZoneDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ZoneMapper {
    ZoneDTO toDto(Zone entity);
    Zone toEntity(ZoneDTO dto);
}
