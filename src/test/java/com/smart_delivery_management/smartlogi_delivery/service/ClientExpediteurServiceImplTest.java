package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.dto.ClientExpediteurDTO;
import com.smart_delivery_management.smartlogi_delivery.entity.ClientExpediteur;
import com.smart_delivery_management.smartlogi_delivery.exception.ResourceNotFoundException;
import com.smart_delivery_management.smartlogi_delivery.mapper.ClientExpediteurMapper;
import com.smart_delivery_management.smartlogi_delivery.repository.ClientExpediteurRepository;
import com.smart_delivery_management.smartlogi_delivery.service.impl.ClientExpediteurServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ClientExpediteurServiceImplTest {

    @Mock
    private ClientExpediteurRepository repository;

    @Mock
    private ClientExpediteurMapper mapper;

    @InjectMocks
    private ClientExpediteurServiceImpl service;

    private ClientExpediteur client;
    private ClientExpediteurDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new ClientExpediteur("1", "Latifi", "Ali", "ali@test.com", "0600000000", "Casa");
        dto = new ClientExpediteurDTO("1", "Latifi", "Ali", "ali@test.com", "0600000000", "Casa");
    }

    // ---------- CREATE ----------
    @Test
    @DisplayName("Créer un client avec succès")
    void testCreate() {
        when(mapper.toEntity(dto)).thenReturn(client);
        when(repository.save(client)).thenReturn(client);
        when(mapper.toDto(client)).thenReturn(dto);

        ClientExpediteurDTO result = service.create(dto);

        assertThat(result).isEqualTo(dto);
        verify(repository, times(1)).save(any(ClientExpediteur.class));
    }

    // ---------- GET BY ID ----------
    @Test
    @DisplayName("Récupérer un client existant")
    void testGetById() {
        when(repository.findById("1")).thenReturn(Optional.of(client));
        when(mapper.toDto(client)).thenReturn(dto);

        ClientExpediteurDTO result = service.getById("1");

        assertThat(result.getNom()).isEqualTo("Latifi");
    }

    @Test
    @DisplayName("Lève une exception si le client n'existe pas")
    void testGetByIdNotFound() {
        when(repository.findById("99")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById("99"));
    }

    // ---------- GET ALL ----------
    @Test
    @DisplayName("Retourne tous les clients paginés")
    void testGetAll() {
        when(repository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(client)));
        when(mapper.toDto(client)).thenReturn(dto);

        Page<ClientExpediteurDTO> result = service.getAll(PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(repository, times(1)).findAll(any(PageRequest.class));
    }

    // ---------- UPDATE ----------
    @Test
    @DisplayName("Met à jour un client existant avec succès")
    void testUpdate() {
        when(repository.findById("1")).thenReturn(Optional.of(client));
        when(repository.save(any(ClientExpediteur.class))).thenReturn(client);
        when(mapper.toDto(client)).thenReturn(dto);

        ClientExpediteurDTO result = service.update("1", dto);

        assertThat(result.getNom()).isEqualTo("Latifi");
        verify(repository, times(1)).save(any(ClientExpediteur.class));
    }

    @Test
    @DisplayName("Lève une exception lors de la mise à jour d'un client inexistant")
    void testUpdateNotFound() {
        when(repository.findById("99")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update("99", dto));
    }

    // ---------- DELETE ----------
    @Test
    @DisplayName("Supprime un client avec succès")
    void testDelete() {
        when(repository.existsById("1")).thenReturn(true);
        doNothing().when(repository).deleteById("1");

        service.delete("1");

        verify(repository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Lève une exception si on tente de supprimer un client inexistant")
    void testDeleteNotFound() {
        when(repository.existsById("99")).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.delete("99"));
    }

    // ---------- SEARCH ----------
    @Test
    @DisplayName("Recherche par nom ou prénom")
    void testSearchByNomOrPrenom() {
        when(repository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(client)));
        when(mapper.toDto(client)).thenReturn(dto);

        var result = service.search("Ali", PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(repository, times(1))
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(eq("Ali"), eq("Ali"), any(PageRequest.class));
    }

    @Test
    @DisplayName("Recherche par téléphone si aucun résultat par nom/prénom")
    void testSearchByTelephone() {
        when(repository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(Page.empty());
        when(repository.findByTelephone(anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(client)));
        when(mapper.toDto(client)).thenReturn(dto);

        var result = service.search("0600000000", PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Recherche par email si aucun résultat ailleurs")
    void testSearchByEmail() {
        when(repository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(Page.empty());
        when(repository.findByTelephone(anyString(), any(PageRequest.class)))
                .thenReturn(Page.empty());
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(client));

        service.search("ali@test.com", PageRequest.of(0, 10));

        verify(repository, times(1)).findByEmail("ali@test.com");
    }
}
