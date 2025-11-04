package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entities.HistoriqueLivraison;
import com.smart_delivery_management.smartlogi_delivery.entities.enums.StatutColis;
import java.util.List;
import java.util.Optional;

public interface HistoriqueLivraisonService {
    HistoriqueLivraison save(HistoriqueLivraison historiqueLivraison);
    Optional<HistoriqueLivraison> findById(String id);
    List<HistoriqueLivraison> findAll();
    List<HistoriqueLivraison> findByColisIdOrderByDateDesc(String colisId);
    List<HistoriqueLivraison> findByStatut(StatutColis statut);
    void deleteById(String id);
    boolean existsById(String id);
}