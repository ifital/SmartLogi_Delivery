package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.entity.Livreur;
import com.smart_delivery_management.smartlogi_delivery.repository.LivreurRepository;
import com.smart_delivery_management.smartlogi_delivery.service.LivreurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LivreurServiceImpl implements LivreurService {

    private final LivreurRepository livreurRepository;

    @Override
    @Transactional
    public Livreur save(Livreur livreur) {
        log.info("Enregistrement d'un livreur: {} {}", livreur.getNom(), livreur.getPrenom());
        try {
            Livreur saved = livreurRepository.save(livreur);
            log.info("Livreur enregistré: id={}, nom={} {}, zone={}",
                    saved.getId(), saved.getNom(), saved.getPrenom(),
                    saved.getZoneAssignee() != null ? saved.getZoneAssignee().getNom() : "Non assignée");
            return saved;
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement du livreur: {} {}", livreur.getNom(), livreur.getPrenom(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Livreur> findById(String id) {
        log.debug("Recherche du livreur par ID: {}", id);
        return livreurRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Livreur> findAll(Pageable pageable) {
        log.debug("Récupération paginée de tous les livreurs");
        Page<Livreur> result = livreurRepository.findAll(pageable);
        log.info("Page {} - Livreurs récupérés: {}", pageable.getPageNumber(), result.getNumberOfElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Livreur> searchByNomOrPrenom(String nom, String prenom, Pageable pageable) {
        log.debug("Recherche paginée de livreurs par nom/prénom: {}, {}", nom, prenom);
        Page<Livreur> result = livreurRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(nom, prenom, pageable);
        log.info("Livreurs trouvés (nom/prénom) '{}', '{}': {}", nom, prenom, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Livreur> findByZoneAssigneeId(String zoneId, Pageable pageable) {
        log.debug("Recherche paginée des livreurs assignés à la zone: {}", zoneId);
        Page<Livreur> result = livreurRepository.findByZoneAssigneeId(zoneId, pageable);
        log.info("Livreurs trouvés pour la zone {}: {}", zoneId, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Livreur> findLivreursByZone(String zoneId, Pageable pageable) {
        log.debug("Recherche paginée des livreurs de la zone (requête personnalisée): {}", zoneId);
        Page<Livreur> result = livreurRepository.findLivreursByZone(zoneId, pageable);
        log.info("Livreurs récupérés pour la zone {}: {}", zoneId, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        log.info("Suppression du livreur: {}", id);
        try {
            livreurRepository.deleteById(id);
            log.info("Livreur supprimé avec succès: {}", id);
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du livreur: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        log.debug("Vérification existence livreur: {}", id);
        return livreurRepository.existsById(id);
    }
}
