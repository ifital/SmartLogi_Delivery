package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entities.Produit;
import java.util.List;
import java.util.Optional;

public interface ProduitService {
    Produit save(Produit produit);
    Optional<Produit> findById(String id);
    List<Produit> findAll();
    List<Produit> searchByNom(String nom);
    List<Produit> findByCategorie(String categorie);
    void deleteById(String id);
    boolean existsById(String id);
}