package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entities.Livreur;
import java.util.List;
import java.util.Optional;

public interface LivreurService {
    Livreur save(Livreur livreur);
    Optional<Livreur> findById(String id);
    List<Livreur> findAll();
    List<Livreur> searchByNomOrPrenom(String nom, String prenom);
    List<Livreur> findByZoneAssigneeId(String zoneId);
    List<Livreur> findLivreursByZone(String zoneId);
    void deleteById(String id);
    boolean existsById(String id);
}