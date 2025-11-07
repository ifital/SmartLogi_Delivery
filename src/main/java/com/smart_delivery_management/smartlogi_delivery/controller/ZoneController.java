package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.Zone;
import com.smart_delivery_management.smartlogi_delivery.service.ZoneService;
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
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Zones", description = "Gestion des zones")
public class ZoneController {

    private final ZoneService zoneService;

    // ------------------- CREATE -------------------
    @Operation(summary = "Créer une zone")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Zone créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<Zone> createZone(@Valid @RequestBody Zone zone) {
        Zone saved = zoneService.save(zone);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @Operation(summary = "Récupérer une zone par ID")
    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZoneById(
            @Parameter(description = "ID de la zone") @PathVariable String id) {

        return zoneService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ------------------- READ ALL (Pagination) -------------------
    @Operation(summary = "Récupérer toutes les zones avec pagination")
    @GetMapping
    public ResponseEntity<Page<Zone>> getAllZones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Zone> result = zoneService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR NOM -------------------
    @Operation(summary = "Rechercher des zones par nom")
    @GetMapping("/search")
    public ResponseEntity<Page<Zone>> searchByNom(
            @RequestParam(required = false, defaultValue = "") String nom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Zone> result = zoneService.searchByNom(nom, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR CODE POSTAL -------------------
    @Operation(summary = "Rechercher des zones par code postal")
    @GetMapping("/code-postal")
    public ResponseEntity<Page<Zone>> searchByCodePostal(
            @RequestParam(required = false, defaultValue = "") String codePostal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Zone> result = zoneService.findByCodePostal(codePostal, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE -------------------
    @Operation(summary = "Supprimer une zone par ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(
            @Parameter(description = "ID de la zone") @PathVariable String id) {

        if (!zoneService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        zoneService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
