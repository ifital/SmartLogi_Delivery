package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.Zone;
import com.smart_delivery_management.smartlogi_delivery.service.ZoneService;
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
public class ZoneController {

    private final ZoneService zoneService;

    // ------------------- CREATE -------------------
    @PostMapping
    public ResponseEntity<Zone> createZone(@Valid @RequestBody Zone zone) {
        log.info("Appel API: CREATE Zone nom={}, codePostal={}", zone.getNom(), zone.getCodePostal());
        Zone saved = zoneService.save(zone);
        log.info("Zone créée avec succès: ID={}", saved.getId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZoneById(@PathVariable String id) {
        log.info("Appel API: GET Zone ID={}", id);
        return zoneService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Aucune zone trouvée avec ID={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // ------------------- READ ALL (Pagination) -------------------
    @GetMapping
    public ResponseEntity<Page<Zone>> getAllZones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET All Zones page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Zone> result = zoneService.findAll(pageable);
        log.info("Nombre total de zones récupérées: {}", result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR NOM -------------------
    @GetMapping("/search")
    public ResponseEntity<Page<Zone>> searchByNom(
            @RequestParam(required = false, defaultValue = "") String nom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: SEARCH Zone par nom='{}'", nom);
        Pageable pageable = PageRequest.of(page, size);
        Page<Zone> result = zoneService.searchByNom(nom, pageable);
        log.info("Résultats: {} zones trouvées", result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR CODE POSTAL -------------------
    @GetMapping("/code-postal")
    public ResponseEntity<Page<Zone>> searchByCodePostal(
            @RequestParam(required = false, defaultValue = "") String codePostal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: SEARCH Zone par codePostal='{}'", codePostal);
        Pageable pageable = PageRequest.of(page, size);
        Page<Zone> result = zoneService.findByCodePostal(codePostal, pageable);
        log.info("Résultats: {} zones trouvées avec codePostal='{}'", result.getTotalElements(), codePostal);
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable String id) {
        log.info("Appel API: DELETE Zone ID={}", id);
        if (!zoneService.existsById(id)) {
            log.warn("Tentative de suppression d'une zone inexistante ID={}", id);
            return ResponseEntity.notFound().build();
        }
        zoneService.deleteById(id);
        log.info("Zone supprimée: ID={}", id);
        return ResponseEntity.noContent().build();
    }
}
