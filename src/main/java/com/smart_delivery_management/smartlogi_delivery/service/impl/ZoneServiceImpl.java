package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.entities.Zone;
import com.smart_delivery_management.smartlogi_delivery.repository.ZoneRepository;
import com.smart_delivery_management.smartlogi_delivery.service.ZoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;

    @Override
    @Transactional
    public Zone save(Zone zone) {
        log.info("Enregistrement d'une zone: nom={}, codePostal={}",
                zone.getNom(), zone.getCodePostal());
        try {
            Zone saved = zoneRepository.save(zone);
            log.info("Zone enregistrée avec succès: id={}, nom={}, codePostal={}",
                    saved.getId(), saved.getNom(), saved.getCodePostal());
            return saved;
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de la zone: nom={}, codePostal={}",
                    zone.getNom(), zone.getCodePostal(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Zone> findById(String id) {
        log.debug("Recherche de la zone par ID: {}", id);
        Optional<Zone> result = zoneRepository.findById(id);
        if (result.isPresent()) {
            log.debug("Zone trouvée: id={}, nom={}, codePostal={}",
                    id, result.get().getNom(), result.get().getCodePostal());
        } else {
            log.debug("Zone non trouvée avec l'ID: {}", id);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Zone> findAll() {
        log.debug("Récupération de toutes les zones");
        List<Zone> result = zoneRepository.findAll();
        log.info("Nombre total de zones récupérées: {}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Zone> searchByNom(String nom) {
        log.debug("Recherche de zones par nom: {}", nom);
        List<Zone> result = zoneRepository.findByNomContainingIgnoreCase(nom);
        log.info("Nombre de zones trouvées avec le nom contenant '{}': {}", nom, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Zone> findByCodePostal(String codePostal) {
        log.debug("Recherche de zones par code postal: {}", codePostal);
        List<Zone> result = zoneRepository.findByCodePostal(codePostal);
        log.info("Nombre de zones trouvées avec le code postal '{}': {}", codePostal, result.size());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        log.info("Suppression de la zone: {}", id);
        try {
            Optional<Zone> zone = zoneRepository.findById(id);
            if (zone.isPresent()) {
                log.info("Suppression de la zone: nom={}, codePostal={}",
                        zone.get().getNom(), zone.get().getCodePostal());
            }
            zoneRepository.deleteById(id);
            log.info("Zone supprimée avec succès: {}", id);
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de la zone: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        log.debug("Vérification de l'existence de la zone: {}", id);
        boolean exists = zoneRepository.existsById(id);
        log.debug("Zone existe: {}", exists);
        return exists;
    }
}