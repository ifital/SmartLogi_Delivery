package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entities.Destinataire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DestinataireService {
    Destinataire save(Destinataire destinataire);
    Optional<Destinataire> findById(String id);
    Page<Destinataire> findAll(Pageable pageable);
    Page<Destinataire> searchByNomOrPrenom(String nom, String prenom, Pageable pageable);
    Page<Destinataire> findByTelephone(String telephone, Pageable pageable);
    void deleteById(String id);
    boolean existsById(String id);
}