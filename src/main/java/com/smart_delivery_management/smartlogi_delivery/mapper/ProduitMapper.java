package com.smart_delivery_management.smartlogi_delivery.mapper;

import com.smart_delivery_management.smartlogi_delivery.dto.ProduitDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProduitMapper {
    ProduitDTO toDto(Produit entity);
    Produit toEntity(ProduitDTO dto);
}
