package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entity.Zone;
import com.smart_delivery_management.smartlogi_delivery.repository.ZoneRepository;
import com.smart_delivery_management.smartlogi_delivery.service.impl.ZoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ZoneServiceImplTest {

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneServiceImpl zoneService;

    private Zone zone;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        zone = new Zone("1", "Casablanca", "20000");
    }

    // ---------- SAVE ----------
    @Test
    @DisplayName("Devrait sauvegarder une zone")
    void testSave() {
        when(zoneRepository.save(zone)).thenReturn(zone);

        Zone saved = zoneService.save(zone);

        assertThat(saved).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Casablanca");
        verify(zoneRepository, times(1)).save(zone);
    }

    // ---------- FIND BY ID ----------
    @Test
    @DisplayName("Devrait récupérer une zone par ID")
    void testFindById() {
        when(zoneRepository.findById("1")).thenReturn(Optional.of(zone));

        Optional<Zone> found = zoneService.findById("1");

        assertThat(found).isPresent();
        assertThat(found.get().getNom()).isEqualTo("Casablanca");
        verify(zoneRepository, times(1)).findById("1");
    }

    // ---------- FIND ALL ----------
    @Test
    @DisplayName("Devrait récupérer toutes les zones paginées")
    void testFindAll() {
        Page<Zone> page = new PageImpl<>(List.of(zone));
        when(zoneRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<Zone> result = zoneService.findAll(PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(zoneRepository, times(1)).findAll(PageRequest.of(0, 10));
    }

    // ---------- SEARCH BY NOM ----------
    @Test
    @DisplayName("Devrait rechercher des zones par nom")
    void testSearchByNom() {
        Page<Zone> page = new PageImpl<>(List.of(zone));
        when(zoneRepository.findByNomContainingIgnoreCase("casa", PageRequest.of(0, 10)))
                .thenReturn(page);

        Page<Zone> result = zoneService.searchByNom("casa", PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(zoneRepository, times(1)).findByNomContainingIgnoreCase("casa", PageRequest.of(0, 10));
    }

    // ---------- FIND BY CODE POSTAL ----------
    @Test
    @DisplayName("Devrait rechercher des zones par code postal")
    void testFindByCodePostal() {
        Page<Zone> page = new PageImpl<>(List.of(zone));
        when(zoneRepository.findByCodePostal("20000", PageRequest.of(0, 10)))
                .thenReturn(page);

        Page<Zone> result = zoneService.findByCodePostal("20000", PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(zoneRepository, times(1)).findByCodePostal("20000", PageRequest.of(0, 10));
    }

    // ---------- DELETE ----------
    @Test
    @DisplayName("Devrait supprimer une zone par ID")
    void testDeleteById() {
        doNothing().when(zoneRepository).deleteById("1");

        zoneService.deleteById("1");

        verify(zoneRepository, times(1)).deleteById("1");
    }

    // ---------- EXISTS BY ID ----------
    @Test
    @DisplayName("Devrait vérifier l'existence d'une zone")
    void testExistsById() {
        when(zoneRepository.existsById("1")).thenReturn(true);

        boolean exists = zoneService.existsById("1");

        assertThat(exists).isTrue();
        verify(zoneRepository, times(1)).existsById("1");
    }
}
