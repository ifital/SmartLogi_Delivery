package com.smart_delivery_management.smartlogi_delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart_delivery_management.smartlogi_delivery.dto.*;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.PrioriteColis;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import com.smart_delivery_management.smartlogi_delivery.service.ColisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ColisController.class)
class ColisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ColisService colisService;

    @Autowired
    private ObjectMapper objectMapper;

    private ColisDTO colisDTO;
    private ColisCreateDTO colisCreateDTO;

    @BeforeEach
    void setup() {
        colisCreateDTO = new ColisCreateDTO("Colis test", BigDecimal.valueOf(2.5),
                PrioriteColis.URGENTE, "Casablanca", "clientId", "destId", "zoneId");

        colisDTO = new ColisDTO("colisId", "Colis test", BigDecimal.valueOf(2.5),
                StatutColis.CREE, PrioriteColis.URGENTE, "Casablanca",
                LocalDateTime.now(), null, null, "clientId", "John",
                "destId", "Jane", "zoneId", "Zone1", List.of());
    }

    @Test
    void testCreateColis() throws Exception {
        Mockito.when(colisService.createColis(any())).thenReturn(colisDTO);

        mockMvc.perform(post("/api/colis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(colisCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("colisId"));
    }

    @Test
    void testGetColisById() throws Exception {
        Mockito.when(colisService.getColisById("colisId")).thenReturn(colisDTO);

        mockMvc.perform(get("/api/colis/{id}", "colisId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("colisId"));
    }

    @Test
    void testUpdateColis() throws Exception {
        Mockito.when(colisService.updateColis(eq("colisId"), any())).thenReturn(colisDTO);

        mockMvc.perform(put("/api/colis/{id}", "colisId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(colisDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("colisId"));
    }

    @Test
    void testDeleteColis() throws Exception {
        Mockito.doNothing().when(colisService).deleteColis("colisId");

        mockMvc.perform(delete("/api/colis/{id}", "colisId"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAssignerLivreur() throws Exception {
        Mockito.when(colisService.assignerLivreur("colisId", "livreurId")).thenReturn(colisDTO);

        mockMvc.perform(post("/api/colis/{colisId}/assigner-livreur/{livreurId}", "colisId", "livreurId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("colisId"));
    }

    @Test
    void testUpdateStatut() throws Exception {
        Mockito.when(colisService.updateStatut("colisId", StatutColis.LIVRE, "Commentaire"))
                .thenReturn(colisDTO);

        mockMvc.perform(post("/api/colis/{colisId}/statut", "colisId")
                        .param("statut", "LIVRE")
                        .param("commentaire", "Commentaire"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("colisId"));
    }

    @Test
    void testGetAllColis() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(colisService.getAllColis(pageable))
                .thenReturn(new PageImpl<>(List.of(colisDTO), pageable, 1));

        mockMvc.perform(get("/api/colis")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("colisId"));
    }

    @Test
    void testSearchColis() throws Exception {
        ColisSearchCriteria criteria = new ColisSearchCriteria();
        criteria.setVille("Casa");

        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(colisService.searchColis(criteria, pageable))
                .thenReturn(new PageImpl<>(List.of(colisDTO), pageable, 1));

        mockMvc.perform(post("/api/colis/search")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("colisId"));
    }

    @Test
    void testStatsByLivreur() throws Exception {
        ColisStatisticsDTO stats = new ColisStatisticsDTO();
        stats.setLivreurId("livreurId");
        stats.setNombreColis(5L);
        stats.setPoidsTotal(BigDecimal.valueOf(12.5));

        Mockito.when(colisService.getStatisticsByLivreur("livreurId")).thenReturn(stats);

        mockMvc.perform(get("/api/colis/stats/livreur/{livreurId}", "livreurId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.livreurId").value("livreurId"))
                .andExpect(jsonPath("$.nombreColis").value(5));
    }

    @Test
    void testStatsByZone() throws Exception {
        ColisStatisticsDTO stats = new ColisStatisticsDTO();
        stats.setZoneId("zoneId");
        stats.setNombreColis(3L);
        stats.setPoidsTotal(BigDecimal.valueOf(8.5));

        Mockito.when(colisService.getStatisticsByZone("zoneId")).thenReturn(stats);

        mockMvc.perform(get("/api/colis/stats/zone/{zoneId}", "zoneId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zoneId").value("zoneId"))
                .andExpect(jsonPath("$.nombreColis").value(3));
    }

    @Test
    void testGetColisEnRetard() throws Exception {
        Mockito.when(colisService.getColisEnRetard()).thenReturn(List.of(colisDTO));

        mockMvc.perform(get("/api/colis/retard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("colisId"));
    }

    @Test
    void testGetColisPrioritairesNonAssignes() throws Exception {
        Mockito.when(colisService.getColisPrioritairesNonAssignes()).thenReturn(List.of(colisDTO));

        mockMvc.perform(get("/api/colis/prioritaires/non-assignes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("colisId"));
    }

    @Test
    void testGetColisByLivreur() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(colisService.getColisByLivreur("livreurId", pageable))
                .thenReturn(new PageImpl<>(List.of(colisDTO), pageable, 1));

        mockMvc.perform(get("/api/colis/livreur/{livreurId}", "livreurId")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("colisId"));
    }

    @Test
    void testGetColisByClient() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(colisService.getColisByClient("clientId", pageable))
                .thenReturn(new PageImpl<>(List.of(colisDTO), pageable, 1));

        mockMvc.perform(get("/api/colis/client/{clientId}", "clientId")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("colisId"));
    }

    @Test
    void testGetColisByDestinataire() throws Exception {
        Mockito.when(colisService.getColisByDestinataire("destId")).thenReturn(List.of(colisDTO));

        mockMvc.perform(get("/api/colis/destinataire/{destinataireId}", "destId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("colisId"));
    }

    @Test
    void testGetHistorique() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        HistoriqueLivraisonDTO hist = new HistoriqueLivraisonDTO(
                "histId",
                StatutColis.CREE,
                LocalDateTime.now(),  // date
                "Comment"            // commentaire
        );
        Mockito.when(colisService.getHistorique("colisId", pageable))
                .thenReturn(new PageImpl<>(List.of(hist), pageable, 1));

        mockMvc.perform(get("/api/colis/{colisId}/historique", "colisId")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("histId"));
    }

    @Test
    void testGetColisByStatut() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(colisService.getColisByStatut(StatutColis.CREE, pageable))
                .thenReturn(new PageImpl<>(List.of(colisDTO), pageable, 1));

        mockMvc.perform(get("/api/colis/statut/{statut}", "CREE")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("colisId"));
    }

    @Test
    void testGetColisByPriorite() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(colisService.getColisByPriorite(PrioriteColis.URGENTE, pageable))
                .thenReturn(new PageImpl<>(List.of(colisDTO), pageable, 1));

        mockMvc.perform(get("/api/colis/priorite/{priorite}", "URGENTE")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("colisId"));
    }

    @Test
    void testGetColisByZone() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(colisService.getColisByZone("zoneId", pageable))
                .thenReturn(new PageImpl<>(List.of(colisDTO), pageable, 1));

        mockMvc.perform(get("/api/colis/zone/{zoneId}", "zoneId")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("colisId"));
    }

    @Test
    void testGetColisByVille() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(colisService.getColisByVille("Casablanca", pageable))
                .thenReturn(new PageImpl<>(List.of(colisDTO), pageable, 1));

        mockMvc.perform(get("/api/colis/ville/{ville}", "Casablanca")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("colisId"));
    }

    @Test
    void testCountByStatutGroupBy() throws Exception {
        List<Object[]> mockList = List.<Object[]>of(new Object[]{"CREE", 5L});
        Mockito.when(colisService.countByStatutGroupBy()).thenReturn(mockList);

        mockMvc.perform(get("/api/colis/stats/statut"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0][0]").value("CREE"))
                .andExpect(jsonPath("$[0][1]").value(5));
    }

    @Test
    void testCountByZoneGroupBy() throws Exception {
        List<Object[]> mockList = List.<Object[]>of(new Object[]{"zoneId", 3L});
        Mockito.when(colisService.countByZoneGroupBy()).thenReturn(mockList);

        mockMvc.perform(get("/api/colis/stats/zones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0][0]").value("zoneId"))
                .andExpect(jsonPath("$[0][1]").value(3));
    }

    @Test
    void testCountByPrioriteGroupBy() throws Exception {
        List<Object[]> mockList = List.<Object[]>of(new Object[]{"URGENTE", 2L});
        Mockito.when(colisService.countByPrioriteGroupBy()).thenReturn(mockList);

        mockMvc.perform(get("/api/colis/stats/priorite"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0][0]").value("URGENTE"))
                .andExpect(jsonPath("$[0][1]").value(2));
    }

}
