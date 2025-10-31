package com.smart_delivery_management.smartlogi_delivery.mapper;

import com.smart_delivery_management.smartlogi_delivery.dto.ClientExpediteurDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.ClientExpediteur;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientExpediteurMapper {
    ClientExpediteurDTO toDto(ClientExpediteur entity);
    ClientExpediteur toEntity(ClientExpediteurDTO dto);
}