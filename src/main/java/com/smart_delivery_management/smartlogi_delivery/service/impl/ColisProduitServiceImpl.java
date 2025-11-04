package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.entities.ColisProduit;
import com.smart_delivery_management.smartlogi_delivery.entities.ColisProduitId;
import com.smart_delivery_management.smartlogi_delivery.repository.ColisProduitRepository;
import com.smart_delivery_management.smartlogi_delivery.service.ColisProduitService;
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
public class ColisProduitServiceImpl implements ColisProduitService {

    private final ColisProduitRepository colisProduitRepository;

    @Override
    @Transactional
    public ColisProduit save(ColisProduit colisProduit) {
        log.info("Enregistrement d'une association colis-produit: colisId={}, produitId={}",
                colisProduit.getId().getColisId(), colisProduit.getId().getProduitId());
        try {
            ColisProduit saved = colisProduitRepository.save(colisProduit);
            log.info("Association colis-produit enregistrée avec succès: colisId={}, produitId={}",
                    saved.getId().getColisId(), saved.getId().getProduitId());
            return saved;
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'association colis-produit: colisId={}, produitId={}",
                    colisProduit.getId().getColisId(), colisProduit.getId().getProduitId(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ColisProduit> findById(ColisProduitId id) {
        log.debug("Recherche d'une association colis-produit par ID: colisId={}, produitId={}",
                id.getColisId(), id.getProduitId());
        Optional<ColisProduit> result = colisProduitRepository.findById(id);
        if (result.isPresent()) {
            log.debug("Association colis-produit trouvée: colisId={}, produitId={}",
                    id.getColisId(), id.getProduitId());
        } else {
            log.debug("Association colis-produit non trouvée: colisId={}, produitId={}",
                    id.getColisId(), id.getProduitId());
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ColisProduit> findAll(Pageable pageable) {
        log.debug("Récupération de toutes les associations colis-produit");
        Page<ColisProduit> result = colisProduitRepository.findAll(pageable);
        log.info("Nombre total d'associations colis-produit récupérées: {}", result.getTotalElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ColisProduit> findByColisId(String colisId, Pageable pageable) {
        log.debug("Recherche paginée des produits pour le colis: {}", colisId);
        Page<ColisProduit> result = colisProduitRepository.findByColisId(colisId, pageable);
        log.info("Nombre de produits trouvés pour le colis {}: {}", colisId, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ColisProduit> findByProduitId(String produitId, Pageable pageable) {
        log.debug("Recherche paginée des colis contenant le produit: {}", produitId);
        Page<ColisProduit> result = colisProduitRepository.findByProduitId(produitId, pageable);
        log.info("Nombre de colis trouvés contenant le produit {}: {}", produitId, result.getTotalElements());
        return result;
    }


    @Override
    @Transactional
    public void deleteById(ColisProduitId id) {
        log.info("Suppression de l'association colis-produit: colisId={}, produitId={}",
                id.getColisId(), id.getProduitId());
        try {
            colisProduitRepository.deleteById(id);
            log.info("Association colis-produit supprimée avec succès: colisId={}, produitId={}",
                    id.getColisId(), id.getProduitId());
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de l'association colis-produit: colisId={}, produitId={}",
                    id.getColisId(), id.getProduitId(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteByColisId(String colisId) {
        log.info("Suppression de toutes les associations pour le colis: {}", colisId);
        try {
            colisProduitRepository.deleteByColisId(colisId);
            log.info("Toutes les associations du colis {} supprimées avec succès", colisId);
        } catch (Exception e) {
            log.error("Erreur lors de la suppression des associations du colis: {}", colisId, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ColisProduitId id) {
        log.debug("Vérification de l'existence de l'association colis-produit: colisId={}, produitId={}",
                id.getColisId(), id.getProduitId());
        boolean exists = colisProduitRepository.existsById(id);
        log.debug("Association colis-produit existe: {}", exists);
        return exists;
    }
}