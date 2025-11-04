package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.entities.HistoriqueLivraison;
import com.smart_delivery_management.smartlogi_delivery.entities.enums.StatutColis;
import com.smart_delivery_management.smartlogi_delivery.repository.HistoriqueLivraisonRepository;
import com.smart_delivery_management.smartlogi_delivery.service.HistoriqueLivraisonService;
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
public class HistoriqueLivraisonServiceImpl implements HistoriqueLivraisonService {

    private final HistoriqueLivraisonRepository historiqueLivraisonRepository;

    @Override
    @Transactional
    public HistoriqueLivraison save(HistoriqueLivraison historiqueLivraison) {
        log.info("Enregistrement d'un historique de livraison: colisId={}, statut={}",
                historiqueLivraison.getColis().getId(), historiqueLivraison.getStatut());
        try {
            HistoriqueLivraison saved = historiqueLivraisonRepository.save(historiqueLivraison);
            log.info("Historique de livraison enregistré avec succès: id={}, colisId={}, statut={}",
                    saved.getId(), saved.getColis().getId(), saved.getStatut());
            return saved;
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'historique de livraison: colisId={}, statut={}",
                    historiqueLivraison.getColis().getId(), historiqueLivraison.getStatut(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriqueLivraison> findById(String id) {
        log.debug("Recherche de l'historique de livraison par ID: {}", id);
        Optional<HistoriqueLivraison> result = historiqueLivraisonRepository.findById(id);
        if (result.isPresent()) {
            log.debug("Historique de livraison trouvé: id={}, colisId={}, statut={}",
                    id, result.get().getColis().getId(), result.get().getStatut());
        } else {
            log.debug("Historique de livraison non trouvé avec l'ID: {}", id);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueLivraison> findAll(Pageable  pageable) {
        log.debug("Récupération de tous les historiques de livraison");
        Page<HistoriqueLivraison> result = historiqueLivraisonRepository.findAll(pageable);
        log.info("Nombre total d'historiques de livraison récupérés: {}", result.getTotalElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueLivraison> findByColisIdOrderByDateDesc(String colisId, Pageable pageable) {
        log.debug("Recherche de l'historique pour le colis: {}", colisId);
        Page<HistoriqueLivraison> result = historiqueLivraisonRepository
                .findByColisIdOrderByDateChangementDesc(colisId,  pageable);
        log.info("Nombre d'entrées d'historique trouvées pour le colis {}: {}", colisId, result.getTotalElements());
        if (!result.isEmpty()) {
            log.debug("Dernier statut du colis {}: {}", colisId, result.getContent().get(0).getStatut());
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueLivraison> findByStatut(StatutColis statut, Pageable pageable) {
        log.debug("Recherche des historiques par statut: {}", statut);
        Page<HistoriqueLivraison> result = historiqueLivraisonRepository.findByStatut(statut, pageable);
        log.info("Nombre d'historiques trouvés avec le statut '{}': {}", statut, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        log.info("Suppression de l'historique de livraison: {}", id);
        try {
            historiqueLivraisonRepository.deleteById(id);
            log.info("Historique de livraison supprimé avec succès: {}", id);
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de l'historique de livraison: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        log.debug("Vérification de l'existence de l'historique de livraison: {}", id);
        boolean exists = historiqueLivraisonRepository.existsById(id);
        log.debug("Historique de livraison existe: {}", exists);
        return exists;
    }
}