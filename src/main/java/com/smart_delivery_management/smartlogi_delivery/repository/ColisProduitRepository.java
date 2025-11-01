package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entities.ColisProduit;
import com.smart_delivery_management.smartlogi_delivery.entities.ColisProduitId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ColisProduitRepository extends JpaRepository<ColisProduit, ColisProduitId> {
    List<ColisProduit> findByColisId(String colisId);
    List<ColisProduit> findByProduitId(String produitId);
    void deleteByColisId(String colisId);
}
