package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.dto.*;
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
        log.info("Colis créé avec succès: ID={}", created.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // ------------------- READ -------------------
    @Operation(summary = "Obtenir un colis par ID", description = "Récupère les informations détaillées d’un colis à partir de son identifiant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colis trouvé"),
            @ApiResponse(responseCode = "404", description = "Colis non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ColisDTO> getColisById(@PathVariable String id) {
        log.info("Appel API: GET Colis ID={}", id);
        ColisDTO dto = colisService.getColisById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Lister tous les colis", description = "Renvoie la liste paginée de tous les colis enregistrés dans le système.")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    @GetMapping
    public ResponseEntity<Page<ColisDTO>> getAllColis(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ColisDTO> colisPage = colisService.getAllColis(pageable);
        return ResponseEntity.ok(colisPage);
    }

    // ------------------- UPDATE -------------------
    @Operation(summary = "Mettre à jour un colis", description = "Modifie les informations d’un colis existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colis mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Colis non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ColisDTO> updateColis(
            @PathVariable String id,
            @Valid @RequestBody ColisDTO colisDTO) {

        ColisDTO updated = colisService.updateColis(id, colisDTO);
        return ResponseEntity.ok(updated);
    }

    // ------------------- DELETE -------------------
    @Operation(summary = "Supprimer un colis", description = "Supprime définitivement un colis par son identifiant.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Colis supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Colis non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColis(@PathVariable String id) {
        colisService.deleteColis(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- ASSIGNER LIVREUR -------------------
    @Operation(summary = "Assigner un livreur à un colis", description = "Associe un livreur existant à un colis non encore livré.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livreur assigné avec succès"),
            @ApiResponse(responseCode = "404", description = "Colis ou livreur non trouvé")
    })
    @PostMapping("/{colisId}/assigner-livreur/{livreurId}")
    public ResponseEntity<ColisDTO> assignerLivreur(
            @PathVariable String colisId,
            @PathVariable String livreurId) {

        ColisDTO dto = colisService.assignerLivreur(colisId, livreurId);
        return ResponseEntity.ok(dto);
    }

    // ------------------- UPDATE STATUT -------------------
    @Operation(summary = "Mettre à jour le statut d’un colis", description = "Change le statut du colis (Ex: EN_TRANSIT, LIVRÉ, RETARD, etc.) et ajoute un commentaire optionnel.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statut mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Colis non trouvé")
    })
    @PostMapping("/{colisId}/statut")
    public ResponseEntity<ColisDTO> updateStatut(
            @PathVariable String colisId,
            @RequestParam StatutColis statut,
            @RequestParam(required = false) String commentaire) {

        ColisDTO dto = colisService.updateStatut(colisId, statut, commentaire);
        return ResponseEntity.ok(dto);
    }

    // ------------------- SEARCH -------------------
    @Operation(summary = "Rechercher des colis", description = "Recherche des colis selon plusieurs critères (statut, priorité, zone, etc.).")
    @ApiResponse(responseCode = "200", description = "Résultats de la recherche")
    @PostMapping("/search")
    public ResponseEntity<Page<ColisDTO>> searchColis(
            @RequestBody ColisSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ColisDTO> results = colisService.searchColis(criteria, pageable);
        return ResponseEntity.ok(results);
    }

    // ------------------- STATISTIQUES -------------------
    @Operation(summary = "Statistiques par livreur", description = "Affiche des statistiques liées aux colis traités par un livreur donné.")
    @ApiResponse(responseCode = "200", description = "Statistiques récupérées avec succès")
    @GetMapping("/stats/livreur/{livreurId}")
    public ResponseEntity<ColisStatisticsDTO> statsByLivreur(@PathVariable String livreurId) {
        ColisStatisticsDTO stats = colisService.getStatisticsByLivreur(livreurId);
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Statistiques par zone", description = "Affiche des statistiques sur les colis d’une zone géographique donnée.")
    @ApiResponse(responseCode = "200", description = "Statistiques récupérées avec succès")
    @GetMapping("/stats/zone/{zoneId}")
    public ResponseEntity<ColisStatisticsDTO> statsByZone(@PathVariable String zoneId) {
        ColisStatisticsDTO stats = colisService.getStatisticsByZone(zoneId);
        return ResponseEntity.ok(stats);
    }

    // ------------------- COLIS EN RETARD -------------------
    @Operation(summary = "Lister les colis en retard", description = "Retourne tous les colis dont la livraison a dépassé la date prévue.")
    @ApiResponse(responseCode = "200", description = "Liste des colis en retard récupérée avec succès")
    @GetMapping("/retard")
    public ResponseEntity<List<ColisDTO>> getColisEnRetard() {
        List<ColisDTO> result = colisService.getColisEnRetard();
        return ResponseEntity.ok(result);
    }

    // ------------------- COLIS PRIORITAIRES NON ASSIGNÉS -------------------
    @Operation(summary = "Lister les colis prioritaires non assignés", description = "Affiche tous les colis prioritaires qui n’ont pas encore été attribués à un livreur.")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    @GetMapping("/prioritaires/non-assignes")
    public ResponseEntity<List<ColisDTO>> getColisPrioritairesNonAssignes() {
        List<ColisDTO> result = colisService.getColisPrioritairesNonAssignes();
        return ResponseEntity.ok(result);
    }
}
