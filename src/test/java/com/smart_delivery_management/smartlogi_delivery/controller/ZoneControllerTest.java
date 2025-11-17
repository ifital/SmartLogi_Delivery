package com.smart_delivery_management.smartlogi_delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart_delivery_management.smartlogi_delivery.entity.Zone;
import com.smart_delivery_management.smartlogi_delivery.service.ZoneService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(ZoneController.class)
class ZoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ZoneService zoneService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Zone zone = new Zone("1", "Casablanca", "20000");

    // ---------- CREATE ----------
    @Test
    @DisplayName("POST /api/zones → doit créer une zone")
    void testCreateZone() throws Exception {
        when(zoneService.save(any(Zone.class))).thenReturn(zone);

        mockMvc.perform(post("/api/zones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zone)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Casablanca"))
                .andExpect(jsonPath("$.codePostal").value("20000"));
    }

    // ---------- GET BY ID ----------
    @Test
    @DisplayName("GET /api/zones/{id} → doit retourner une zone")
    void testGetZoneById() throws Exception {
        when(zoneService.findById("1")).thenReturn(Optional.of(zone));

        mockMvc.perform(get("/api/zones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nom").value("Casablanca"));
    }

    // ---------- GET ALL ----------
    @Test
    @DisplayName("GET /api/zones → doit retourner la liste paginée des zones")
    void testGetAllZones() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(zoneService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(zone), pageable, 1));

        mockMvc.perform(get("/api/zones?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nom").value("Casablanca"));
    }

    // ---------- SEARCH BY NOM ----------
    @Test
    @DisplayName("GET /api/zones/search → doit retourner les résultats de recherche par nom")
    void testSearchByNom() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(zoneService.searchByNom(eq("Casa"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(zone), pageable, 1));

        mockMvc.perform(get("/api/zones/search?nom=Casa&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nom").value("Casablanca"));
    }

    // ---------- SEARCH BY CODE POSTAL ----------
    @Test
    @DisplayName("GET /api/zones/code-postal → doit retourner les résultats de recherche par code postal")
    void testSearchByCodePostal() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(zoneService.findByCodePostal(eq("20000"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(zone), pageable, 1));

        mockMvc.perform(get("/api/zones/code-postal?codePostal=20000&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].codePostal").value("20000"));
    }

    // ---------- DELETE ----------
    @Test
    @DisplayName("DELETE /api/zones/{id} → doit supprimer une zone")
    void testDeleteZone() throws Exception {
        when(zoneService.existsById("1")).thenReturn(true);
        doNothing().when(zoneService).deleteById("1");

        mockMvc.perform(delete("/api/zones/1"))
                .andExpect(status().isNoContent());

        verify(zoneService, times(1)).deleteById("1");
    }

    // ---------- DELETE NON EXISTANTE ----------
    @Test
    @DisplayName("DELETE /api/zones/{id} → zone inexistante renvoie 404")
    void testDeleteZoneNotFound() throws Exception {
        when(zoneService.existsById("999")).thenReturn(false);

        mockMvc.perform(delete("/api/zones/999"))
                .andExpect(status().isNotFound());
    }
}
