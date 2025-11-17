package com.smart_delivery_management.smartlogi_delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart_delivery_management.smartlogi_delivery.entity.Livreur;
import com.smart_delivery_management.smartlogi_delivery.entity.Zone;
import com.smart_delivery_management.smartlogi_delivery.service.LivreurService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivreurController.class)
class LivreurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivreurService livreurService;

    @Autowired
    private ObjectMapper objectMapper;

    // Zone complètement initialisée pour éviter LazyInitializationException
    private final Zone zone = new Zone();
    {
        zone.setId("1");
        zone.setNom("Casablanca");
        zone.setCodePostal("20000");
    }

    private final Livreur livreur = new Livreur();
    {
        livreur.setId("1");
        livreur.setNom("Dupont");
        livreur.setPrenom("Jean");
        livreur.setTelephone("0600000001");
        livreur.setVehicule("Moto");
        livreur.setZoneAssignee(zone);
    }

    // ------------------- CREATE -------------------
    @Test
    @DisplayName("POST /api/livreurs → doit créer un livreur")
    void testCreateLivreur() throws Exception {
        when(livreurService.save(any(Livreur.class))).thenReturn(livreur);

        mockMvc.perform(post("/api/livreurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livreur)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Dupont"))
                .andExpect(jsonPath("$.prenom").value("Jean"));

        verify(livreurService, times(1)).save(any(Livreur.class));
    }

    // ------------------- GET BY ID -------------------
    @Test
    @DisplayName("GET /api/livreurs/{id} → doit retourner un livreur")
    void testGetLivreurById() throws Exception {
        when(livreurService.findById("1")).thenReturn(Optional.of(livreur));

        mockMvc.perform(get("/api/livreurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nom").value("Dupont"));

        verify(livreurService, times(1)).findById("1");
    }

    @Test
    @DisplayName("GET /api/livreurs/{id} → livreur non trouvé renvoie 404")
    void testGetLivreurByIdNotFound() throws Exception {
        when(livreurService.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/livreurs/999"))
                .andExpect(status().isNotFound());

        verify(livreurService, times(1)).findById("999");
    }

    // ------------------- GET ALL (PAGINATION) -------------------
    @Test
    @DisplayName("GET /api/livreurs → doit retourner une page de livreurs")
    void testGetAllLivreurs() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = new PageImpl<>(List.of(livreur), pageable, 1);
        when(livreurService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/livreurs?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nom").value("Dupont"))
                .andExpect(jsonPath("$.content[0].prenom").value("Jean"))
                .andExpect(jsonPath("$.content[0].zoneAssignee.nom").value("Casablanca"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));

        verify(livreurService, times(1)).findAll(any(Pageable.class));
    }

    // ------------------- SEARCH BY NOM/PRENOM -------------------
    @Test
    @DisplayName("GET /api/livreurs/search → doit retourner les livreurs par nom/prénom")
    void testSearchByNomOrPrenom() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = new PageImpl<>(List.of(livreur), pageable, 1);
        when(livreurService.searchByNomOrPrenom(eq("Dupont"), eq("Jean"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/livreurs/search?nom=Dupont&prenom=Jean&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nom").value("Dupont"))
                .andExpect(jsonPath("$.content[0].prenom").value("Jean"))
                .andExpect(jsonPath("$.content[0].zoneAssignee.nom").value("Casablanca"));

        verify(livreurService, times(1))
                .searchByNomOrPrenom(eq("Dupont"), eq("Jean"), any(Pageable.class));
    }

    // ------------------- GET BY ZONE -------------------
    @Test
    @DisplayName("GET /api/livreurs/zone/{zoneId} → doit retourner les livreurs d'une zone")
    void testGetLivreursByZone() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = new PageImpl<>(List.of(livreur), pageable, 1);
        when(livreurService.findByZoneAssigneeId(eq("1"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/livreurs/zone/1?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].zoneAssignee.nom").value("Casablanca"));

        verify(livreurService, times(1)).findByZoneAssigneeId(eq("1"), any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/livreurs/zone/custom/{zoneId} → doit retourner les livreurs via requête personnalisée")
    void testGetLivreursByZoneCustom() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = new PageImpl<>(List.of(livreur), pageable, 1);
        when(livreurService.findLivreursByZone(eq("1"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/livreurs/zone/custom/1?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].zoneAssignee.nom").value("Casablanca"));

        verify(livreurService, times(1)).findLivreursByZone(eq("1"), any(Pageable.class));
    }

    // ------------------- DELETE -------------------
    @Test
    @DisplayName("DELETE /api/livreurs/{id} → doit supprimer un livreur existant")
    void testDeleteLivreur() throws Exception {
        when(livreurService.existsById("1")).thenReturn(true);
        doNothing().when(livreurService).deleteById("1");

        mockMvc.perform(delete("/api/livreurs/1"))
                .andExpect(status().isNoContent());

        verify(livreurService, times(1)).existsById("1");
        verify(livreurService, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("DELETE /api/livreurs/{id} → livreur non trouvé renvoie 404")
    void testDeleteLivreurNotFound() throws Exception {
        when(livreurService.existsById("999")).thenReturn(false);

        mockMvc.perform(delete("/api/livreurs/999"))
                .andExpect(status().isNotFound());

        verify(livreurService, times(1)).existsById("999");
        verify(livreurService, never()).deleteById(anyString());
    }
}
