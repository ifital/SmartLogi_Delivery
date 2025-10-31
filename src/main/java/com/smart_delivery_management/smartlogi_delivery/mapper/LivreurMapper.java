package com.smart_delivery_management.smartlogi_delivery.mapper;

import com.smart_delivery_management.smartlogi_delivery.dto.LivreurDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.Livreur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LivreurMapper {
    @Mapping(source = "zoneAssignee.id", target = "zoneAssigneeId")
    @Mapping(source = "zoneAssignee.nom", target = "zoneAssigneeNom")
    LivreurDTO toDto(Livreur entity);

    @Mapping(source = "zoneAssigneeId", target = "zoneAssignee.id")
    Livreur toEntity(LivreurDTO dto);
}


