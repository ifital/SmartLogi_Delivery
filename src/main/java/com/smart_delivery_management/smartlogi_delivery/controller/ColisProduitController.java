package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.ColisProduit;
import com.smart_delivery_management.smartlogi_delivery.entity.ColisProduitId;
import com.smart_delivery_management.smartlogi_delivery.service.ColisProduitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/colis-produits")
@RequiredArgsConstructor
@Slf4j
public class ColisProduitController {

    private final ColisProduitService colisProduitService;

    // ------------------- CREATE -------------------
    @PostMapping
    public ResponseEntity<ColisProduit> create(@RequestBody ColisProduit colisProduit) {
        log.info("Appel API: CREATE ColisProduit colisId={}, produitId={}",
                colisProduit.getId().getColisId(), colisProduit.getId().getProduitId());
        ColisProduit saved = colisProduitService.save(colisProduit);
        log.info("Association Colis-Produit créée avec succès: colisId={}, produitId={}",
                saved.getId().getColisId(), saved.getId().getProduitId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @GetMapping("/colis/{colisId}/produit/{produitId}")
    public ResponseEntity<ColisProduit> getById(
            @PathVariable String colisId,
            @PathVariable String produitId) {

        ColisProduitId id = new ColisProduitId(colisId, produitId);
        log.info("Appel API: GET ColisProduit colisId={}, produitId={}", colisId, produitId);
        return colisProduitService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Association Colis-Produit non trouvée: colisId={}, produitId={}", colisId, produitId);
                    return ResponseEntity.notFound().build();
                });
    }

    // ------------------- READ ALL (Pagination) -------------------
    @GetMapping
    public ResponseEntity<Page<ColisProduit>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET All ColisProduits page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ColisProduit> result = colisProduitService.findAll(pageable);
        log.info("Nombre total d'associations Colis-Produit récupérées: {}", result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- FIND BY COLIS -------------------
    @GetMapping("/colis/{colisId}")
    public ResponseEntity<Page<ColisProduit>> getByColis(
            @PathVariable String colisId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET ColisProduit par colisId={}", colisId);
        Pageable pageable = PageRequest.of(page, size);
        Page<ColisProduit> result = colisProduitService.findByColisId(colisId, pageable);
        log.info("Nombre de produits pour le colis {}: {}", colisId, result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- FIND BY PRODUIT -------------------
    @GetMapping("/produit/{produitId}")
    public ResponseEntity<Page<ColisProduit>> getByProduit(
            @PathVariable String produitId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET ColisProduit par produitId={}", produitId);
        Pageable pageable = PageRequest.of(page, size);
        Page<ColisProduit> result = colisProduitService.findByProduitId(produitId, pageable);
        log.info("Nombre de colis contenant le produit {}: {}", produitId, result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE BY ID -------------------
    @DeleteMapping("/colis/{colisId}/produit/{produitId}")
    public ResponseEntity<Void> deleteById(
            @PathVariable String colisId,
            @PathVariable String produitId) {

        ColisProduitId id = new ColisProduitId(colisId, produitId);
        log.info("Appel API: DELETE ColisProduit colisId={}, produitId={}", colisId, produitId);
        if (!colisProduitService.existsById(id)) {
            log.warn("Association Colis-Produit non trouvée: colisId={}, produitId={}", colisId, produitId);
            return ResponseEntity.notFound().build();
        }
        colisProduitService.deleteById(id);
        log.info("Association Colis-Produit supprimée: colisId={}, produitId={}", colisId, produitId);
        return ResponseEntity.noContent().build();
    }

    // ------------------- DELETE ALL BY COLIS -------------------
    @DeleteMapping("/colis/{colisId}")
    public ResponseEntity<Void> deleteByColis(@PathVariable String colisId) {
        log.info("Appel API: DELETE toutes les associations pour le colis {}", colisId);
        colisProduitService.deleteByColisId(colisId);
        log.info("Toutes les associations supprimées pour le colis {}", colisId);
        return ResponseEntity.noContent().build();
    }
}
