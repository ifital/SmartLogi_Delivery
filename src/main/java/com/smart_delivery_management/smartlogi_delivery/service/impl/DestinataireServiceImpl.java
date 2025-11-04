package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.entities.Destinataire;
import com.smart_delivery_management.smartlogi_delivery.repository.DestinataireRepository;
import com.smart_delivery_management.smartlogi_delivery.service.DestinataireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
            log.info("Destinataire enregistré avec succès: id={}, nom={} {}",
                    saved.getId(), saved.getNom(), saved.getPrenom());
            return saved;
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement du destinataire: {} {}",
                    destinataire.getNom(), destinataire.getPrenom(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Destinataire> findById(String id) {
        log.debug("Recherche du destinataire par ID: {}", id);
        Optional<Destinataire> result = destinataireRepository.findById(id);
        if (result.isPresent()) {
            log.debug("Destinataire trouvé: id={}, nom={} {}",
                    id, result.get().getNom(), result.get().getPrenom());
        } else {
            log.debug("Destinataire non trouvé avec l'ID: {}", id);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Destinataire> findAll() {
        log.debug("Récupération de tous les destinataires");
        List<Destinataire> result = destinataireRepository.findAll();
        log.info("Nombre total de destinataires récupérés: {}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Destinataire> searchByNomOrPrenom(String nom, String prenom) {
        log.debug("Recherche de destinataires par nom ou prénom: nom={}, prenom={}", nom, prenom);
        List<Destinataire> result = destinataireRepository
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(nom, prenom);
        log.info("Nombre de destinataires trouvés avec nom/prénom '{}' ou '{}': {}", nom, prenom, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Destinataire> findByTelephone(String telephone) {
        log.debug("Recherche de destinataires par téléphone: {}", telephone);
        List<Destinataire> result = destinataireRepository.findByTelephone(telephone);
        log.info("Nombre de destinataires trouvés avec le téléphone '{}': {}", telephone, result.size());
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
        log.debug("Vérification de l'existence du destinataire: {}", id);
        boolean exists = destinataireRepository.existsById(id);
        log.debug("Destinataire existe: {}", exists);
        return exists;
    }
}