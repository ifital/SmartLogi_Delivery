package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entity.ColisProduit;
import com.smart_delivery_management.smartlogi_delivery.entity.ColisProduitId;
import com.smart_delivery_management.smartlogi_delivery.repository.ColisProduitRepository;
import com.smart_delivery_management.smartlogi_delivery.service.impl.ColisProduitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ColisProduitServiceImplTest {

    @Mock
    private ColisProduitRepository colisProduitRepository;

    @InjectMocks
    private ColisProduitServiceImpl colisProduitService;

    private ColisProduit colisProduit;
    private ColisProduitId colisProduitId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        colisProduitId = new ColisProduitId("colis-1", "produit-1");

        colisProduit = new ColisProduit();
        colisProduit.setId(colisProduitId);
        colisProduit.setQuantite(2);
        colisProduit.setPrix(BigDecimal.valueOf(20.5));
    }

    @Test
    void testSave() {
        when(colisProduitRepository.save(colisProduit)).thenReturn(colisProduit);

        ColisProduit saved = colisProduitService.save(colisProduit);

        assertThat(saved).isEqualTo(colisProduit);
        verify(colisProduitRepository, times(1)).save(colisProduit);
    }

    @Test
    void testFindById() {
        when(colisProduitRepository.findById(colisProduitId)).thenReturn(Optional.of(colisProduit));

        Optional<ColisProduit> result = colisProduitService.findById(colisProduitId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(colisProduit);
        verify(colisProduitRepository, times(1)).findById(colisProduitId);
    }

    @Test
    void testFindAll() {
        Page<ColisProduit> page = new PageImpl<>(List.of(colisProduit));
        when(colisProduitRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<ColisProduit> result = colisProduitService.findAll(PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(colisProduitRepository, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    void testFindByColisId() {
        Page<ColisProduit> page = new PageImpl<>(List.of(colisProduit));
        when(colisProduitRepository.findByColisId("colis-1", PageRequest.of(0, 10))).thenReturn(page);

        Page<ColisProduit> result = colisProduitService.findByColisId("colis-1", PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(colisProduitRepository, times(1)).findByColisId("colis-1", PageRequest.of(0, 10));
    }

    @Test
    void testFindByProduitId() {
        Page<ColisProduit> page = new PageImpl<>(List.of(colisProduit));
        when(colisProduitRepository.findByProduitId("produit-1", PageRequest.of(0, 10))).thenReturn(page);

        Page<ColisProduit> result = colisProduitService.findByProduitId("produit-1", PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(colisProduitRepository, times(1)).findByProduitId("produit-1", PageRequest.of(0, 10));
    }

    @Test
    void testDeleteById() {
        doNothing().when(colisProduitRepository).deleteById(colisProduitId);

        colisProduitService.deleteById(colisProduitId);

        verify(colisProduitRepository, times(1)).deleteById(colisProduitId);
    }

    @Test
    void testDeleteByColisId() {
        doNothing().when(colisProduitRepository).deleteByColisId("colis-1");

        colisProduitService.deleteByColisId("colis-1");

        verify(colisProduitRepository, times(1)).deleteByColisId("colis-1");
    }

    @Test
    void testExistsById() {
        when(colisProduitRepository.existsById(colisProduitId)).thenReturn(true);

        boolean exists = colisProduitService.existsById(colisProduitId);

        assertThat(exists).isTrue();
        verify(colisProduitRepository, times(1)).existsById(colisProduitId);
    }
}
