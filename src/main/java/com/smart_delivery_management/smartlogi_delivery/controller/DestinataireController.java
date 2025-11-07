package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.Destinataire;
import com.smart_delivery_management.smartlogi_delivery.service.DestinataireService;
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
public class DestinataireController {

    private final DestinataireService destinataireService;

    // ------------------- CREATE -------------------
    @PostMapping
    public ResponseEntity<Destinataire> createDestinataire(@Valid @RequestBody Destinataire destinataire) {
        log.info("Appel API: CREATE Destinataire nom={} {}", destinataire.getNom(), destinataire.getPrenom());
        Destinataire saved = destinataireService.save(destinataire);
        log.info("Destinataire créé avec succès: ID={}", saved.getId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @GetMapping("/{id}")
    public ResponseEntity<Destinataire> getDestinataireById(@PathVariable String id) {
        log.info("Appel API: GET Destinataire ID={}", id);
        return destinataireService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Aucun destinataire trouvé avec ID={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // ------------------- READ ALL (Pagination) -------------------
    @GetMapping
    public ResponseEntity<Page<Destinataire>> getAllDestinataires(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET All Destinataires page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Destinataire> result = destinataireService.findAll(pageable);
        log.info("Nombre total de destinataires récupérés: {}", result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR NOM / PRENOM -------------------
    @GetMapping("/search")
    public ResponseEntity<Page<Destinataire>> searchByNomOrPrenom(
            @RequestParam(required = false, defaultValue = "") String nom,
            @RequestParam(required = false, defaultValue = "") String prenom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: SEARCH Destinataires nom='{}', prenom='{}'", nom, prenom);
        Pageable pageable = PageRequest.of(page, size);
        Page<Destinataire> result = destinataireService.searchByNomOrPrenom(nom, prenom, pageable);
        log.info("Résultats: {} destinataires trouvés", result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR TÉLÉPHONE -------------------
    @GetMapping("/telephone")
    public ResponseEntity<Page<Destinataire>> findByTelephone(
            @RequestParam String telephone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: SEARCH Destinataire téléphone={}", telephone);
        Pageable pageable = PageRequest.of(page, size);
        Page<Destinataire> result = destinataireService.findByTelephone(telephone, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestinataire(@PathVariable String id) {
        log.info("Appel API: DELETE Destinataire ID={}", id);
        if (!destinataireService.existsById(id)) {
            log.warn("Tentative de suppression d'un destinataire inexistant ID={}", id);
            return ResponseEntity.notFound().build();
        }
        destinataireService.deleteById(id);
        log.info("Destinataire supprimé: ID={}", id);
        return ResponseEntity.noContent().build();
    }
}
