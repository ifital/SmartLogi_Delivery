package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entities.HistoriqueLivraison;
import com.smart_delivery_management.smartlogi_delivery.entities.enums.StatutColis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriqueLivraisonRepository extends JpaRepository<HistoriqueLivraison, String> {
    Page<HistoriqueLivraison> findByColisIdOrderByDateChangementDesc(String colisId, Pageable pageable);
    Page<HistoriqueLivraison> findByStatut(StatutColis statut, Pageable pageable);
}
