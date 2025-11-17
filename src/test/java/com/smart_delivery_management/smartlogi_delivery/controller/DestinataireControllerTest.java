package com.smart_delivery_management.smartlogi_delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart_delivery_management.smartlogi_delivery.entity.Destinataire;
import com.smart_delivery_management.smartlogi_delivery.service.DestinataireService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DestinataireController.class)
class DestinataireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DestinataireService destinataireService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Destinataire destinataire = new Destinataire(
            "1", "Dupont", "Jean", "jean@example.com", "0600000001", "Rue de Paris");

    // ----------------- CREATE -----------------
    @Test
    @DisplayName("POST /api/destinataires → doit créer un destinataire")
    void testCreateDestinataire() throws Exception {
        when(destinataireService.save(any(Destinataire.class))).thenReturn(destinataire);

        mockMvc.perform(post("/api/destinataires")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(destinataire)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Dupont"))
                .andExpect(jsonPath("$.prenom").value("Jean"));

        verify(destinataireService, times(1)).save(any(Destinataire.class));
    }

    // ----------------- GET BY ID -----------------
    @Test
    @DisplayName("GET /api/destinataires/{id} → doit retourner un destinataire")
    void testGetDestinataireById() throws Exception {
        when(destinataireService.findById("1")).thenReturn(Optional.of(destinataire));

        mockMvc.perform(get("/api/destinataires/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nom").value("Dupont"));

        verify(destinataireService, times(1)).findById("1");
    }

    @Test
    @DisplayName("GET /api/destinataires/{id} → destinataire non trouvé renvoie 404")
    void testGetDestinataireByIdNotFound() throws Exception {
        when(destinataireService.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/destinataires/999"))
                .andExpect(status().isNotFound());

        verify(destinataireService, times(1)).findById("999");
    }

    // ----------------- GET ALL -----------------
    @Test
    @DisplayName("GET /api/destinataires → doit retourner une page de destinataires")
    void testGetAllDestinataires() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Destinataire> page = new PageImpl<>(List.of(destinataire), pageable, 1);

        when(destinataireService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/destinataires?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nom").value("Dupont"))
                .andExpect(jsonPath("$.content[0].prenom").value("Jean"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));

        verify(destinataireService, times(1)).findAll(any(Pageable.class));
    }

    // ----------------- SEARCH BY NOM/PRENOM -----------------
    @Test
    @DisplayName("GET /api/destinataires/search → doit retourner les résultats par nom/prénom")
    void testSearchByNomOrPrenom() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Destinataire> page = new PageImpl<>(List.of(destinataire), pageable, 1);

        when(destinataireService.searchByNomOrPrenom(eq("Dupont"), eq("Jean"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/destinataires/search?nom=Dupont&prenom=Jean&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].prenom").value("Jean"))
                .andExpect(jsonPath("$.content[0].nom").value("Dupont"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));

        verify(destinataireService, times(1))
                .searchByNomOrPrenom(eq("Dupont"), eq("Jean"), any(Pageable.class));
    }

    // ----------------- SEARCH BY TELEPHONE -----------------
    @Test
    @DisplayName("GET /api/destinataires/telephone → doit retourner les résultats par téléphone")
    void testFindByTelephone() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Destinataire> page = new PageImpl<>(List.of(destinataire), pageable, 1);

        when(destinataireService.findByTelephone(eq("0600000001"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/destinataires/telephone?telephone=0600000001&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].telephone").value("0600000001"))
                .andExpect(jsonPath("$.content[0].nom").value("Dupont"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));

        verify(destinataireService, times(1))
                .findByTelephone(eq("0600000001"), any(Pageable.class));
    }

    // ----------------- DELETE -----------------
    @Test
    @DisplayName("DELETE /api/destinataires/{id} → doit supprimer un destinataire existant")
    void testDeleteDestinataire() throws Exception {
        when(destinataireService.existsById("1")).thenReturn(true);
        doNothing().when(destinataireService).deleteById("1");

        mockMvc.perform(delete("/api/destinataires/1"))
                .andExpect(status().isNoContent());

        verify(destinataireService, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("DELETE /api/destinataires/{id} → destinataire non trouvé renvoie 404")
    void testDeleteDestinataireNotFound() throws Exception {
        when(destinataireService.existsById("999")).thenReturn(false);

        mockMvc.perform(delete("/api/destinataires/999"))
                .andExpect(status().isNotFound());

        verify(destinataireService, times(1)).existsById("999");
    }
}
