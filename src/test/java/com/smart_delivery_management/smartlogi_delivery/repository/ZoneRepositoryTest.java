package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entity.Zone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ZoneRepositoryTest {

    @Autowired
    private ZoneRepository zoneRepository;

    // ---------- CREATE ----------
    @Test
    @DisplayName("Devrait sauvegarder une zone")
    void testSaveZone() {
        Zone zone = new Zone(null, "Casablanca", "20000");
        Zone saved = zoneRepository.save(zone);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Casablanca");
        assertThat(saved.getCodePostal()).isEqualTo("20000");
    }

    // ---------- READ ----------
    @Test
    @DisplayName("Devrait récupérer une zone par ID")
    void testFindById() {
        Zone zone = new Zone(null, "Rabat", "10000");
        Zone saved = zoneRepository.save(zone);

        Optional<Zone> found = zoneRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getNom()).isEqualTo("Rabat");
    }

    @Test
    @DisplayName("Devrait retourner toutes les zones")
    void testFindAll() {
        zoneRepository.save(new Zone(null, "Fes", "30000"));
        zoneRepository.save(new Zone(null, "Marrakech", "40000"));

        List<Zone> zones = zoneRepository.findAll();
        assertThat(zones).hasSizeGreaterThanOrEqualTo(2);
    }

    // ---------- UPDATE ----------
    @Test
    @DisplayName("Devrait mettre à jour une zone")
    void testUpdateZone() {
        Zone zone = new Zone(null, "Tangier", "90000");
        Zone saved = zoneRepository.save(zone);

        saved.setNom("Tanger");
        saved.setCodePostal("90001");
        Zone updated = zoneRepository.save(saved);

        assertThat(updated.getNom()).isEqualTo("Tanger");
        assertThat(updated.getCodePostal()).isEqualTo("90001");
    }

    // ---------- DELETE ----------
    @Test
    @DisplayName("Devrait supprimer une zone")
    void testDeleteZone() {
        Zone zone = new Zone(null, "Agadir", "80000");
        Zone saved = zoneRepository.save(zone);

        zoneRepository.deleteById(saved.getId());
        Optional<Zone> found = zoneRepository.findById(saved.getId());
        assertThat(found).isNotPresent();
    }

    // ---------- SEARCH ----------
    @Test
    @DisplayName("Devrait trouver les zones par nom (contient, insensible à la casse)")
    void testFindByNomContainingIgnoreCase() {
        zoneRepository.save(new Zone(null, "Casablanca", "20000"));
        zoneRepository.save(new Zone(null, "Casa Blanca", "21000"));

        Page<Zone> page = zoneRepository.findByNomContainingIgnoreCase("casa", PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("Devrait trouver les zones par code postal exact")
    void testFindByCodePostal() {
        zoneRepository.save(new Zone(null, "Rabat", "10000"));

        Page<Zone> page = zoneRepository.findByCodePostal("10000", PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getNom()).isEqualTo("Rabat");
    }
}
