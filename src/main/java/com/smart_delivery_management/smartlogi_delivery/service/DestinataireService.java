package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entities.Destinataire;
import java.util.List;
import java.util.Optional;

public interface DestinataireService {
    Destinataire save(Destinataire destinataire);
    Optional<Destinataire> findById(String id);
    List<Destinataire> findAll();
    List<Destinataire> searchByNomOrPrenom(String nom, String prenom);
    List<Destinataire> findByTelephone(String telephone);
    void deleteById(String id);
    boolean existsById(String id);
}