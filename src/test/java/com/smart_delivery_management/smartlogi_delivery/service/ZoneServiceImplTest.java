package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entity.Zone;
import com.smart_delivery_management.smartlogi_delivery.repository.ZoneRepository;
import com.smart_delivery_management.smartlogi_delivery.service.impl.ZoneServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZoneServiceImplTest {

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneServiceImpl zoneService;

    private Zone createZone() {
        return new Zone(
                "1",
                "Zone Centre",
                "75001"
        );
    }

    @Test
    void save_shouldSaveZone() {
        Zone zone = createZone();
        when(zoneRepository.save(zone)).thenReturn(zone);

        Zone saved = zoneService.save(zone);

        assertThat(saved).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Zone Centre");
        verify(zoneRepository).save(zone);
    }

    @Test
    void findById_shouldReturnZone() {
        Zone zone = createZone();
        when(zoneRepository.findById("1")).thenReturn(Optional.of(zone));

        Optional<Zone> result = zoneService.findById("1");

        assertThat(result).isPresent();
        assertThat(result.get().getCodePostal()).isEqualTo("75001");
    }

    @Test
    void findAll_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Zone> page = new PageImpl<>(List.of(createZone()));
        when(zoneRepository.findAll(pageable)).thenReturn(page);

        Page<Zone> result = zoneService.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void searchByNom_shouldReturnMatchingZones() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Zone> page = new PageImpl<>(List.of(createZone()));
        when(zoneRepository.findByNomContainingIgnoreCase("centre", pageable)).thenReturn(page);

        Page<Zone> result = zoneService.searchByNom("centre", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(zoneRepository).findByNomContainingIgnoreCase("centre", pageable);
    }

    @Test
    void findByCodePostal_shouldReturnZones() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Zone> page = new PageImpl<>(List.of(createZone()));
        when(zoneRepository.findByCodePostal("75001", pageable)).thenReturn(page);

        Page<Zone> result = zoneService.findByCodePostal("75001", pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void deleteById_shouldCallRepository() {
        doNothing().when(zoneRepository).deleteById("1");

        zoneService.deleteById("1");

        verify(zoneRepository).deleteById("1");
    }

    @Test
    void existsById_shouldReturnTrue() {
        when(zoneRepository.existsById("1")).thenReturn(true);

        boolean exists = zoneService.existsById("1");

        assertThat(exists).isTrue();
    }
}
