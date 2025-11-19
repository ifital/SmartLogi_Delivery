package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entity.Colis;
import com.smart_delivery_management.smartlogi_delivery.entity.HistoriqueLivraison;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.PrioriteColis;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import com.smart_delivery_management.smartlogi_delivery.repository.HistoriqueLivraisonRepository;
import com.smart_delivery_management.smartlogi_delivery.service.impl.HistoriqueLivraisonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HistoriqueLivraisonServiceImplTest {

    @Mock
    private HistoriqueLivraisonRepository historiqueRepo;

    @InjectMocks
    private HistoriqueLivraisonServiceImpl historiqueService;

    private Colis colis;
    private HistoriqueLivraison historique;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        colis = new Colis();
        colis.setId("colis-123");
        colis.setDescription("Test colis");
        colis.setPoids(BigDecimal.valueOf(4.2));
        colis.setPriorite(PrioriteColis.NORMALE);
        colis.setStatut(StatutColis.CREE); // ✔️ corrigé
        colis.setVilleDestination("Casablanca");

        historique = new HistoriqueLivraison();
        historique.setId("hist-1");
        historique.setColis(colis);
        historique.setStatut(StatutColis.CREE); // ✔️ corrigé
        historique.setCommentaire("Créé");
    }

    @Test
    @DisplayName("save() doit sauvegarder un historique")
    void testSave() {
        when(historiqueRepo.save(historique)).thenReturn(historique);

        HistoriqueLivraison result = historiqueService.save(historique);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("hist-1");
        verify(historiqueRepo, times(1)).save(historique);
    }

    @Test
    @DisplayName("findById() doit retourner un historique si trouvé")
    void testFindByIdFound() {
        when(historiqueRepo.findById("hist-1")).thenReturn(Optional.of(historique));

        Optional<HistoriqueLivraison> result = historiqueService.findById("hist-1");

        assertThat(result).isPresent();
        assertThat(result.get().getColis().getId()).isEqualTo("colis-123");
    }

    @Test
    @DisplayName("findById() doit retourner Optional.empty si non trouvé")
    void testFindByIdNotFound() {
        when(historiqueRepo.findById("unknown")).thenReturn(Optional.empty());

        Optional<HistoriqueLivraison> result = historiqueService.findById("unknown");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll() doit retourner une page d'historiques")
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<HistoriqueLivraison> page = new PageImpl<>(List.of(historique));

        when(historiqueRepo.findAll(pageable)).thenReturn(page);

        Page<HistoriqueLivraison> result = historiqueService.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(historiqueRepo, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("findByColisIdOrderByDateDesc() doit retourner les historiques du colis")
    void testFindByColisIdOrderByDateDesc() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<HistoriqueLivraison> page = new PageImpl<>(List.of(historique));

        when(historiqueRepo.findByColisIdOrderByDateChangementDesc("colis-123", pageable))
                .thenReturn(page);

        Page<HistoriqueLivraison> result =
                historiqueService.findByColisIdOrderByDateDesc("colis-123", pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(historiqueRepo, times(1))
                .findByColisIdOrderByDateChangementDesc("colis-123", pageable);
    }

    @Test
    @DisplayName("findByStatut() doit retourner les historiques filtrés")
    void testFindByStatut() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<HistoriqueLivraison> page = new PageImpl<>(List.of(historique));

        when(historiqueRepo.findByStatut(StatutColis.CREE, pageable)) // ✔️ corrigé
                .thenReturn(page);

        Page<HistoriqueLivraison> result =
                historiqueService.findByStatut(StatutColis.CREE, pageable); // ✔️ corrigé

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStatut()).isEqualTo(StatutColis.CREE); // ✔️ corrigé

        verify(historiqueRepo, times(1))
                .findByStatut(StatutColis.CREE, pageable); // ✔️ corrigé
    }

    @Test
    @DisplayName("deleteById() doit supprimer un historique")
    void testDeleteById() {
        doNothing().when(historiqueRepo).deleteById("hist-1");

        historiqueService.deleteById("hist-1");

        verify(historiqueRepo, times(1)).deleteById("hist-1");
    }

    @Test
    @DisplayName("existsById() doit retourner true si l'historique existe")
    void testExistsById() {
        when(historiqueRepo.existsById("hist-1")).thenReturn(true);

        boolean exists = historiqueService.existsById("hist-1");

        assertThat(exists).isTrue();
        verify(historiqueRepo, times(1)).existsById("hist-1");
    }
}
