package com.smart_delivery_management.smartlogi_delivery.mapper;

import com.smart_delivery_management.smartlogi_delivery.dto.ColisProduitDTO;
import com.smart_delivery_management.smartlogi_delivery.entity.ColisProduit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ColisProduitMapper {

    @Mapping(source = "produit.id", target = "produitId")
    @Mapping(source = "produit.nom", target = "produitNom")
    ColisProduitDTO toDto(ColisProduit entity);

    @Mapping(source = "produitId", target = "produit.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "colis", ignore = true)
    ColisProduit toEntity(ColisProduitDTO dto);
}
