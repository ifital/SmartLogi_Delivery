package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.entity.Destinataire;
import com.smart_delivery_management.smartlogi_delivery.repository.DestinataireRepository;
import com.smart_delivery_management.smartlogi_delivery.service.DestinataireService;
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
public class DestinataireServiceImpl implements DestinataireService {

    private final DestinataireRepository destinataireRepository;

    @Override
    @Transactional
    public Destinataire save(Destinataire destinataire) {
        log.info("Enregistrement d'un destinataire: {} {}", destinataire.getNom(), destinataire.getPrenom());
        try {
            Destinataire saved = destinataireRepository.save(destinataire);
            log.info("Destinataire enregistré avec succès: id={}, nom={} {}", saved.getId(), saved.getNom(), saved.getPrenom());
            return saved;
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement du destinataire: {} {}", destinataire.getNom(), destinataire.getPrenom(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Destinataire> findById(String id) {
        log.debug("Recherche du destinataire par ID: {}", id);
        Optional<Destinataire> result = destinataireRepository.findById(id);
        result.ifPresentOrElse(
                d -> log.debug("Destinataire trouvé: id={}, nom={} {}", id, d.getNom(), d.getPrenom()),
                () -> log.debug("Aucun destinataire trouvé avec l'ID: {}", id)
        );
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Destinataire> findAll(Pageable pageable) {
        log.debug("Récupération paginée de tous les destinataires");
        Page<Destinataire> result = destinataireRepository.findAll(pageable);
        log.info("Page {} sur {} récupérée, total destinataires: {}",
                result.getNumber() + 1, result.getTotalPages(), result.getTotalElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Destinataire> searchByNomOrPrenom(String nom, String prenom, Pageable pageable) {
        log.debug("Recherche paginée de destinataires: nom={}, prenom={}", nom, prenom);
        Page<Destinataire> result = destinataireRepository
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(nom, prenom, pageable);
        log.info("Résultats trouvés: {} destinataires pour nom/prénom '{}' ou '{}'",
                result.getTotalElements(), nom, prenom);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Destinataire> findByTelephone(String telephone, Pageable pageable) {
        log.debug("Recherche paginée de destinataires par téléphone: {}", telephone);
        Page<Destinataire> result = destinataireRepository.findByTelephone(telephone, pageable);
        log.info("Nombre de destinataires trouvés avec le téléphone '{}': {}", telephone, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        log.info("Suppression du destinataire: {}", id);
        try {
            destinataireRepository.deleteById(id);
            log.info("Destinataire supprimé avec succès: {}", id);
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du destinataire: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        boolean exists = destinataireRepository.existsById(id);
        log.debug("Vérification existence du destinataire ({}): {}", id, exists);
        return exists;
    }
}
