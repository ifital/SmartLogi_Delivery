package com.smart_delivery_management.smartlogi_delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart_delivery_management.smartlogi_delivery.entity.Colis;
import com.smart_delivery_management.smartlogi_delivery.entity.HistoriqueLivraison;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import com.smart_delivery_management.smartlogi_delivery.service.HistoriqueLivraisonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistoriqueLivraisonController.class)
class HistoriqueLivraisonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoriqueLivraisonService historiqueService;

    @Autowired
    private ObjectMapper objectMapper;

    private HistoriqueLivraison getHistorique() {
        HistoriqueLivraison h = new HistoriqueLivraison();
        h.setId("hist-1");
        h.setStatut(StatutColis.CREE);
        h.setCommentaire("Test historique");
        h.setDateChangement(LocalDateTime.now());
        return h;
    }

    // ------------------- FIND BY ID -------------------
    @Test
    @DisplayName("GET /api/historique-livraisons/{id} doit retourner un historique")
    void testGetByIdFound() throws Exception {
        HistoriqueLivraison historique = getHistorique();

        when(historiqueService.findById("hist-1"))
                .thenReturn(Optional.of(historique));

        mockMvc.perform(get("/api/historique-livraisons/hist-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("hist-1"));
    }

    @Test
    @DisplayName("GET /api/historique-livraisons/{id} doit retourner 404 si non trouvé")
    void testGetByIdNotFound() throws Exception {
        when(historiqueService.findById("unknown"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/historique-livraisons/unknown"))
                .andExpect(status().isNotFound());
    }

    // ------------------- GET ALL -------------------
    @Test
    @DisplayName("GET /api/historique-livraisons doit retourner page d'historiques")
    void testGetAllHistoriques() throws Exception {
        HistoriqueLivraison historique = getHistorique();

        Pageable pageable = PageRequest.of(0, 10);
        Page<HistoriqueLivraison> page =
                new PageImpl<>(List.of(historique), pageable, 1);

        when(historiqueService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get("/api/historique-livraisons")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("hist-1"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    // ------------------- GET BY COLIS -------------------
    @Test
    @DisplayName("GET /api/historique-livraisons/colis/{id} doit retourner historiques du colis")
    void testGetByColisId() throws Exception {
        HistoriqueLivraison historique = getHistorique();

        Pageable pageable = PageRequest.of(0, 10);
        Page<HistoriqueLivraison> page =
                new PageImpl<>(List.of(historique), pageable, 1);

        when(historiqueService.findByColisIdOrderByDateDesc("colis-123", pageable))
                .thenReturn(page);

        mockMvc.perform(get("/api/historique-livraisons/colis/colis-123")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("hist-1"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    // ------------------- GET BY STATUT -------------------
    @Test
    @DisplayName("GET /api/historique-livraisons/statut?statut=CREE retourne filtrage statut")
    void testGetByStatut() throws Exception {
        HistoriqueLivraison historique = getHistorique();

        Pageable pageable = PageRequest.of(0, 10);
        Page<HistoriqueLivraison> page =
                new PageImpl<>(List.of(historique), pageable, 1);

        when(historiqueService.findByStatut(StatutColis.CREE, pageable))
                .thenReturn(page);

        mockMvc.perform(get("/api/historique-livraisons/statut")
                        .param("statut", "CREE")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].statut").value("CREE"));
    }

    // ------------------- DELETE -------------------
    @Test
    @DisplayName("DELETE /api/historique-livraisons/{id} doit supprimer si existe")
    void testDeleteHistorique() throws Exception {
        when(historiqueService.existsById("hist-1")).thenReturn(true);
        doNothing().when(historiqueService).deleteById("hist-1");

        mockMvc.perform(delete("/api/historique-livraisons/hist-1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/historique-livraisons/{id} doit retourner 404 si n'existe pas")
    void testDeleteHistoriqueNotFound() throws Exception {
        when(historiqueService.existsById("unknown")).thenReturn(false);

        mockMvc.perform(delete("/api/historique-livraisons/unknown"))
                .andExpect(status().isNotFound());
    }
}
