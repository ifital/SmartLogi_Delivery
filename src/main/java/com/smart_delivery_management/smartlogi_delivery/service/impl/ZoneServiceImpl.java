package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.entities.Zone;
import com.smart_delivery_management.smartlogi_delivery.repository.ZoneRepository;
import com.smart_delivery_management.smartlogi_delivery.service.ZoneService;
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
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;

    @Override
    @Transactional
    public Zone save(Zone zone) {
        log.info("Enregistrement d'une zone: nom={}, codePostal={}", zone.getNom(), zone.getCodePostal());
        try {
            Zone saved = zoneRepository.save(zone);
            log.info("Zone enregistrée avec succès: id={}, nom={}, codePostal={}",
                    saved.getId(), saved.getNom(), saved.getCodePostal());
            return saved;
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de la zone: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Zone> findById(String id) {
        log.debug("Recherche de la zone par ID: {}", id);
        return zoneRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Zone> findAll(Pageable pageable) {
        log.debug("Récupération paginée de toutes les zones");
        Page<Zone> result = zoneRepository.findAll(pageable);
        log.info("Nombre de zones récupérées (page {}): {}", pageable.getPageNumber(), result.getNumberOfElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Zone> searchByNom(String nom, Pageable pageable) {
        log.debug("Recherche de zones par nom (paginée): {}", nom);
        Page<Zone> result = zoneRepository.findByNomContainingIgnoreCase(nom, pageable);
        log.info("Zones trouvées contenant '{}': {}", nom, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Zone> findByCodePostal(String codePostal, Pageable pageable) {
        log.debug("Recherche de zones par code postal (paginée): {}", codePostal);
        Page<Zone> result = zoneRepository.findByCodePostal(codePostal, pageable);
        log.info("Zones trouvées avec le code postal '{}': {}", codePostal, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        log.info("Suppression de la zone: {}", id);
        try {
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
        return zoneRepository.existsById(id);
    }
}
