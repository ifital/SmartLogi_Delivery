package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entities.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ZoneService {

    Zone save(Zone zone);

    Optional<Zone> findById(String id);

    Page<Zone> findAll(Pageable pageable);

    Page<Zone> searchByNom(String nom, Pageable pageable);

    Page<Zone> findByCodePostal(String codePostal, Pageable pageable);

    void deleteById(String id);

    boolean existsById(String id);
}
