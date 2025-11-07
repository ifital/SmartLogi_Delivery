package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.dto.ClientExpediteurDTO;
import com.smart_delivery_management.smartlogi_delivery.service.ClientExpediteurService;
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

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ClientExpediteur", description = "Endpoints pour la gestion des clients expéditeurs")
public class ClientExpediteurController {

    private final ClientExpediteurService clientService;

    // --------------------- CREATE ---------------------
    @Operation(summary = "Créer un client expéditeur", description = "Ajoute un nouveau client expéditeur dans le système")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données")
    })
    @PostMapping
    public ResponseEntity<ClientExpediteurDTO> createClient(@Valid @RequestBody ClientExpediteurDTO dto) {
        log.info("Appel API: CREATE Client avec email={}", dto.getEmail());
        ClientExpediteurDTO created = clientService.create(dto);
        log.info("Client créé avec succès: ID={}, email={}", created.getId(), created.getEmail());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // --------------------- READ -----------------------
    @Operation(summary = "Obtenir un client par ID", description = "Récupère les informations d’un client expéditeur via son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client trouvé"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientExpediteurDTO> getClientById(@PathVariable String id) {
        log.info("Appel API: GET Client par ID={}", id);
        ClientExpediteurDTO dto = clientService.getById(id);
        log.info("Client récupéré: ID={}, email={}", dto.getId(), dto.getEmail());
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Lister tous les clients", description = "Renvoie la liste paginée de tous les clients expéditeurs")
    @ApiResponse(responseCode = "200", description = "Liste de clients récupérée avec succès")
    @GetMapping
    public ResponseEntity<Page<ClientExpediteurDTO>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: GET All Clients, page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ClientExpediteurDTO> clients = clientService.getAll(pageable);
        log.info("Nombre de clients récupérés: {}", clients.getTotalElements());
        return ResponseEntity.ok(clients);
    }

    // --------------------- UPDATE ---------------------
    @Operation(summary = "Mettre à jour un client", description = "Modifie les informations d’un client expéditeur existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientExpediteurDTO> updateClient(
            @PathVariable String id,
            @Valid @RequestBody ClientExpediteurDTO dto) {

        log.info("Appel API: UPDATE Client ID={}", id);
        ClientExpediteurDTO updated = clientService.update(id, dto);
        log.info("Client mis à jour: ID={}, email={}", updated.getId(), updated.getEmail());
        return ResponseEntity.ok(updated);
    }

    // --------------------- DELETE ---------------------
    @Operation(summary = "Supprimer un client", description = "Supprime un client expéditeur par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        log.info("Appel API: DELETE Client ID={}", id);
        clientService.delete(id);
        log.info("Client supprimé avec succès: ID={}", id);
        return ResponseEntity.noContent().build();
    }

    // --------------------- SEARCH ---------------------
    @Operation(summary = "Rechercher des clients", description = "Recherche des clients expéditeurs par mot-clé (nom, email, etc.)")
    @ApiResponse(responseCode = "200", description = "Résultats de la recherche")
    @GetMapping("/search")
    public ResponseEntity<Page<ClientExpediteurDTO>> searchClients(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Appel API: SEARCH Clients, keyword='{}', page={}, size={}", keyword, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ClientExpediteurDTO> results = clientService.search(keyword, pageable);
        log.info("Nombre de clients trouvés: {}", results.getTotalElements());
        return ResponseEntity.ok(results);
    }
}
