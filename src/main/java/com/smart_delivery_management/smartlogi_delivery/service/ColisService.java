package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.dto.*;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.PrioriteColis;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ColisService {

    ColisDTO createColis(ColisCreateDTO createDTO);

    ColisDTO getColisById(String id);

    Page<ColisDTO> getAllColis(Pageable pageable);

    ColisDTO updateColis(String id, ColisDTO colisDTO);

    void deleteColis(String id);

    ColisDTO assignerLivreur(String colisId, String livreurId);

    ColisDTO updateStatut(String colisId, StatutColis nouveauStatut, String commentaire);

    Page<ColisDTO> searchColis(ColisSearchCriteria criteria, Pageable pageable);

    Page<ColisDTO> getColisByClient(String clientId, Pageable pageable);

    List<ColisDTO> getColisByDestinataire(String destinataireId);

    Page<HistoriqueLivraisonDTO> getHistorique(String colisId, Pageable pageable);

    ColisStatisticsDTO getStatisticsByLivreur(String livreurId);

    ColisStatisticsDTO getStatisticsByZone(String zoneId);

    List<ColisDTO> getColisEnRetard();

    List<ColisDTO> getColisPrioritairesNonAssignes();

    List<Object[]> countByStatutGroupBy();

    List<Object[]> countByZoneGroupBy();

    List<Object[]> countByPrioriteGroupBy();

    Page<ColisDTO> getColisByStatut(StatutColis statut, Pageable pageable);

    Page<ColisDTO> getColisByPriorite(PrioriteColis priorite, Pageable pageable);

    Page<ColisDTO> getColisByZone(String zoneId, Pageable pageable);

    Page<ColisDTO> getColisByVille(String ville, Pageable pageable);

    Page<ColisDTO> getColisByLivreur(String livreurId, Pageable pageable);
}
