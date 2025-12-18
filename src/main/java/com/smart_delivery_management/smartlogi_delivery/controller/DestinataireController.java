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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<Destinataire> createDestinataire(
            @Valid @RequestBody Destinataire destinataire) {

        Destinataire saved = destinataireService.save(destinataire);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @Operation(summary = "Récupérer un destinataire par ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','DESTINATAIRE','LIVREUR','ADMIN')")
    public ResponseEntity<Destinataire> getDestinataireById(
            @PathVariable String id) {

        return destinataireService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------- READ ALL (Pagination) -------------------
    @Operation(summary = "Récupérer tous les destinataires avec pagination")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Destinataire>> getAllDestinataires(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(destinataireService.findAll(pageable));
    }

    // ------------------- SEARCH PAR NOM / PRENOM -------------------
    @Operation(summary = "Rechercher des destinataires par nom ou prénom")
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Destinataire>> searchByNomOrPrenom(
            @RequestParam(defaultValue = "") String nom,
            @RequestParam(defaultValue = "") String prenom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                destinataireService.searchByNomOrPrenom(nom, prenom, pageable)
        );
    }

    // ------------------- SEARCH PAR TÉLÉPHONE -------------------
    @Operation(summary = "Rechercher des destinataires par téléphone")
    @GetMapping("/telephone")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Destinataire>> findByTelephone(
            @RequestParam String telephone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                destinataireService.findByTelephone(telephone, pageable)
        );
    }

    // ------------------- DELETE -------------------
    @Operation(summary = "Supprimer un destinataire par ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDestinataire(@PathVariable String id) {

        if (!destinataireService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        destinataireService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
