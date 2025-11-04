package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entities.Zone;
import java.util.List;
import java.util.Optional;

public interface ZoneService {
    Zone save(Zone zone);
    Optional<Zone> findById(String id);
    List<Zone> findAll();
    List<Zone> searchByNom(String nom);
    List<Zone> findByCodePostal(String codePostal);
    void deleteById(String id);
    boolean existsById(String id);
}