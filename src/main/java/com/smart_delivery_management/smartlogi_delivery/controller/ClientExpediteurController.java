package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.dto.ClientExpediteurDTO;
import com.smart_delivery_management.smartlogi_delivery.service.ClientExpediteurService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ClientExpediteur", description = "Endpoints pour la gestion des clients expéditeurs")
public class ClientExpediteurController {

    private final ClientExpediteurService clientService;

    // --------------------- CREATE ---------------------
    @Operation(summary = "Créer un client expéditeur")
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<ClientExpediteurDTO> createClient(
            @Valid @RequestBody ClientExpediteurDTO dto) {

        ClientExpediteurDTO created = clientService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // --------------------- READ -----------------------
    @Operation(summary = "Obtenir un client par ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<ClientExpediteurDTO> getClientById(@PathVariable String id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    // --------------------- READ ALL -------------------
    @Operation(summary = "Lister tous les clients")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ClientExpediteurDTO>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(clientService.getAll(pageable));
    }

    // --------------------- UPDATE ---------------------
    @Operation(summary = "Mettre à jour un client")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<ClientExpediteurDTO> updateClient(
            @PathVariable String id,
            @Valid @RequestBody ClientExpediteurDTO dto) {

        return ResponseEntity.ok(clientService.update(id, dto));
    }

    // --------------------- DELETE ---------------------
    @Operation(summary = "Supprimer un client")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --------------------- SEARCH ---------------------
    @Operation(summary = "Rechercher des clients")
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ClientExpediteurDTO>> searchClients(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(clientService.search(keyword, pageable));
    }
}
