package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entity.Produit;
import com.smart_delivery_management.smartlogi_delivery.repository.ProduitRepository;
import com.smart_delivery_management.smartlogi_delivery.service.impl.ProduitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProduitServiceImplTest {

    @Mock
    private ProduitRepository produitRepository;

    @InjectMocks
    private ProduitServiceImpl produitService;

    private Produit produit1;
    private Produit produit2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produit1 = new Produit("1", "Laptop Dell", "Electronique",
                new BigDecimal("2.5"), new BigDecimal("1200.00"));
        produit2 = new Produit("2", "Laptop HP", "Electronique",
                new BigDecimal("2.3"), new BigDecimal("1100.00"));
    }

    @Test
    @DisplayName("Test save produit")
    void testSaveProduit() {
        when(produitRepository.save(any(Produit.class))).thenReturn(produit1);

        Produit saved = produitService.save(produit1);

        assertThat(saved).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Laptop Dell");
        verify(produitRepository, times(1)).save(produit1);
    }

    @Test
    @DisplayName("Test findById produit")
    void testFindById() {
        when(produitRepository.findById("1")).thenReturn(Optional.of(produit1));

        Optional<Produit> result = produitService.findById("1");

        assertThat(result).isPresent();
        assertThat(result.get().getNom()).isEqualTo("Laptop Dell");
        verify(produitRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Test findAll produits paginés")
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> page = new PageImpl<>(List.of(produit1, produit2));
        when(produitRepository.findAll(pageable)).thenReturn(page);

        Page<Produit> result = produitService.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(produitRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Test searchByNom")
    void testSearchByNom() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> page = new PageImpl<>(List.of(produit1));
        when(produitRepository.findByNomContainingIgnoreCase("Dell", pageable)).thenReturn(page);

        Page<Produit> result = produitService.searchByNom("Dell", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getNom()).isEqualTo("Laptop Dell");
        verify(produitRepository, times(1)).findByNomContainingIgnoreCase("Dell", pageable);
    }

    @Test
    @DisplayName("Test findByCategorie")
    void testFindByCategorie() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> page = new PageImpl<>(List.of(produit1, produit2));
        when(produitRepository.findByCategorie("Electronique", pageable)).thenReturn(page);

        Page<Produit> result = produitService.findByCategorie("Electronique", pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(produitRepository, times(1)).findByCategorie("Electronique", pageable);
    }

    @Test
    @DisplayName("Test deleteById")
    void testDeleteById() {
        doNothing().when(produitRepository).deleteById("1");

        produitService.deleteById("1");

        verify(produitRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Test existsById")
    void testExistsById() {
        when(produitRepository.existsById("1")).thenReturn(true);

        boolean exists = produitService.existsById("1");

        assertThat(exists).isTrue();
        verify(produitRepository, times(1)).existsById("1");
    }
}
