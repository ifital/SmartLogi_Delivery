package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.entity.Destinataire;
import com.smart_delivery_management.smartlogi_delivery.repository.DestinataireRepository;
import com.smart_delivery_management.smartlogi_delivery.service.impl.DestinataireServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DestinataireServiceTest {

    @Mock
    private DestinataireRepository destinataireRepository;

    @InjectMocks
    private DestinataireServiceImpl destinataireService;

    private Destinataire destinataire1;
    private Destinataire destinataire2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        destinataire1 = new Destinataire("1", "Dupont", "Jean", "jean@example.com", "0600000001", "Rue de Paris");
        destinataire2 = new Destinataire("2", "Durand", "Sophie", "sophie@example.com", "0600000002", "Rue de Lyon");
    }

    // -------------- TEST: Enregistrement ---------------
    @Test
    @DisplayName(" Doit enregistrer un destinataire avec succès")
    void testSaveDestinataire() {
        when(destinataireRepository.save(destinataire1)).thenReturn(destinataire1);

        Destinataire saved = destinataireService.save(destinataire1);

        assertThat(saved).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Dupont");
        verify(destinataireRepository, times(1)).save(destinataire1);
    }

    // ------------- TEST: Recherche par ID ---------------
    @Test
    @DisplayName(" Doit retourner un destinataire existant par ID")
    void testFindById() {
        when(destinataireRepository.findById("1")).thenReturn(Optional.of(destinataire1));

        Optional<Destinataire> result = destinataireService.findById("1");

        assertThat(result).isPresent();
        assertThat(result.get().getPrenom()).isEqualTo("Jean");
        verify(destinataireRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName(" Doit retourner vide si aucun destinataire trouvé")
    void testFindByIdNotFound() {
        when(destinataireRepository.findById("99")).thenReturn(Optional.empty());

        Optional<Destinataire> result = destinataireService.findById("99");

        assertThat(result).isEmpty();
        verify(destinataireRepository, times(1)).findById("99");
    }

    // --------------- TEST: Récupération paginée -----------------
    @Test
    @DisplayName(" Doit retourner tous les destinataires paginés")
    void testFindAll() {
        Page<Destinataire> page = new PageImpl<>(List.of(destinataire1, destinataire2));
        Pageable pageable = PageRequest.of(0, 10);

        when(destinataireRepository.findAll(pageable)).thenReturn(page);

        Page<Destinataire> result = destinataireService.findAll(pageable);

        assertThat(result.getContent()).hasSize(2);
        verify(destinataireRepository, times(1)).findAll(pageable);
    }

    // ------------- TEST: Recherche nom/prénom --------------
    @Test
    @DisplayName(" Doit rechercher par nom ou prénom avec pagination")
    void testSearchByNomOrPrenom() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Destinataire> page = new PageImpl<>(List.of(destinataire1));

        when(destinataireRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase("Dup", "Dup", pageable))
                .thenReturn(page);

        Page<Destinataire> result = destinataireService.searchByNomOrPrenom("Dup", "Dup", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getNom()).isEqualTo("Dupont");
        verify(destinataireRepository, times(1))
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase("Dup", "Dup", pageable);
    }

    // ----------- TEST: Recherche par téléphone -----------------
    @Test
    @DisplayName("Doit rechercher un destinataire par téléphone")
    void testFindByTelephone() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Destinataire> page = new PageImpl<>(List.of(destinataire2));

        when(destinataireRepository.findByTelephone("0600000002", pageable)).thenReturn(page);

        Page<Destinataire> result = destinataireService.findByTelephone("0600000002", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPrenom()).isEqualTo("Sophie");
        verify(destinataireRepository, times(1)).findByTelephone("0600000002", pageable);
    }

    // ------------- TEST: Suppression -----------------
    @Test
    @DisplayName(" Doit supprimer un destinataire par ID")
    void testDeleteById() {
        doNothing().when(destinataireRepository).deleteById("1");

        destinataireService.deleteById("1");

        verify(destinataireRepository, times(1)).deleteById("1");
    }

    // ----------------- TEST: Vérification existence --------------------
    @Test
    @DisplayName("✔️ Doit vérifier si un destinataire existe par ID")
    void testExistsById() {
        when(destinataireRepository.existsById("1")).thenReturn(true);

        boolean exists = destinataireService.existsById("1");

        assertThat(exists).isTrue();
        verify(destinataireRepository, times(1)).existsById("1");
    }
}
