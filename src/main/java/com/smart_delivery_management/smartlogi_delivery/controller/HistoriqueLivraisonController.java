package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.HistoriqueLivraison;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import com.smart_delivery_management.smartlogi_delivery.service.HistoriqueLivraisonService;
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
public class HistoriqueLivraisonController {

    private final HistoriqueLivraisonService historiqueLivraisonService;

    // ------------------- CREATE -------------------
    @PostMapping
    public ResponseEntity<HistoriqueLivraison> createHistorique(@RequestBody HistoriqueLivraison historique) {
        log.info("Appel API: CREATE Historique de livraison colisId={}, statut={}",
                historique.getColis().getId(), historique.getStatut());
        HistoriqueLivraison saved = historiqueLivraisonService.save(historique);
        log.info("Historique créé: ID={}, colisId={}, statut={}", saved.getId(), saved.getColis().getId(), saved.getStatut());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueLivraison> getHistoriqueById(@PathVariable String id) {
        log.info("Appel API: GET Historique ID={}", id);
        return historiqueLivraisonService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Aucun historique trouvé avec ID={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // ------------------- READ ALL (Pagination) -------------------
    @GetMapping
    public ResponseEntity<Page<HistoriqueLivraison>> getAllHistoriques(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET All Historiques page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoriqueLivraison> result = historiqueLivraisonService.findAll(pageable);
        log.info("Nombre total d'historiques récupérés: {}", result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- HISTORIQUES PAR COLIS -------------------
    @GetMapping("/colis/{colisId}")
    public ResponseEntity<Page<HistoriqueLivraison>> getByColisId(
            @PathVariable String colisId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET Historiques pour le colis ID={}", colisId);
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoriqueLivraison> result = historiqueLivraisonService.findByColisIdOrderByDateDesc(colisId, pageable);
        log.info("Nombre d'historiques trouvés pour le colis {}: {}", colisId, result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- HISTORIQUES PAR STATUT -------------------
    @GetMapping("/statut")
    public ResponseEntity<Page<HistoriqueLivraison>> getByStatut(
            @RequestParam StatutColis statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET Historiques par statut={}", statut);
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoriqueLivraison> result = historiqueLivraisonService.findByStatut(statut, pageable);
        log.info("Nombre d'historiques trouvés avec le statut {}: {}", statut, result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistorique(@PathVariable String id) {
        log.info("Appel API: DELETE Historique ID={}", id);
        if (!historiqueLivraisonService.existsById(id)) {
            log.warn("Tentative de suppression d'un historique inexistant ID={}", id);
            return ResponseEntity.notFound().build();
        }
        historiqueLivraisonService.deleteById(id);
        log.info("Historique supprimé: ID={}", id);
        return ResponseEntity.noContent().build();
    }
}
