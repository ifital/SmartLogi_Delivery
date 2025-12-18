package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.entity.Produit;
import com.smart_delivery_management.smartlogi_delivery.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Produits", description = "Gestion des produits")
public class ProduitController {

    private final ProduitService produitService;

    // ------------------- CREATE -------------------
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produit> createProduit(@Valid @RequestBody Produit produit) {
        Produit saved = produitService.save(produit);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ------------------- READ (BY ID) -------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','LIVREUR')")
    public ResponseEntity<Produit> getProduitById(@PathVariable String id) {
        return produitService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ------------------- READ ALL -------------------
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','LIVREUR')")
    public ResponseEntity<Page<Produit>> getAllProduits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Produit> result = produitService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR NOM -------------------
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','LIVREUR')")
    public ResponseEntity<Page<Produit>> searchByNom(
            @RequestParam(required = false, defaultValue = "") String nom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Produit> result = produitService.searchByNom(nom, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- SEARCH PAR CATÉGORIE -------------------
    @GetMapping("/categorie")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','LIVREUR')")
    public ResponseEntity<Page<Produit>> searchByCategorie(
            @RequestParam(required = false, defaultValue = "") String categorie,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Produit> result = produitService.findByCategorie(categorie, pageable);
        return ResponseEntity.ok(result);
    }

    // ------------------- DELETE -------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduit(@PathVariable String id) {
        if (!produitService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        produitService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
