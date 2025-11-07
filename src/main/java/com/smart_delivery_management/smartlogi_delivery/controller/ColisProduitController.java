package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.ColisProduit;
import com.smart_delivery_management.smartlogi_delivery.entity.ColisProduitId;
import com.smart_delivery_management.smartlogi_delivery.service.ColisProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
@Tag(name = "Colis-Produit", description = "Gestion des associations entre colis et produits")
public class ColisProduitController {

    private final ColisProduitService colisProduitService;

    // ------------------- CREATE -------------------
    @Operation(summary = "Créer une association Colis-Produit")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Association créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<ColisProduit> create(@RequestBody ColisProduit colisProduit) {
        log.info("Appel API: CREATE ColisProduit colisId={}, produitId={}",
                colisProduit.getId().getColisId(), colisProduit.getId().getProduitId());
        ColisProduit saved = colisProduitService.save(colisProduit);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @Operation(summary = "Récupérer une association Colis-Produit par ID")
    @GetMapping("/colis/{colisId}/produit/{produitId}")
    public ResponseEntity<ColisProduit> getById(
            @Parameter(description = "ID du colis") @PathVariable String colisId,
            @Parameter(description = "ID du produit") @PathVariable String produitId) {

        ColisProduitId id = new ColisProduitId(colisId, produitId);
        return colisProduitService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ------------------- READ ALL -------------------
    @Operation(summary = "Récupérer toutes les associations Colis-Produit avec pagination")
    @GetMapping
    public ResponseEntity<Page<ColisProduit>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ColisProduit> result = colisProduitService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- FIND BY COLIS -------------------
    @Operation(summary = "Récupérer toutes les associations pour un colis donné")
    @GetMapping("/colis/{colisId}")
    public ResponseEntity<Page<ColisProduit>> getByColis(
            @Parameter(description = "ID du colis") @PathVariable String colisId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ColisProduit> result = colisProduitService.findByColisId(colisId, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- FIND BY PRODUIT -------------------
    @Operation(summary = "Récupérer toutes les associations pour un produit donné")
    @GetMapping("/produit/{produitId}")
    public ResponseEntity<Page<ColisProduit>> getByProduit(
            @Parameter(description = "ID du produit") @PathVariable String produitId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ColisProduit> result = colisProduitService.findByProduitId(produitId, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE BY ID -------------------
    @Operation(summary = "Supprimer une association Colis-Produit par ID")
    @DeleteMapping("/colis/{colisId}/produit/{produitId}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID du colis") @PathVariable String colisId,
            @Parameter(description = "ID du produit") @PathVariable String produitId) {

        ColisProduitId id = new ColisProduitId(colisId, produitId);
        if (!colisProduitService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        colisProduitService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- DELETE ALL BY COLIS -------------------
    @Operation(summary = "Supprimer toutes les associations pour un colis donné")
    @DeleteMapping("/colis/{colisId}")
    public ResponseEntity<Void> deleteByColis(@Parameter(description = "ID du colis") @PathVariable String colisId) {
        colisProduitService.deleteByColisId(colisId);
        return ResponseEntity.noContent().build();
    }
}
