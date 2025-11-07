package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.HistoriqueLivraison;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import com.smart_delivery_management.smartlogi_delivery.service.HistoriqueLivraisonService;
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
@RequestMapping("/api/historique-livraisons")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "HistoriqueLivraisons", description = "Gestion des historiques de livraison")
public class HistoriqueLivraisonController {

    private final HistoriqueLivraisonService historiqueLivraisonService;

    // ------------------- CREATE -------------------
    @Operation(summary = "Créer un historique de livraison")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Historique créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<HistoriqueLivraison> createHistorique(
            @RequestBody HistoriqueLivraison historique) {

        HistoriqueLivraison saved = historiqueLivraisonService.save(historique);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @Operation(summary = "Récupérer un historique par ID")
    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueLivraison> getHistoriqueById(
            @Parameter(description = "ID de l'historique") @PathVariable String id) {

        return historiqueLivraisonService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ------------------- READ ALL (Pagination) -------------------
    @Operation(summary = "Récupérer tous les historiques de livraison avec pagination")
    @GetMapping
    public ResponseEntity<Page<HistoriqueLivraison>> getAllHistoriques(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<HistoriqueLivraison> result = historiqueLivraisonService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- HISTORIQUES PAR COLIS -------------------
    @Operation(summary = "Récupérer les historiques d'un colis par son ID")
    @GetMapping("/colis/{colisId}")
    public ResponseEntity<Page<HistoriqueLivraison>> getByColisId(
            @Parameter(description = "ID du colis") @PathVariable String colisId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<HistoriqueLivraison> result = historiqueLivraisonService.findByColisIdOrderByDateDesc(colisId, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- HISTORIQUES PAR STATUT -------------------
    @Operation(summary = "Récupérer les historiques par statut")
    @GetMapping("/statut")
    public ResponseEntity<Page<HistoriqueLivraison>> getByStatut(
            @Parameter(description = "Statut du colis") @RequestParam StatutColis statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<HistoriqueLivraison> result = historiqueLivraisonService.findByStatut(statut, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE -------------------
    @Operation(summary = "Supprimer un historique par ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistorique(
            @Parameter(description = "ID de l'historique") @PathVariable String id) {

        if (!historiqueLivraisonService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        historiqueLivraisonService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
