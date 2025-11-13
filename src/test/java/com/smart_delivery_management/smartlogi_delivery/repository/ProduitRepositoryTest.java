package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entity.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProduitRepositoryTest {

    @Autowired
    private ProduitRepository produitRepository;

    private Produit produit1;
    private Produit produit2;

    @BeforeEach
    void setUp() {
        produitRepository.deleteAll();

        produit1 = new Produit();
        produit1.setNom("Laptop Dell");
        produit1.setCategorie("Electronique");
        produit1.setPoids(new BigDecimal("2.5"));
        produit1.setPrix(new BigDecimal("1200.00"));

        produit2 = new Produit();
        produit2.setNom("Laptop HP");
        produit2.setCategorie("Electronique");
        produit2.setPoids(new BigDecimal("2.3"));
        produit2.setPrix(new BigDecimal("1100.00"));

        produitRepository.save(produit1);
        produitRepository.save(produit2);
    }

    @Test
    @DisplayName("Test findByNomContainingIgnoreCase retourne les produits correspondants")
    void testFindByNomContainingIgnoreCase() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> result = produitRepository.findByNomContainingIgnoreCase("laptop", pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("nom")
                .containsExactlyInAnyOrder("Laptop Dell", "Laptop HP");
    }

    @Test
    @DisplayName("Test findByCategorie retourne les produits d'une catégorie")
    void testFindByCategorie() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> result = produitRepository.findByCategorie("Electronique", pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("categorie")
                .containsOnly("Electronique");
    }

    @Test
    @DisplayName("Test save produit")
    void testSaveProduit() {
        Produit produit = new Produit();
        produit.setNom("Smartphone Samsung");
        produit.setCategorie("Electronique");
        produit.setPoids(new BigDecimal("0.3"));
        produit.setPrix(new BigDecimal("800.00"));

        Produit saved = produitRepository.save(produit);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Smartphone Samsung");
        assertThat(saved.getCategorie()).isEqualTo("Electronique");
    }

    @Test
    @DisplayName("Test delete produit")
    void testDeleteProduit() {
        produitRepository.delete(produit1);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> result = produitRepository.findByNomContainingIgnoreCase("Laptop", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getNom()).isEqualTo("Laptop HP");
    }
}
