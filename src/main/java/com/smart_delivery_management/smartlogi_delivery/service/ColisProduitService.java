package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entities.ColisProduit;
import com.smart_delivery_management.smartlogi_delivery.entities.ColisProduitId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ColisProduitService {
    ColisProduit save(ColisProduit colisProduit);
    Optional<ColisProduit> findById(ColisProduitId id);
    Page<ColisProduit> findAll(Pageable pageable);
    Page<ColisProduit> findByColisId(String colisId, Pageable pageable);
    Page<ColisProduit> findByProduitId(String produitId, Pageable pageable);
    void deleteById(ColisProduitId id);
    void deleteByColisId(String colisId);
    boolean existsById(ColisProduitId id);
}