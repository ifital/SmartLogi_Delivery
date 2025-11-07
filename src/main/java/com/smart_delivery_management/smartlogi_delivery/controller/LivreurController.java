package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.Livreur;
import com.smart_delivery_management.smartlogi_delivery.service.LivreurService;
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
public class LivreurController {

    private final LivreurService livreurService;

    // ------------------- CREATE -------------------
    @PostMapping
    public ResponseEntity<Livreur> createLivreur(@Valid @RequestBody Livreur livreur) {
        log.info("Appel API: CREATE Livreur nom={} {}", livreur.getNom(), livreur.getPrenom());
        Livreur saved = livreurService.save(livreur);
        log.info("Livreur créé avec succès: ID={}", saved.getId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @GetMapping("/{id}")
    public ResponseEntity<Livreur> getLivreurById(@PathVariable String id) {
        log.info("Appel API: GET Livreur ID={}", id);
        return livreurService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Aucun livreur trouvé avec ID={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // ------------------- READ ALL (Pagination) -------------------
    @GetMapping
    public ResponseEntity<Page<Livreur>> getAllLivreurs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET All Livreurs page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Livreur> result = livreurService.findAll(pageable);
        log.info("Nombre total de livreurs récupérés: {}", result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR NOM / PRENOM -------------------
    @GetMapping("/search")
    public ResponseEntity<Page<Livreur>> searchByNomOrPrenom(
            @RequestParam(required = false, defaultValue = "") String nom,
            @RequestParam(required = false, defaultValue = "") String prenom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: SEARCH Livreur nom='{}', prenom='{}'", nom, prenom);
        Pageable pageable = PageRequest.of(page, size);
        Page<Livreur> result = livreurService.searchByNomOrPrenom(nom, prenom, pageable);
        log.info("Résultats: {} livreurs trouvés", result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- LIVREURS PAR ZONE -------------------
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<Page<Livreur>> getLivreursByZone(
            @PathVariable String zoneId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET Livreurs par Zone ID={}", zoneId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Livreur> result = livreurService.findByZoneAssigneeId(zoneId, pageable);
        log.info("Livreurs trouvés pour zone {}: {}", zoneId, result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- LIVREURS PAR ZONE (requête personnalisée) -------------------
    @GetMapping("/zone/custom/{zoneId}")
    public ResponseEntity<Page<Livreur>> getLivreursByZoneCustom(
            @PathVariable String zoneId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET Livreurs par Zone (custom) ID={}", zoneId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Livreur> result = livreurService.findLivreursByZone(zoneId, pageable);
        log.info("Livreurs récupérés (custom) pour zone {}: {}", zoneId, result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivreur(@PathVariable String id) {
        log.info("Appel API: DELETE Livreur ID={}", id);
        if (!livreurService.existsById(id)) {
            log.warn("Tentative de suppression d'un livreur inexistant ID={}", id);
            return ResponseEntity.notFound().build();
        }
        livreurService.deleteById(id);
        log.info("Livreur supprimé: ID={}", id);
        return ResponseEntity.noContent().build();
    }
}
