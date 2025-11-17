package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entity.Livreur;
import com.smart_delivery_management.smartlogi_delivery.entity.Zone;
import com.smart_delivery_management.smartlogi_delivery.repository.LivreurRepository;
import com.smart_delivery_management.smartlogi_delivery.service.impl.LivreurServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LivreurServiceImplTest {

    @Mock
    private LivreurRepository livreurRepository;

    @InjectMocks
    private LivreurServiceImpl livreurService;

    private Livreur livreur;
    private Zone zone;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        zone = new Zone("1", "Casablanca", "20000");
        livreur = new Livreur("1", "Dupont", "Jean", "0600000001", "Moto", zone);
    }

    @Test
    @DisplayName("save → doit enregistrer un livreur")
    void testSave() {
        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);

        Livreur saved = livreurService.save(livreur);

        assertThat(saved).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Dupont");
        verify(livreurRepository, times(1)).save(livreur);
    }

    @Test
    @DisplayName("findById → doit retourner un livreur par id")
    void testFindById() {
        when(livreurRepository.findById("1")).thenReturn(Optional.of(livreur));

        Optional<Livreur> result = livreurService.findById("1");

        assertThat(result).isPresent();
        assertThat(result.get().getNom()).isEqualTo("Dupont");
        verify(livreurRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("findAll → doit retourner une page de livreurs")
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = new PageImpl<>(List.of(livreur), pageable, 1);
        when(livreurRepository.findAll(pageable)).thenReturn(page);

        Page<Livreur> result = livreurService.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getNom()).isEqualTo("Dupont");
        verify(livreurRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("searchByNomOrPrenom → doit retourner les livreurs correspondant")
    void testSearchByNomOrPrenom() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = new PageImpl<>(List.of(livreur), pageable, 1);
        when(livreurRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase("Dupont", "Jean", pageable))
                .thenReturn(page);

        Page<Livreur> result = livreurService.searchByNomOrPrenom("Dupont", "Jean", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPrenom()).isEqualTo("Jean");
        verify(livreurRepository, times(1))
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase("Dupont", "Jean", pageable);
    }

    @Test
    @DisplayName("findByZoneAssigneeId → doit retourner les livreurs de la zone")
    void testFindByZoneAssigneeId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = new PageImpl<>(List.of(livreur), pageable, 1);
        when(livreurRepository.findByZoneAssigneeId("1", pageable)).thenReturn(page);

        Page<Livreur> result = livreurService.findByZoneAssigneeId("1", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getZoneAssignee().getNom()).isEqualTo("Casablanca");
        verify(livreurRepository, times(1)).findByZoneAssigneeId("1", pageable);
    }

    @Test
    @DisplayName("findLivreursByZone → doit retourner les livreurs via query custom")
    void testFindLivreursByZone() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = new PageImpl<>(List.of(livreur), pageable, 1);
        when(livreurRepository.findLivreursByZone("1", pageable)).thenReturn(page);

        Page<Livreur> result = livreurService.findLivreursByZone("1", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getZoneAssignee().getNom()).isEqualTo("Casablanca");
        verify(livreurRepository, times(1)).findLivreursByZone("1", pageable);
    }

    @Test
    @DisplayName("deleteById → doit supprimer un livreur")
    void testDeleteById() {
        doNothing().when(livreurRepository).deleteById("1");

        livreurService.deleteById("1");

        verify(livreurRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("existsById → doit retourner true si le livreur existe")
    void testExistsById() {
        when(livreurRepository.existsById("1")).thenReturn(true);

        boolean exists = livreurService.existsById("1");

        assertThat(exists).isTrue();
        verify(livreurRepository, times(1)).existsById("1");
    }
}
