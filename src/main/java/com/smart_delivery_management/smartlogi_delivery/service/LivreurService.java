package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entity.Livreur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LivreurService {

    Livreur save(Livreur livreur);

    Optional<Livreur> findById(String id);

    Page<Livreur> findAll(Pageable pageable);

    Page<Livreur> searchByNomOrPrenom(String nom, String prenom, Pageable pageable);

    Page<Livreur> findByZoneAssigneeId(String zoneId, Pageable pageable);

    Page<Livreur> findLivreursByZone(String zoneId, Pageable pageable);

    void deleteById(String id);

    boolean existsById(String id);
}
