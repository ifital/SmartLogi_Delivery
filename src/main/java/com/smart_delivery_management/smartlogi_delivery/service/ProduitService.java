package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProduitService {
    Produit save(Produit produit);
    Optional<Produit> findById(String id);
    Page<Produit> findAll(Pageable pageable);
    Page<Produit> searchByNom(String nom, Pageable pageable);
    Page<Produit> findByCategorie(String categorie, Pageable pageable);
    void deleteById(String id);
    boolean existsById(String id);
}