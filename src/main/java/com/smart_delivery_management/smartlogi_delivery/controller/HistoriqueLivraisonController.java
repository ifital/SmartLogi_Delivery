package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.dto.HistoriqueLivraisonDTO;
import com.smart_delivery_management.smartlogi_delivery.entity.HistoriqueLivraison;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import com.smart_delivery_management.smartlogi_delivery.service.HistoriqueLivraisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historique-livraisons")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "HistoriqueLivraisons", description = "Gestion des historiques de livraison")
public class HistoriqueLivraisonController {

    private final HistoriqueLivraisonService historiqueLivraisonService;

    // ------------------- CREATE -------------------
    @Operation(summary = "Créer un historique de livraison")
    @PostMapping
    @PreAuthorize("hasAnyRole('LIVREUR','ADMIN')")
    public ResponseEntity<HistoriqueLivraisonDTO> createHistorique(
            @RequestBody HistoriqueLivraison historique) {

        HistoriqueLivraison saved = historiqueLivraisonService.save(historique);
        return new ResponseEntity<>(mapToDTO(saved), HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @Operation(summary = "Récupérer un historique par ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','DESTINATAIRE','LIVREUR','ADMIN')")
    public ResponseEntity<HistoriqueLivraisonDTO> getHistoriqueById(@PathVariable String id) {

        return historiqueLivraisonService.findById(id)
                .map(this::mapToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------- READ ALL -------------------
    @Operation(summary = "Récupérer tous les historiques (admin)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<HistoriqueLivraisonDTO>> getAllHistoriques(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<HistoriqueLivraison> result = historiqueLivraisonService.findAll(pageable);

        List<HistoriqueLivraisonDTO> dtos = result.stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(new PageImpl<>(dtos, pageable, result.getTotalElements()));
    }

    // ------------------- HISTORIQUES PAR COLIS -------------------
    @Operation(summary = "Récupérer l'historique d'un colis")
    @GetMapping("/colis/{colisId}")
    @PreAuthorize("hasAnyRole('CLIENT','DESTINATAIRE','LIVREUR','ADMIN')")
    public ResponseEntity<Page<HistoriqueLivraisonDTO>> getByColisId(
            @PathVariable String colisId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<HistoriqueLivraison> result =
                historiqueLivraisonService.findByColisIdOrderByDateDesc(colisId, pageable);

        List<HistoriqueLivraisonDTO> dtos = result.stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(new PageImpl<>(dtos, pageable, result.getTotalElements()));
    }

    // ------------------- HISTORIQUES PAR STATUT -------------------
    @Operation(summary = "Récupérer les historiques par statut")
    @GetMapping("/statut")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<HistoriqueLivraisonDTO>> getByStatut(
            @RequestParam StatutColis statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<HistoriqueLivraison> result =
                historiqueLivraisonService.findByStatut(statut, pageable);

        List<HistoriqueLivraisonDTO> dtos = result.stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(new PageImpl<>(dtos, pageable, result.getTotalElements()));
    }

    // ------------------- DELETE -------------------
    @Operation(summary = "Supprimer un historique")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHistorique(@PathVariable String id) {

        if (!historiqueLivraisonService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        historiqueLivraisonService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- MAPPING -------------------
    private HistoriqueLivraisonDTO mapToDTO(HistoriqueLivraison historique) {
        return new HistoriqueLivraisonDTO(
                historique.getId(),
                historique.getStatut(),
                historique.getDateChangement(),
                historique.getCommentaire()
        );
    }
}
