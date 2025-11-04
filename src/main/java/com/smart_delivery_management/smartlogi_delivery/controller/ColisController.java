package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.dto.*;
import com.smart_delivery_management.smartlogi_delivery.entities.enums.StatutColis;
import com.smart_delivery_management.smartlogi_delivery.service.ColisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colis")
@RequiredArgsConstructor
@Slf4j
public class ColisController {

    private final ColisService colisService;

    // ------------------- CREATE -------------------
    @PostMapping
    public ResponseEntity<ColisDTO> createColis(@Valid @RequestBody ColisCreateDTO createDTO) {
        log.info("Appel API: CREATE Colis pour le client={}", createDTO.getClientExpediteurId());
        ColisDTO created = colisService.createColis(createDTO);
        log.info("Colis créé avec succès: ID={}", created.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // ------------------- READ -------------------
    @GetMapping("/{id}")
    public ResponseEntity<ColisDTO> getColisById(@PathVariable String id) {
        log.info("Appel API: GET Colis ID={}", id);
        ColisDTO dto = colisService.getColisById(id);
        log.info("Colis récupéré: ID={}, statut={}", dto.getId(), dto.getStatut());
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ColisDTO>> getAllColis(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET All Colis, page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ColisDTO> colisPage = colisService.getAllColis(pageable);
        log.info("Nombre de colis récupérés: {}", colisPage.getTotalElements());
        return ResponseEntity.ok(colisPage);
    }

    // ------------------- UPDATE -------------------
    @PutMapping("/{id}")
    public ResponseEntity<ColisDTO> updateColis(
            @PathVariable String id,
            @Valid @RequestBody ColisDTO colisDTO) {

        log.info("Appel API: UPDATE Colis ID={}", id);
        ColisDTO updated = colisService.updateColis(id, colisDTO);
        log.info("Colis mis à jour: ID={}, statut={}", updated.getId(), updated.getStatut());
        return ResponseEntity.ok(updated);
    }

    // ------------------- DELETE -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColis(@PathVariable String id) {
        log.info("Appel API: DELETE Colis ID={}", id);
        colisService.deleteColis(id);
        log.info("Colis supprimé avec succès: ID={}", id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- ASSIGNER LIVREUR -------------------
    @PostMapping("/{colisId}/assigner-livreur/{livreurId}")
    public ResponseEntity<ColisDTO> assignerLivreur(
            @PathVariable String colisId,
            @PathVariable String livreurId) {

        log.info("Appel API: ASSIGNER LIVREUR colisID={} -> livreurID={}", colisId, livreurId);
        ColisDTO dto = colisService.assignerLivreur(colisId, livreurId);
        log.info("Livreur assigné au colis ID={}, livreurID={}", colisId, livreurId);
        return ResponseEntity.ok(dto);
    }

    // ------------------- UPDATE STATUT -------------------
    @PostMapping("/{colisId}/statut")
    public ResponseEntity<ColisDTO> updateStatut(
            @PathVariable String colisId,
            @RequestParam StatutColis statut,
            @RequestParam(required = false) String commentaire) {

        log.info("Appel API: UPDATE STATUT colisID={} -> statut={}", colisId, statut);
        ColisDTO dto = colisService.updateStatut(colisId, statut, commentaire);
        log.info("Statut mis à jour pour le colis ID={}, nouveau statut={}", colisId, statut);
        return ResponseEntity.ok(dto);
    }

    // ------------------- SEARCH -------------------
    @PostMapping("/search")
    public ResponseEntity<Page<ColisDTO>> searchColis(
            @RequestBody ColisSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: SEARCH Colis avec critères");
        Pageable pageable = PageRequest.of(page, size);
        Page<ColisDTO> results = colisService.searchColis(criteria, pageable);
        log.info("Nombre de colis trouvés: {}", results.getTotalElements());
        return ResponseEntity.ok(results);
    }

    // ------------------- STATISTIQUES -------------------
    @GetMapping("/stats/livreur/{livreurId}")
    public ResponseEntity<ColisStatisticsDTO> statsByLivreur(@PathVariable String livreurId) {
        log.info("Appel API: STATISTIQUES par livreurID={}", livreurId);
        ColisStatisticsDTO stats = colisService.getStatisticsByLivreur(livreurId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/zone/{zoneId}")
    public ResponseEntity<ColisStatisticsDTO> statsByZone(@PathVariable String zoneId) {
        log.info("Appel API: STATISTIQUES par zoneID={}", zoneId);
        ColisStatisticsDTO stats = colisService.getStatisticsByZone(zoneId);
        return ResponseEntity.ok(stats);
    }

    // ------------------- COLIS EN RETARD -------------------
    @GetMapping("/retard")
    public ResponseEntity<List<ColisDTO>> getColisEnRetard() {
        log.info("Appel API: COLIS EN RETARD");
        List<ColisDTO> result = colisService.getColisEnRetard();
        log.info("Nombre de colis en retard: {}", result.size());
        return ResponseEntity.ok(result);
    }

    // ------------------- COLIS PRIORITAIRES NON ASSIGNES -------------------
    @GetMapping("/prioritaires/non-assignes")
    public ResponseEntity<List<ColisDTO>> getColisPrioritairesNonAssignes() {
        log.info("Appel API: COLIS PRIORITAIRES NON ASSIGNES");
        List<ColisDTO> result = colisService.getColisPrioritairesNonAssignes();
        log.info("Nombre de colis prioritaires non assignés: {}", result.size());
        return ResponseEntity.ok(result);
    }
}
