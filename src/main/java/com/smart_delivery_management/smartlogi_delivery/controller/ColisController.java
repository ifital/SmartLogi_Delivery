package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.dto.*;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.PrioriteColis;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import com.smart_delivery_management.smartlogi_delivery.service.ColisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/colis")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Colis", description = "Endpoints pour la gestion et le suivi des colis")
public class ColisController {

    private final ColisService colisService;

    // ------------------- CREATE -------------------
    @Operation(summary = "Créer un colis")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Colis créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColisDTO> createColis(
            @Valid @RequestBody ColisCreateDTO createDTO) {

        log.info("CREATE colis - client={}", createDTO.getClientExpediteurId());
        return new ResponseEntity<>(
                colisService.createColis(createDTO),
                HttpStatus.CREATED
        );
    }

    // ------------------- READ -------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColisDTO> getColisById(@PathVariable String id) {
        return ResponseEntity.ok(colisService.getColisById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ColisDTO>> getAllColis(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(colisService.getAllColis(pageable));
    }

    // ------------------- UPDATE -------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColisDTO> updateColis(
            @PathVariable String id,
            @Valid @RequestBody ColisDTO colisDTO) {

        return ResponseEntity.ok(colisService.updateColis(id, colisDTO));
    }

    // ------------------- DELETE -------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteColis(@PathVariable String id) {
        colisService.deleteColis(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- ASSIGNER LIVREUR -------------------
    @PostMapping("/{colisId}/assigner-livreur/{livreurId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColisDTO> assignerLivreur(
            @PathVariable String colisId,
            @PathVariable String livreurId) {

        return ResponseEntity.ok(
                colisService.assignerLivreur(colisId, livreurId)
        );
    }

    // ------------------- COLIS DU LIVREUR CONNECTÉ -------------------
    @PreAuthorize("hasRole('LIVREUR')")
    @GetMapping("/livreur/me")
    public ResponseEntity<Page<ColisDTO>> getMyColis(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        String email = authentication.getName();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                colisService.getColisByLivreurEmail(email, pageable)
        );
    }

    // ------------------- UPDATE STATUT -------------------
    @PreAuthorize("hasRole('LIVREUR')")
    @PostMapping("/{colisId}/statut")
    public ResponseEntity<ColisDTO> updateStatut(
            @PathVariable String colisId,
            @RequestParam StatutColis statut,
            @RequestParam(required = false) String commentaire,
            Authentication authentication) throws AccessDeniedException {

        return ResponseEntity.ok(
                colisService.updateStatutByLivreur(
                        colisId,
                        statut,
                        commentaire,
                        authentication.getName()
                )
        );
    }

    // ------------------- SEARCH -------------------
    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ColisDTO>> searchColis(
            @RequestBody ColisSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(colisService.searchColis(criteria, pageable));
    }

    // ------------------- STATISTIQUES -------------------
    @GetMapping("/stats/livreur/{livreurId}")
    @PreAuthorize("hasRole('LIVREUR')")
    public ResponseEntity<ColisStatisticsDTO> statsByLivreur(
            @PathVariable String livreurId) {

        return ResponseEntity.ok(
                colisService.getStatisticsByLivreur(livreurId)
        );
    }

    @GetMapping("/stats/zone/{zoneId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColisStatisticsDTO> statsByZone(
            @PathVariable String zoneId) {

        return ResponseEntity.ok(
                colisService.getStatisticsByZone(zoneId)
        );
    }

    // ------------------- RETARD -------------------
    @GetMapping("/retard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ColisDTO>> getColisEnRetard() {
        return ResponseEntity.ok(colisService.getColisEnRetard());
    }

    // ------------------- PRIORITAIRES NON ASSIGNÉS -------------------
    @GetMapping("/prioritaires/non-assignes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ColisDTO>> getColisPrioritairesNonAssignes() {
        return ResponseEntity.ok(
                colisService.getColisPrioritairesNonAssignes()
        );
    }

    // ------------------- FILTRES -------------------
    @GetMapping("/livreur/{livreurId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ColisDTO>> getColisByLivreur(
            @PathVariable String livreurId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                colisService.getColisByLivreur(livreurId, pageable)
        );
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ColisDTO>> getColisByClient(
            @PathVariable String clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                colisService.getColisByClient(clientId, pageable)
        );
    }

    @GetMapping("/destinataire/{destinataireId}")
    @PreAuthorize("hasRole('DESTINATAIRE')")
    public ResponseEntity<List<ColisDTO>> getColisByDestinataire(
            @PathVariable String destinataireId) {

        return ResponseEntity.ok(
                colisService.getColisByDestinataire(destinataireId)
        );
    }

    // ------------------- HISTORIQUE -------------------
    @GetMapping("/{colisId}/historique")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<HistoriqueLivraisonDTO>> getHistorique(
            @PathVariable String colisId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                colisService.getHistorique(colisId, pageable)
        );
    }

    // ------------------- PAR STATUT / PRIORITÉ / ZONE -------------------
    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ColisDTO>> getColisByStatut(
            @PathVariable StatutColis statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                colisService.getColisByStatut(statut, pageable)
        );
    }

    @GetMapping("/priorite/{priorite}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ColisDTO>> getColisByPriorite(
            @PathVariable String priorite,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PrioriteColis p = PrioriteColis.valueOf(priorite.toUpperCase());
        return ResponseEntity.ok(
                colisService.getColisByPriorite(p, pageable)
        );
    }

    @GetMapping("/zone/{zoneId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ColisDTO>> getColisByZone(
            @PathVariable String zoneId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                colisService.getColisByZone(zoneId, pageable)
        );
    }

    @GetMapping("/ville/{ville}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ColisDTO>> getColisByVille(
            @PathVariable String ville,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                colisService.getColisByVille(ville, pageable)
        );
    }

    // ------------------- STATS GLOBALES -------------------
    @GetMapping("/stats/statut")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> countByStatutGroupBy() {
        return ResponseEntity.ok(colisService.countByStatutGroupBy());
    }

    @GetMapping("/stats/zones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> countByZoneGroupBy() {
        return ResponseEntity.ok(colisService.countByZoneGroupBy());
    }

    @GetMapping("/stats/priorite")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> countByPrioriteGroupBy() {
        return ResponseEntity.ok(colisService.countByPrioriteGroupBy());
    }
}
