package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.Livreur;
import com.smart_delivery_management.smartlogi_delivery.service.LivreurService;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Livreurs", description = "Gestion des livreurs")
public class LivreurController {

    private final LivreurService livreurService;

    // ------------------- CREATE -------------------
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Livreur> createLivreur(@Valid @RequestBody Livreur livreur) {
        Livreur saved = livreurService.save(livreur);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isLivreurOwner(#id)")
    public ResponseEntity<Livreur> getLivreurById(@PathVariable String id) {
        return livreurService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ------------------- READ ALL -------------------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Livreur>> getAllLivreurs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(livreurService.findAll(pageable));
    }

    // ------------------- SEARCH -------------------
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Livreur>> searchByNomOrPrenom(
            @RequestParam(required = false, defaultValue = "") String nom,
            @RequestParam(required = false, defaultValue = "") String prenom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(livreurService.searchByNomOrPrenom(nom, prenom, pageable));
    }

    // ------------------- LIVREURS PAR ZONE -------------------
    @GetMapping("/zone/{zoneId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Livreur>> getLivreursByZone(
            @PathVariable String zoneId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(livreurService.findByZoneAssigneeId(zoneId, pageable));
    }

    // ------------------- LIVREURS PAR ZONE (CUSTOM) -------------------
    @GetMapping("/zone/custom/{zoneId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Livreur>> getLivreursByZoneCustom(
            @PathVariable String zoneId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(livreurService.findLivreursByZone(zoneId, pageable));
    }

    // ------------------- DELETE -------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLivreur(@PathVariable String id) {
        if (!livreurService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        livreurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
