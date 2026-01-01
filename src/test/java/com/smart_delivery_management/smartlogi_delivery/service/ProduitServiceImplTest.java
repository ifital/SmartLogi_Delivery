package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entity.Produit;
import com.smart_delivery_management.smartlogi_delivery.repository.ProduitRepository;
import com.smart_delivery_management.smartlogi_delivery.service.impl.ProduitServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceImplTest {

    @Mock
    private ProduitRepository produitRepository;

    @InjectMocks
    private ProduitServiceImpl produitService;

    private Produit createProduit() {
        return new Produit(
                "1",
                "Ordinateur",
                "Informatique",
                new BigDecimal("2.50"),
                new BigDecimal("1200.00")
        );
    }

    @Test
    void save_shouldSaveProduit() {
        Produit produit = createProduit();
        when(produitRepository.save(produit)).thenReturn(produit);

        Produit saved = produitService.save(produit);

        assertThat(saved).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Ordinateur");
        verify(produitRepository, times(1)).save(produit);
    }

    @Test
    void findById_shouldReturnProduit() {
        Produit produit = createProduit();
        when(produitRepository.findById("1")).thenReturn(Optional.of(produit));

        Optional<Produit> result = produitService.findById("1");

        assertThat(result).isPresent();
        assertThat(result.get().getCategorie()).isEqualTo("Informatique");
    }

    @Test
    void findAll_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> page = new PageImpl<>(List.of(createProduit()));
        when(produitRepository.findAll(pageable)).thenReturn(page);

        Page<Produit> result = produitService.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void deleteById_shouldCallRepository() {
        doNothing().when(produitRepository).deleteById("1");

        produitService.deleteById("1");

        verify(produitRepository).deleteById("1");
    }
}
