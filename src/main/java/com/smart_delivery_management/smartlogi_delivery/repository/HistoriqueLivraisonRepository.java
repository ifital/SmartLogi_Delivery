package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entities.HistoriqueLivraison;
import com.smart_delivery_management.smartlogi_delivery.entities.enums.StatutColis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriqueLivraisonRepository extends JpaRepository<HistoriqueLivraison, Long> {
    List<HistoriqueLivraison> findByColisIdOrderByDateChangementDesc(Long colisId);
    List<HistoriqueLivraison> findByStatut(StatutColis statut);
}
