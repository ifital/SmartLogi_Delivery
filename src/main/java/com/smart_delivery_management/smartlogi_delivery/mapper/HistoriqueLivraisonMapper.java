package com.smart_delivery_management.smartlogi_delivery.mapper;

import com.smart_delivery_management.smartlogi_delivery.dto.HistoriqueLivraisonDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.HistoriqueLivraison;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HistoriqueLivraisonMapper {

    HistoriqueLivraisonDTO toDto(HistoriqueLivraison entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "colis", ignore = true)
    @Mapping(target = "dateChangement", ignore = true)
    HistoriqueLivraison toEntity(HistoriqueLivraisonDTO dto);
}
