package com.smart_delivery_management.smartlogi_delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart_delivery_management.smartlogi_delivery.entity.Produit;
import com.smart_delivery_management.smartlogi_delivery.service.ProduitService;
import org.junit.jupiter.api.BeforeEach;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProduitController.class)
class ProduitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProduitService produitService;

    @Autowired
    private ObjectMapper objectMapper;

    private Produit produit1;
    private Produit produit2;

    @BeforeEach
    void setUp() {
        produit1 = new Produit("1", "Laptop Dell", "Electronique", new BigDecimal("2.5"), new BigDecimal("1200.00"));
        produit2 = new Produit("2", "Laptop HP", "Electronique", new BigDecimal("2.3"), new BigDecimal("1100.00"));
    }

    // ------------------- CREATE -------------------
    @Test
    @DisplayName("POST /api/produits → doit créer un produit")
    void testCreateProduit() throws Exception {
        when(produitService.save(any(Produit.class))).thenReturn(produit1);

        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produit1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Laptop Dell"))
                .andExpect(jsonPath("$.categorie").value("Electronique"));

        verify(produitService, times(1)).save(any(Produit.class));
    }

    // ------------------- GET BY ID -------------------
    @Test
    @DisplayName("GET /api/produits/{id} → doit retourner un produit")
    void testGetProduitById() throws Exception {
        when(produitService.findById("1")).thenReturn(Optional.of(produit1));

        mockMvc.perform(get("/api/produits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nom").value("Laptop Dell"));

        verify(produitService, times(1)).findById("1");
    }

    @Test
    @DisplayName("GET /api/produits/{id} → produit non trouvé renvoie 404")
    void testGetProduitByIdNotFound() throws Exception {
        when(produitService.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/produits/999"))
                .andExpect(status().isNotFound());

        verify(produitService, times(1)).findById("999");
    }

    // ------------------- GET ALL (PAGINATION) -------------------
    @Test
    @DisplayName("GET /api/produits → doit retourner une page de produits")
    void testGetAllProduits() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> page = new PageImpl<>(List.of(produit1, produit2), pageable, 2);
        when(produitService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/produits?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nom").value("Laptop Dell"))
                .andExpect(jsonPath("$.content[1].nom").value("Laptop HP"))
                .andExpect(jsonPath("$.totalElements").value(2));

        verify(produitService, times(1)).findAll(any(Pageable.class));
    }

    // ------------------- SEARCH BY NOM -------------------
    @Test
    @DisplayName("GET /api/produits/search → doit retourner les produits par nom")
    void testSearchByNom() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> page = new PageImpl<>(List.of(produit1), pageable, 1);

        when(produitService.searchByNom(eq("Dell"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/produits/search")
                        .param("nom", "Dell")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nom").value("Laptop Dell"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(produitService, times(1)).searchByNom(eq("Dell"), any(Pageable.class));
    }

    // ------------------- SEARCH BY CATEGORIE -------------------
    @Test
    @DisplayName("GET /api/produits/categorie → doit retourner les produits par catégorie")
    void testSearchByCategorie() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> page = new PageImpl<>(List.of(produit1, produit2), pageable, 2);

        when(produitService.findByCategorie(eq("Electronique"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/produits/categorie")
                        .param("categorie", "Electronique")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].categorie").value("Electronique"))
                .andExpect(jsonPath("$.content[1].categorie").value("Electronique"))
                .andExpect(jsonPath("$.totalElements").value(2));

        verify(produitService, times(1)).findByCategorie(eq("Electronique"), any(Pageable.class));
    }
    // ------------------- DELETE -------------------
    @Test
    @DisplayName("DELETE /api/produits/{id} → doit supprimer un produit existant")
    void testDeleteProduit() throws Exception {
        when(produitService.existsById("1")).thenReturn(true);
        doNothing().when(produitService).deleteById("1");

        mockMvc.perform(delete("/api/produits/1"))
                .andExpect(status().isNoContent());

        verify(produitService, times(1)).existsById("1");
        verify(produitService, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("DELETE /api/produits/{id} → produit non trouvé renvoie 404")
    void testDeleteProduitNotFound() throws Exception {
        when(produitService.existsById("999")).thenReturn(false);

        mockMvc.perform(delete("/api/produits/999"))
                .andExpect(status().isNotFound());

        verify(produitService, times(1)).existsById("999");
        verify(produitService, never()).deleteById(anyString());
    }
}
