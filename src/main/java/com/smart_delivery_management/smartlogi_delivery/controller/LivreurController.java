package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.Livreur;
import com.smart_delivery_management.smartlogi_delivery.service.LivreurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Livreurs", description = "Gestion des livreurs")
public class LivreurController {

    private final LivreurService livreurService;

    // ------------------- CREATE -------------------
    @Operation(summary = "Créer un livreur")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livreur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<Livreur> createLivreur(@Valid @RequestBody Livreur livreur) {
        Livreur saved = livreurService.save(livreur);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @Operation(summary = "Récupérer un livreur par ID")
    @GetMapping("/{id}")
    public ResponseEntity<Livreur> getLivreurById(
            @Parameter(description = "ID du livreur") @PathVariable String id) {

        return livreurService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ------------------- READ ALL (Pagination) -------------------
    @Operation(summary = "Récupérer tous les livreurs avec pagination")
    @GetMapping
    public ResponseEntity<Page<Livreur>> getAllLivreurs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Livreur> result = livreurService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR NOM / PRENOM -------------------
    @Operation(summary = "Rechercher des livreurs par nom ou prénom")
    @GetMapping("/search")
    public ResponseEntity<Page<Livreur>> searchByNomOrPrenom(
            @RequestParam(required = false, defaultValue = "") String nom,
            @RequestParam(required = false, defaultValue = "") String prenom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Livreur> result = livreurService.searchByNomOrPrenom(nom, prenom, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- LIVREURS PAR ZONE -------------------
    @Operation(summary = "Récupérer les livreurs assignés à une zone")
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<Page<Livreur>> getLivreursByZone(
            @Parameter(description = "ID de la zone") @PathVariable String zoneId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Livreur> result = livreurService.findByZoneAssigneeId(zoneId, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- LIVREURS PAR ZONE (requête personnalisée) -------------------
    @Operation(summary = "Récupérer les livreurs d'une zone via une requête personnalisée")
    @GetMapping("/zone/custom/{zoneId}")
    public ResponseEntity<Page<Livreur>> getLivreursByZoneCustom(
            @Parameter(description = "ID de la zone") @PathVariable String zoneId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Livreur> result = livreurService.findLivreursByZone(zoneId, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE -------------------
    @Operation(summary = "Supprimer un livreur par ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivreur(
            @Parameter(description = "ID du livreur") @PathVariable String id) {

        if (!livreurService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        livreurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
