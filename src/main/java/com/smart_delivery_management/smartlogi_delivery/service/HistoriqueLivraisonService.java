package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entities.HistoriqueLivraison;
import com.smart_delivery_management.smartlogi_delivery.entities.enums.StatutColis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HistoriqueLivraisonService {
    HistoriqueLivraison save(HistoriqueLivraison historiqueLivraison);
    Optional<HistoriqueLivraison> findById(String id);
    Page<HistoriqueLivraison> findAll(Pageable pageable);
    Page<HistoriqueLivraison> findByColisIdOrderByDateDesc(String colisId, Pageable pageable);
    Page<HistoriqueLivraison> findByStatut(StatutColis statut, Pageable pageable);
    void deleteById(String id);
    boolean existsById(String id);
}