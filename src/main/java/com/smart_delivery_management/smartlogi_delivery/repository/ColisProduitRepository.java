package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entities.ColisProduit;
import com.smart_delivery_management.smartlogi_delivery.entities.ColisProduitId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ColisProduitRepository extends JpaRepository<ColisProduit, ColisProduitId> {


    Page<ColisProduit> findByColisId(String colisId, Pageable pageable);
    Page<ColisProduit> findByProduitId(String produitId, Pageable pageable);

    void deleteByColisId(String colisId);
}

