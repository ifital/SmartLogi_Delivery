package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entities.ColisProduit;
import com.smart_delivery_management.smartlogi_delivery.entities.ColisProduitId;
import java.util.List;
import java.util.Optional;

public interface ColisProduitService {
    ColisProduit save(ColisProduit colisProduit);
    Optional<ColisProduit> findById(ColisProduitId id);
    List<ColisProduit> findAll();
    List<ColisProduit> findByColisId(String colisId);
    List<ColisProduit> findByProduitId(String produitId);
    void deleteById(ColisProduitId id);
    void deleteByColisId(String colisId);
    boolean existsById(ColisProduitId id);
}