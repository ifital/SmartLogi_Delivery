package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.entities.Produit;
import com.smart_delivery_management.smartlogi_delivery.repository.ProduitRepository;
import com.smart_delivery_management.smartlogi_delivery.service.ProduitService;
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
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;

    @Override
    @Transactional
    public Produit save(Produit produit) {
        log.info("Enregistrement d'un produit: nom={}, catégorie={}", produit.getNom(), produit.getCategorie());
        try {
            Produit saved = produitRepository.save(produit);
            log.info("Produit enregistré avec succès: id={}, nom={}, prix={}",
                    saved.getId(), saved.getNom(), saved.getPrix());
            return saved;
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement du produit: {}", produit.getNom(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Produit> findById(String id) {
        log.debug("Recherche du produit par ID: {}", id);
        return produitRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Produit> findAll(Pageable pageable) {
        log.debug("Récupération paginée de tous les produits");
        Page<Produit> result = produitRepository.findAll(pageable);
        log.info("Page {} - Produits récupérés: {}", pageable.getPageNumber(), result.getNumberOfElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Produit> searchByNom(String nom, Pageable pageable) {
        log.debug("Recherche paginée de produits par nom: {}", nom);
        Page<Produit> result = produitRepository.findByNomContainingIgnoreCase(nom, pageable);
        log.info("Produits trouvés contenant '{}': {}", nom, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Produit> findByCategorie(String categorie, Pageable pageable) {
        log.debug("Recherche paginée de produits par catégorie: {}", categorie);
        Page<Produit> result = produitRepository.findByCategorie(categorie, pageable);
        log.info("Produits trouvés dans la catégorie '{}': {}", categorie, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        log.info("Suppression du produit: {}", id);
        try {
            produitRepository.deleteById(id);
            log.info("Produit supprimé avec succès: {}", id);
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du produit: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        log.debug("Vérification de l'existence du produit: {}", id);
        return produitRepository.existsById(id);
    }
}
