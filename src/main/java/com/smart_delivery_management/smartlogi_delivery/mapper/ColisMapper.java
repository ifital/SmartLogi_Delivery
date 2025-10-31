package com.smart_delivery_management.smartlogi_delivery.mapper;

import com.smart_delivery_management.smartlogi_delivery.dto.ColisCreateDTO;
import com.smart_delivery_management.smartlogi_delivery.dto.ColisDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.Colis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ColisProduitMapper.class})
public interface ColisMapper {

    @Mapping(source = "livreur.id", target = "livreurId")
    @Mapping(source = "livreur.nom", target = "livreurNom")
    @Mapping(source = "clientExpediteur.id", target = "clientExpediteurId")
    @Mapping(source = "clientExpediteur.nom", target = "clientExpediteurNom")
    @Mapping(source = "destinataire.id", target = "destinataireId")
    @Mapping(source = "destinataire.nom", target = "destinataireNom")
    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "zone.nom", target = "zoneNom")
    ColisDTO toDto(Colis entity);

    @Mapping(source = "livreurId", target = "livreur.id")
    @Mapping(source = "clientExpediteurId", target = "clientExpediteur.id")
    @Mapping(source = "destinataireId", target = "destinataire.id")
    @Mapping(source = "zoneId", target = "zone.id")
    @Mapping(target = "historique", ignore = true)
    Colis toEntity(ColisDTO dto);

    @Mapping(source = "clientExpediteurId", target = "clientExpediteur.id")
    @Mapping(source = "destinataireId", target = "destinataire.id")
    @Mapping(source = "zoneId", target = "zone.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statut", constant = "CREE")
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "livreur", ignore = true)
    @Mapping(target = "produits", ignore = true)
    @Mapping(target = "historique", ignore = true)
    Colis toEntityFromCreate(ColisCreateDTO dto);
}
