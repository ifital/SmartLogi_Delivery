package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entity.ColisProduit;
import com.smart_delivery_management.smartlogi_delivery.entity.ColisProduitId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColisProduitRepository extends JpaRepository<ColisProduit, ColisProduitId> {


    Page<ColisProduit> findByColisId(String colisId, Pageable pageable);
    Page<ColisProduit> findByProduitId(String produitId, Pageable pageable);

    void deleteByColisId(String colisId);
}

