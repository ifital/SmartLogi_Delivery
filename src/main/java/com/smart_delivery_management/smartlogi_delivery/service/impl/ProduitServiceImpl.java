package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.entities.Produit;
import com.smart_delivery_management.smartlogi_delivery.repository.ProduitRepository;
import com.smart_delivery_management.smartlogi_delivery.service.ProduitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;

    @Override
    @Transactional
    public Produit save(Produit produit) {
        log.info("Enregistrement d'un produit: nom={}, categorie={}",
                produit.getNom(), produit.getCategorie());
        try {
            Produit saved = produitRepository.save(produit);
            log.info("Produit enregistré avec succès: id={}, nom={}, prix={}",
                    saved.getId(), saved.getNom(), saved.getPrix());
            return saved;
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement du produit: nom={}", produit.getNom(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Produit> findById(String id) {
        log.debug("Recherche du produit par ID: {}", id);
        Optional<Produit> result = produitRepository.findById(id);
        if (result.isPresent()) {
            log.debug("Produit trouvé: id={}, nom={}, categorie={}",
                    id, result.get().getNom(), result.get().getCategorie());
        } else {
            log.debug("Produit non trouvé avec l'ID: {}", id);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> findAll() {
        log.debug("Récupération de tous les produits");
        List<Produit> result = produitRepository.findAll();
        log.info("Nombre total de produits récupérés: {}", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> searchByNom(String nom) {
        log.debug("Recherche de produits par nom: {}", nom);
        List<Produit> result = produitRepository.findByNomContainingIgnoreCase(nom);
        log.info("Nombre de produits trouvés avec le nom contenant '{}': {}", nom, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> findByCategorie(String categorie) {
        log.debug("Recherche de produits par catégorie: {}", categorie);
        List<Produit> result = produitRepository.findByCategorie(categorie);
        log.info("Nombre de produits trouvés dans la catégorie '{}': {}", categorie, result.size());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        log.info("Suppression du produit: {}", id);
        try {
            Optional<Produit> produit = produitRepository.findById(id);
            if (produit.isPresent()) {
                log.info("Suppression du produit: nom={}, categorie={}",
                        produit.get().getNom(), produit.get().getCategorie());
            }
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
        boolean exists = produitRepository.existsById(id);
        log.debug("Produit existe: {}", exists);
        return exists;
    }
}