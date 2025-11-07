package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.Produit;
import com.smart_delivery_management.smartlogi_delivery.service.ProduitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@Slf4j
public class ProduitController {

    private final ProduitService produitService;

    // ------------------- CREATE -------------------
    @PostMapping
    public ResponseEntity<Produit> createProduit(@Valid @RequestBody Produit produit) {
        log.info("Appel API: CREATE Produit nom={}, catégorie={}", produit.getNom(), produit.getCategorie());
        Produit saved = produitService.save(produit);
        log.info("Produit créé avec succès: ID={}, nom={}, prix={}", saved.getId(), saved.getNom(), saved.getPrix());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable String id) {
        log.info("Appel API: GET Produit ID={}", id);
        return produitService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Aucun produit trouvé avec ID={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // ------------------- READ ALL (Pagination) -------------------
    @GetMapping
    public ResponseEntity<Page<Produit>> getAllProduits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET All Produits page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Produit> result = produitService.findAll(pageable);
        log.info("Nombre total de produits récupérés: {}", result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR NOM -------------------
    @GetMapping("/search")
    public ResponseEntity<Page<Produit>> searchByNom(
            @RequestParam(required = false, defaultValue = "") String nom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: SEARCH Produit par nom='{}'", nom);
        Pageable pageable = PageRequest.of(page, size);
        Page<Produit> result = produitService.searchByNom(nom, pageable);
        log.info("Résultats: {} produits trouvés", result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR CATÉGORIE -------------------
    @GetMapping("/categorie")
    public ResponseEntity<Page<Produit>> searchByCategorie(
            @RequestParam(required = false, defaultValue = "") String categorie,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: SEARCH Produit par catégorie='{}'", categorie);
        Pageable pageable = PageRequest.of(page, size);
        Page<Produit> result = produitService.findByCategorie(categorie, pageable);
        log.info("Résultats: {} produits trouvés dans la catégorie='{}'", result.getTotalElements(), categorie);
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable String id) {
        log.info("Appel API: DELETE Produit ID={}", id);
        if (!produitService.existsById(id)) {
            log.warn("Tentative de suppression d'un produit inexistant ID={}", id);
            return ResponseEntity.notFound().build();
        }
        produitService.deleteById(id);
        log.info("Produit supprimé: ID={}", id);
        return ResponseEntity.noContent().build();
    }
}
