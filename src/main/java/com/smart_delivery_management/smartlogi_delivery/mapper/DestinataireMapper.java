package com.smart_delivery_management.smartlogi_delivery.mapper;

import com.smart_delivery_management.smartlogi_delivery.dto.DestinataireDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.Destinataire;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DestinataireMapper {
    DestinataireDTO toDto(Destinataire entity);
    Destinataire toEntity(DestinataireDTO dto);
}
