package com.smart_delivery_management.smartlogi_delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart_delivery_management.smartlogi_delivery.dto.ClientExpediteurDTO;
import com.smart_delivery_management.smartlogi_delivery.service.ClientExpediteurService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientExpediteurController.class)
class ClientExpediteurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientExpediteurService service;

    @Autowired
    private ObjectMapper objectMapper;

    private final ClientExpediteurDTO dto =
            new ClientExpediteurDTO("1", "Latifi", "Ali", "ali@test.com", "0600000000", "Casa");

    // ---------- CREATE ----------
    @Test
    @DisplayName("POST /api/clients → doit créer un client")
    void testCreateClient() throws Exception {
        when(service.create(any(ClientExpediteurDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ali@test.com"))
                .andExpect(jsonPath("$.nom").value("Latifi"));
    }

    // ---------- GET BY ID ----------
    @Test
    @DisplayName("GET /api/clients/{id} → doit retourner un client")
    void testGetClientById() throws Exception {
        when(service.getById("1")).thenReturn(dto);

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("ali@test.com"));
    }

    // ---------- GET ALL ----------
    @Test
    @DisplayName("GET /api/clients → doit retourner la liste paginée des clients")
    void testGetAllClients() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(service.getAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(dto), pageable, 1));

        mockMvc.perform(get("/api/clients?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("ali@test.com"))
                .andExpect(jsonPath("$.content[0].nom").value("Latifi"));
    }

    // ---------- UPDATE ----------
    @Test
    @DisplayName("PUT /api/clients/{id} → doit mettre à jour un client")
    void testUpdateClient() throws Exception {
        when(service.update(eq("1"), any(ClientExpediteurDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ali@test.com"))
                .andExpect(jsonPath("$.nom").value("Latifi"));
    }

    // ---------- DELETE ----------
    @Test
    @DisplayName("DELETE /api/clients/{id} → doit supprimer un client")
    void testDeleteClient() throws Exception {
        doNothing().when(service).delete("1");

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete("1");
    }

    // ---------- SEARCH ----------
    @Test
    @DisplayName("GET /api/clients/search → doit retourner les résultats de recherche")
    void testSearchClients() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(service.search(eq("Ali"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(dto), pageable, 1));

        mockMvc.perform(get("/api/clients/search?keyword=Ali&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].prenom").value("Ali"))
                .andExpect(jsonPath("$.content[0].email").value("ali@test.com"));
    }
}
