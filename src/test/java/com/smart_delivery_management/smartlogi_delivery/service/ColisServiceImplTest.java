package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.dto.*;
import com.smart_delivery_management.smartlogi_delivery.entity.*;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.PrioriteColis;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import com.smart_delivery_management.smartlogi_delivery.exception.ResourceNotFoundException;
import com.smart_delivery_management.smartlogi_delivery.mapper.ColisMapper;
import com.smart_delivery_management.smartlogi_delivery.repository.*;
import com.smart_delivery_management.smartlogi_delivery.service.impl.ColisServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ColisServiceImplTest {

    @InjectMocks
    private ColisServiceImpl colisService;

    @Mock
    private ColisRepository colisRepository;

    @Mock
    private ClientExpediteurRepository clientRepository;

    @Mock
    private DestinataireRepository destinataireRepository;

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private LivreurRepository livreurRepository;

    @Mock
    private HistoriqueLivraisonRepository historiqueRepository;

    @Mock
    private ColisMapper colisMapper;

    private Colis colis;
    private ColisDTO colisDTO;
    private ColisCreateDTO colisCreateDTO;
    private ClientExpediteur client;
    private Destinataire destinataire;
    private Zone zone;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        client = new ClientExpediteur("clientId", "John", "Doe", "john@example.com", "0600000000", "Adresse 1");
        destinataire = new Destinataire("destId", "Jane", "Doe", "jane@example.com", "0611111111", "Adresse 2");
        zone = new Zone("zoneId", "Zone1", "10000");

        colis = new Colis("colisId", "Colis test", BigDecimal.valueOf(2.5),
                StatutColis.CREE, PrioriteColis.URGENTE, "Casablanca",
                LocalDateTime.now(), null, client, destinataire, zone, List.of(), List.of());

        colisDTO = new ColisDTO("colisId", "Colis test", BigDecimal.valueOf(2.5),
                StatutColis.CREE, PrioriteColis.URGENTE, "Casablanca", LocalDateTime.now(),
                null, null, "clientId", "John", "destId", "Jane", "zoneId", "Zone1", List.of());

        colisCreateDTO = new ColisCreateDTO("Colis test", BigDecimal.valueOf(2.5),
                PrioriteColis.URGENTE, "Casablanca", "clientId", "destId", "zoneId");
    }

    // ------------------------- CRUD -------------------------
    @Test
    void testCreateColis() {
        when(clientRepository.findById("clientId")).thenReturn(Optional.of(client));
        when(destinataireRepository.findById("destId")).thenReturn(Optional.of(destinataire));
        when(zoneRepository.findById("zoneId")).thenReturn(Optional.of(zone));
        when(colisMapper.toEntityFromCreate(colisCreateDTO)).thenReturn(colis);
        when(colisRepository.save(colis)).thenReturn(colis);
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        ColisDTO result = colisService.createColis(colisCreateDTO);

        assertThat(result.getId()).isEqualTo("colisId");
        verify(historiqueRepository, times(1)).save(any());
    }

    @Test
    void testGetColisById() {
        when(colisRepository.findById("colisId")).thenReturn(Optional.of(colis));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        ColisDTO result = colisService.getColisById("colisId");

        assertThat(result.getDescription()).isEqualTo("Colis test");
    }

    @Test
    void testGetColisById_NotFound() {
        when(colisRepository.findById("colisId")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> colisService.getColisById("colisId"));
    }

    @Test
    void testUpdateColis() {
        when(colisRepository.findById("colisId")).thenReturn(Optional.of(colis));
        when(colisRepository.save(any())).thenReturn(colis);
        when(colisMapper.toDto(any())).thenReturn(colisDTO);

        ColisDTO updated = colisService.updateColis("colisId", colisDTO);

        assertThat(updated).isNotNull();
        verify(colisRepository, times(1)).save(colis);
    }

    @Test
    void testDeleteColis() {
        when(colisRepository.existsById("colisId")).thenReturn(true);

        colisService.deleteColis("colisId");
        verify(colisRepository, times(1)).deleteById("colisId");
    }

    @Test
    void testDeleteColis_NotFound() {
        when(colisRepository.existsById("colisId")).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> colisService.deleteColis("colisId"));
    }

    // ------------------------- Livreur -------------------------
    @Test
    void testAssignerLivreur() {
        Livreur livreur = new Livreur("livreurId", "Ali", "Benz", "0600000000", "Van", zone);
        when(colisRepository.findById("colisId")).thenReturn(Optional.of(colis));
        when(livreurRepository.findById("livreurId")).thenReturn(Optional.of(livreur));
        when(colisRepository.save(any())).thenReturn(colis);
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        ColisDTO result = colisService.assignerLivreur("colisId", "livreurId");

        assertThat(result).isNotNull();
        verify(historiqueRepository, times(1)).save(any());
        assertThat(colis.getLivreur()).isEqualTo(livreur);
        assertThat(colis.getStatut()).isEqualTo(StatutColis.COLLECTE);
    }

    @Test
    void testGetColisByLivreur() {
        when(colisRepository.findByLivreurId("livreurId")).thenReturn(List.of(colis));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        List<ColisDTO> result = colisService.getColisByLivreur("livreurId");

        assertThat(result).hasSize(1);
    }

    // ------------------------- Statut / Historique -------------------------
    @Test
    void testUpdateStatut() {
        when(colisRepository.findById("colisId")).thenReturn(Optional.of(colis));
        when(colisRepository.save(colis)).thenReturn(colis);
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        ColisDTO result = colisService.updateStatut("colisId", StatutColis.LIVRE, "Livré");

        assertThat(result).isNotNull();
        assertThat(colis.getStatut()).isEqualTo(StatutColis.LIVRE);
        verify(historiqueRepository, times(1)).save(any());
    }

    @Test
    void testGetHistorique() {
        Pageable pageable = mock(Pageable.class);

        HistoriqueLivraison h = new HistoriqueLivraison();
        h.setId("hist1");
        h.setStatut(StatutColis.CREE);
        h.setDateChangement(LocalDateTime.now());
        h.setCommentaire("Créé");

        when(colisRepository.existsById("colisId")).thenReturn(true);
        when(historiqueRepository.findByColisIdOrderByDateChangementDesc("colisId", pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(h)));

        Page<HistoriqueLivraisonDTO> page = colisService.getHistorique("colisId", pageable);

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getCommentaire()).isEqualTo("Créé");
    }

    // ------------------------- Recherche / Filtrage -------------------------
    @Test
    void testSearchColis() {
        Pageable pageable = mock(Pageable.class);

        when(colisRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(colis)));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        ColisSearchCriteria criteria = new ColisSearchCriteria();
        criteria.setVille("Casa");

        Page<ColisDTO> page = colisService.searchColis(criteria, pageable);

        assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void testGetColisByClient() {
        Pageable pageable = mock(Pageable.class);
        when(colisRepository.findByClientExpediteurId("clientId", pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(colis)));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        Page<ColisDTO> page = colisService.getColisByClient("clientId", pageable);

        assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void testGetColisByDestinataire() {
        when(colisRepository.findByDestinataireId("destId")).thenReturn(List.of(colis));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        List<ColisDTO> result = colisService.getColisByDestinataire("destId");

        assertThat(result).hasSize(1);
    }

    // ------------------------- Statistiques -------------------------
    @Test
    void testGetStatisticsByLivreur() {
        Livreur livreur = new Livreur("livreurId", "Ali", "Benz", "0600000", "Moto", zone);

        when(livreurRepository.findById("livreurId")).thenReturn(Optional.of(livreur));
        when(colisRepository.countByLivreurId("livreurId")).thenReturn(5L);
        when(colisRepository.sumPoidsByLivreurId("livreurId")).thenReturn(BigDecimal.valueOf(12.5));

        ColisStatisticsDTO stats = colisService.getStatisticsByLivreur("livreurId");

        assertThat(stats.getNombreColis()).isEqualTo(5L);
        assertThat(stats.getPoidsTotal()).isEqualTo(BigDecimal.valueOf(12.5));
    }

    @Test
    void testGetStatisticsByZone() {
        when(zoneRepository.findById("zoneId")).thenReturn(Optional.of(zone));
        when(colisRepository.countByZoneId("zoneId")).thenReturn(3L);
        when(colisRepository.sumPoidsByZoneId("zoneId")).thenReturn(BigDecimal.valueOf(7.8));

        ColisStatisticsDTO stats = colisService.getStatisticsByZone("zoneId");

        assertThat(stats.getNombreColis()).isEqualTo(3L);
    }

    // ------------------------- Colis en retard / prioritaires -------------------------
    @Test
    void testGetColisEnRetard() {
        when(colisRepository.findColisEnRetard(any(LocalDateTime.class))).thenReturn(List.of(colis));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        List<ColisDTO> result = colisService.getColisEnRetard();

        assertThat(result).hasSize(1);
        verify(colisRepository, times(1)).findColisEnRetard(any());
    }

    @Test
    void testGetColisPrioritairesNonAssignes() {
        when(colisRepository.findColisPrioritairesNonAssignes()).thenReturn(List.of(colis));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        List<ColisDTO> result = colisService.getColisPrioritairesNonAssignes();

        assertThat(result).hasSize(1);
    }

    // ------------------------- Filtres par statut / priorité / zone / ville -------------------------
    @Test
    void testGetColisByStatut() {
        Pageable pageable = mock(Pageable.class);
        when(colisRepository.findByStatut(StatutColis.CREE, pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(colis)));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        Page<ColisDTO> result = colisService.getColisByStatut(StatutColis.CREE, pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testGetColisByPriorite() {
        Pageable pageable = mock(Pageable.class);
        when(colisRepository.findByPriorite(PrioriteColis.URGENTE, pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(colis)));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        Page<ColisDTO> result = colisService.getColisByPriorite(PrioriteColis.URGENTE, pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testGetColisByZone() {
        Pageable pageable = mock(Pageable.class);
        when(colisRepository.findByZoneId("zoneId", pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(colis)));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        Page<ColisDTO> result = colisService.getColisByZone("zoneId", pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testGetColisByVille() {
        Pageable pageable = mock(Pageable.class);
        when(colisRepository.findByVilleDestinationContainingIgnoreCase("Casa", pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(colis)));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        Page<ColisDTO> result = colisService.getColisByVille("Casa", pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testGetColisByLivreur_Pageable() {
        Pageable pageable = mock(Pageable.class);
        when(colisRepository.findByLivreurId("livreurId", pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(colis)));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        Page<ColisDTO> result = colisService.getColisByLivreur("livreurId", pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    // ------------------------- Count / GroupBy -------------------------
    @Test
    void testCountByStatutGroupBy() {
        List<Object[]> mockResult = List.<Object[]>of(new Object[]{"CREE", 5L});
        when(colisRepository.countByStatutGroupBy()).thenReturn(mockResult);

        List<Object[]> result = colisService.countByStatutGroupBy();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)[0]).isEqualTo("CREE");
        assertThat(result.get(0)[1]).isEqualTo(5L);
    }

    @Test
    void testCountByZoneGroupBy() {
        List<Object[]> mockResult = List.<Object[]>of(new Object[]{"zoneId", 3L});
        when(colisRepository.countByZoneGroupBy()).thenReturn(mockResult);

        List<Object[]> result = colisService.countByZoneGroupBy();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)[0]).isEqualTo("zoneId");
        assertThat(result.get(0)[1]).isEqualTo(3L);
    }

    @Test
    void testCountByPrioriteGroupBy() {
        List<Object[]> mockResult = List.<Object[]>of(new Object[]{"URGENTE", 2L});
        when(colisRepository.countByPrioriteGroupBy()).thenReturn(mockResult);

        List<Object[]> result = colisService.countByPrioriteGroupBy();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)[0]).isEqualTo("URGENTE");
        assertThat(result.get(0)[1]).isEqualTo(2L);
    }

}
