package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.Destinataire;
import com.smart_delivery_management.smartlogi_delivery.service.DestinataireService;
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
@RequestMapping("/api/destinataires")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Destinataires", description = "Gestion des destinataires")
public class DestinataireController {

    private final DestinataireService destinataireService;

    // ------------------- CREATE -------------------
    @Operation(summary = "Créer un destinataire")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Destinataire créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<Destinataire> createDestinataire(@Valid @RequestBody Destinataire destinataire) {
        Destinataire saved = destinataireService.save(destinataire);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @Operation(summary = "Récupérer un destinataire par ID")
    @GetMapping("/{id}")
    public ResponseEntity<Destinataire> getDestinataireById(
            @Parameter(description = "ID du destinataire") @PathVariable String id) {

        return destinataireService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ------------------- READ ALL (Pagination) -------------------
    @Operation(summary = "Récupérer tous les destinataires avec pagination")
    @GetMapping
    public ResponseEntity<Page<Destinataire>> getAllDestinataires(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Destinataire> result = destinataireService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR NOM / PRENOM -------------------
    @Operation(summary = "Rechercher des destinataires par nom ou prénom")
    @GetMapping("/search")
    public ResponseEntity<Page<Destinataire>> searchByNomOrPrenom(
            @Parameter(description = "Nom du destinataire") @RequestParam(required = false, defaultValue = "") String nom,
            @Parameter(description = "Prénom du destinataire") @RequestParam(required = false, defaultValue = "") String prenom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Destinataire> result = destinataireService.searchByNomOrPrenom(nom, prenom, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR TÉLÉPHONE -------------------
    @Operation(summary = "Rechercher des destinataires par téléphone")
    @GetMapping("/telephone")
    public ResponseEntity<Page<Destinataire>> findByTelephone(
            @Parameter(description = "Numéro de téléphone") @RequestParam String telephone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Destinataire> result = destinataireService.findByTelephone(telephone, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE -------------------
    @Operation(summary = "Supprimer un destinataire par ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestinataire(
            @Parameter(description = "ID du destinataire") @PathVariable String id) {

        if (!destinataireService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        destinataireService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
