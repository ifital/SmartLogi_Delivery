package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entity.HistoriqueLivraison;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueLivraisonRepository extends JpaRepository<HistoriqueLivraison, String> {
    Page<HistoriqueLivraison> findByColisIdOrderByDateChangementDesc(String colisId, Pageable pageable);
    Page<HistoriqueLivraison> findByStatut(StatutColis statut, Pageable pageable);
}
