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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colis")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Colis", description = "Endpoints pour la gestion et le suivi des colis")
public class ColisController {

    private final ColisService colisService;

    // ------------------- CREATE -------------------
    @Operation(summary = "Créer un colis", description = "Ajoute un nouveau colis pour un client expéditeur.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Colis créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données fournies")
    })
    @PostMapping
    public ResponseEntity<ColisDTO> createColis(@Valid @RequestBody ColisCreateDTO createDTO) {
        log.info("Appel API: CREATE Colis pour le client={}", createDTO.getClientExpediteurId());
        ColisDTO created = colisService.createColis(createDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // ------------------- READ -------------------
    @Operation(summary = "Obtenir un colis par ID", description = "Récupère les informations détaillées d’un colis à partir de son identifiant.")
    @GetMapping("/{id}")
    public ResponseEntity<ColisDTO> getColisById(@PathVariable String id) {
        ColisDTO dto = colisService.getColisById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Lister tous les colis", description = "Renvoie la liste paginée de tous les colis enregistrés dans le système.")
    @GetMapping
    public ResponseEntity<Page<ColisDTO>> getAllColis(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(colisService.getAllColis(pageable));
    }

    // ------------------- UPDATE -------------------
    @Operation(summary = "Mettre à jour un colis", description = "Modifie les informations d’un colis existant.")
    @PutMapping("/{id}")
    public ResponseEntity<ColisDTO> updateColis(
            @PathVariable String id,
            @Valid @RequestBody ColisDTO colisDTO) {
        return ResponseEntity.ok(colisService.updateColis(id, colisDTO));
    }

    // ------------------- DELETE -------------------
    @Operation(summary = "Supprimer un colis", description = "Supprime définitivement un colis par son identifiant.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColis(@PathVariable String id) {
        colisService.deleteColis(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- ASSIGNER LIVREUR -------------------
    @Operation(summary = "Assigner un livreur à un colis", description = "Associe un livreur existant à un colis non encore livré.")
    @PostMapping("/{colisId}/assigner-livreur/{livreurId}")
    public ResponseEntity<ColisDTO> assignerLivreur(
            @PathVariable String colisId,
            @PathVariable String livreurId) {
        return ResponseEntity.ok(colisService.assignerLivreur(colisId, livreurId));
    }

    // ------------------- UPDATE STATUT -------------------
    @Operation(summary = "Mettre à jour le statut d’un colis", description = "Change le statut du colis et ajoute un commentaire optionnel.")
    @PostMapping("/{colisId}/statut")
    public ResponseEntity<ColisDTO> updateStatut(
            @PathVariable String colisId,
            @RequestParam StatutColis statut,
            @RequestParam(required = false) String commentaire) {
        return ResponseEntity.ok(colisService.updateStatut(colisId, statut, commentaire));
    }

    // ------------------- SEARCH -------------------
    @Operation(summary = "Rechercher des colis", description = "Recherche des colis selon plusieurs critères (statut, priorité, zone, etc.).")
    @PostMapping("/search")
    public ResponseEntity<Page<ColisDTO>> searchColis(
            @RequestBody ColisSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(colisService.searchColis(criteria, pageable));
    }

    // ------------------- STATISTIQUES -------------------
    @Operation(summary = "Statistiques par livreur", description = "Affiche des statistiques liées aux colis traités par un livreur donné.")
    @GetMapping("/stats/livreur/{livreurId}")
    public ResponseEntity<ColisStatisticsDTO> statsByLivreur(@PathVariable String livreurId) {
        return ResponseEntity.ok(colisService.getStatisticsByLivreur(livreurId));
    }

    @Operation(summary = "Statistiques par zone", description = "Affiche des statistiques sur les colis d’une zone géographique donnée.")
    @GetMapping("/stats/zone/{zoneId}")
    public ResponseEntity<ColisStatisticsDTO> statsByZone(@PathVariable String zoneId) {
        return ResponseEntity.ok(colisService.getStatisticsByZone(zoneId));
    }

    // ------------------- RETARD -------------------
    @Operation(summary = "Lister les colis en retard", description = "Retourne tous les colis dont la livraison a dépassé la date prévue.")
    @GetMapping("/retard")
    public ResponseEntity<List<ColisDTO>> getColisEnRetard() {
        return ResponseEntity.ok(colisService.getColisEnRetard());
    }

    // ------------------- PRIORITAIRES NON ASSIGNÉS -------------------
    @Operation(summary = "Lister les colis prioritaires non assignés", description = "Affiche tous les colis prioritaires qui n’ont pas encore été attribués à un livreur.")
    @GetMapping("/prioritaires/non-assignes")
    public ResponseEntity<List<ColisDTO>> getColisPrioritairesNonAssignes() {
        return ResponseEntity.ok(colisService.getColisPrioritairesNonAssignes());
    }

    // ------------------- COLIS PAR LIVREUR -------------------
    @Operation(summary = "Lister les colis attribués à un livreur")
    @GetMapping("/livreur/{livreurId}")
    public ResponseEntity<Page<ColisDTO>> getColisByLivreur(
            @PathVariable String livreurId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(colisService.getColisByLivreur(livreurId, pageable));
    }

    // ------------------- COLIS PAR CLIENT -------------------
    @Operation(summary = "Lister les colis d’un client expéditeur")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<ColisDTO>> getColisByClient(
            @PathVariable String clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(colisService.getColisByClient(clientId, pageable));
    }

    // ------------------- COLIS PAR DESTINATAIRE -------------------
    @Operation(summary = "Lister les colis d’un destinataire")
    @GetMapping("/destinataire/{destinataireId}")
    public ResponseEntity<List<ColisDTO>> getColisByDestinataire(@PathVariable String destinataireId) {
        return ResponseEntity.ok(colisService.getColisByDestinataire(destinataireId));
    }

    // ------------------- HISTORIQUE -------------------
    @Operation(summary = "Afficher l’historique d’un colis")
    @GetMapping("/{colisId}/historique")
    public ResponseEntity<Page<HistoriqueLivraisonDTO>> getHistorique(
            @PathVariable String colisId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(colisService.getHistorique(colisId, pageable));
    }

    // ------------------- PAR STATUT -------------------
    @Operation(summary = "Lister les colis par statut")
    @GetMapping("/statut/{statut}")
    public ResponseEntity<Page<ColisDTO>> getColisByStatut(
            @PathVariable StatutColis statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(colisService.getColisByStatut(statut, pageable));
    }

    // ------------------- PAR PRIORITÉ -------------------
    @Operation(summary = "Lister les colis par priorité")
    @GetMapping("/priorite/{priorite}")
    public ResponseEntity<Page<ColisDTO>> getColisByPriorite(
            @PathVariable String priorite,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PrioriteColis p = Enum.valueOf(PrioriteColis.class, priorite.toUpperCase());
        return ResponseEntity.ok(colisService.getColisByPriorite(p, pageable));
    }

    // ------------------- PAR ZONE -------------------
    @Operation(summary = "Lister les colis d’une zone")
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<Page<ColisDTO>> getColisByZone(
            @PathVariable String zoneId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(colisService.getColisByZone(zoneId, pageable));
    }

    // ------------------- PAR VILLE -------------------
    @Operation(summary = "Lister les colis d’une ville")
    @GetMapping("/ville/{ville}")
    public ResponseEntity<Page<ColisDTO>> getColisByVille(
            @PathVariable String ville,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(colisService.getColisByVille(ville, pageable));
    }

    // ------------------- STATISTIQUES GLOBALES -------------------
    @Operation(summary = "Compter les colis par statut")
    @GetMapping("/stats/statut")
    public ResponseEntity<List<Object[]>> countByStatutGroupBy() {
        return ResponseEntity.ok(colisService.countByStatutGroupBy());
    }

    @Operation(summary = "Compter les colis par zone")
    @GetMapping("/stats/zones")
    public ResponseEntity<List<Object[]>> countByZoneGroupBy() {
        return ResponseEntity.ok(colisService.countByZoneGroupBy());
    }

    @Operation(summary = "Compter les colis par priorité")
    @GetMapping("/stats/priorite")
    public ResponseEntity<List<Object[]>> countByPrioriteGroupBy() {
        return ResponseEntity.ok(colisService.countByPrioriteGroupBy());
    }
}
