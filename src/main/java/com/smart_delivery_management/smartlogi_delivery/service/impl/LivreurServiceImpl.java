package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.entities.Livreur;
import com.smart_delivery_management.smartlogi_delivery.repository.LivreurRepository;
import com.smart_delivery_management.smartlogi_delivery.service.LivreurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
            log.info("Livreur enregistré avec succès: id={}, nom={} {}, zone={}",
                    saved.getId(), saved.getNom(), saved.getPrenom(),
                    saved.getZoneAssignee() != null ? saved.getZoneAssignee().getNom() : "Non assignée");
            return saved;
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement du livreur: {} {}",
                    livreur.getNom(), livreur.getPrenom(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Livreur> findById(String id) {
        log.debug("Recherche du livreur par ID: {}", id);
        Optional<Livreur> result = livreurRepository.findById(id);
        if (result.isPresent()) {
            log.debug("Livreur trouvé: id={}, nom={} {}",
                    id, result.get().getNom(), result.get().getPrenom());
        } else {
            log.debug("Livreur non trouvé avec l'ID: {}", id);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livreur> findAll() {
        log.debug("Récupération de tous les livreurs");
        List<Livreur> result = livreurRepository.findAll();
        log.info("Nombre total de livreurs récupérés: {}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livreur> searchByNomOrPrenom(String nom, String prenom) {
        log.debug("Recherche de livreurs par nom ou prénom: nom={}, prenom={}", nom, prenom);
        List<Livreur> result = livreurRepository
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(nom, prenom);
        log.info("Nombre de livreurs trouvés avec nom/prénom '{}' ou '{}': {}", nom, prenom, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livreur> findByZoneAssigneeId(String zoneId) {
        log.debug("Recherche des livreurs assignés à la zone: {}", zoneId);
        List<Livreur> result = livreurRepository.findByZoneAssigneeId(zoneId);
        log.info("Nombre de livreurs trouvés pour la zone {}: {}", zoneId, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livreur> findLivreursByZone(String zoneId) {
        log.debug("Recherche des livreurs de la zone (méthode personnalisée): {}", zoneId);
        List<Livreur> result = livreurRepository.findLivreursByZone(zoneId);
        log.info("Nombre de livreurs récupérés pour la zone {}: {}", zoneId, result.size());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        log.info("Suppression du livreur: {}", id);
        try {
            Optional<Livreur> livreur = livreurRepository.findById(id);
            if (livreur.isPresent()) {
                log.info("Suppression du livreur: {} {}",
                        livreur.get().getNom(), livreur.get().getPrenom());
            }
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
        log.debug("Vérification de l'existence du livreur: {}", id);
        boolean exists = livreurRepository.existsById(id);
        log.debug("Livreur existe: {}", exists);
        return exists;
    }
}