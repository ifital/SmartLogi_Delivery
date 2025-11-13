package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entity.Livreur;
import com.smart_delivery_management.smartlogi_delivery.entity.Zone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LivreurRepositoryTest {

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private ZoneRepository zoneRepository; // On suppose que tu as un ZoneRepository

    @Test
    @DisplayName("save → doit persister un livreur")
    void testSaveLivreur() {
        Zone zone = zoneRepository.save(new Zone("1", "Casablanca", "20000"));
        Livreur livreur = new Livreur(null, "Dupont", "Jean", "0600000001", "Moto", zone);

        Livreur saved = livreurRepository.save(livreur);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Dupont");
        assertThat(saved.getZoneAssignee()).isNotNull();
        assertThat(saved.getZoneAssignee().getNom()).isEqualTo("Casablanca");
    }

    @Test
    @DisplayName("findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase → doit retourner le livreur")
    void testFindByNomOrPrenom() {
        Zone zone = zoneRepository.save(new Zone("1", "Casablanca", "20000"));
        Livreur livreur = new Livreur(null, "Dupont", "Jean", "0600000001", "Moto", zone);
        livreurRepository.save(livreur);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = livreurRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase("dupont", "jean", pageable);

        assertThat(page).isNotEmpty();
        assertThat(page.getContent().get(0).getNom()).isEqualTo("Dupont");
    }

    @Test
    @DisplayName("findByZoneAssigneeId → doit retourner les livreurs d'une zone")
    void testFindByZoneAssigneeId() {
        Zone zone = zoneRepository.save(new Zone("1", "Casablanca", "20000"));
        Livreur livreur = new Livreur(null, "Dupont", "Jean", "0600000001", "Moto", zone);
        livreurRepository.save(livreur);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = livreurRepository.findByZoneAssigneeId(zone.getId(), pageable);

        assertThat(page).isNotEmpty();
        assertThat(page.getContent().get(0).getZoneAssignee().getId()).isEqualTo(zone.getId());
    }

    @Test
    @DisplayName("findLivreursByZone → doit retourner les livreurs par query custom")
    void testFindLivreursByZone() {
        Zone zone = zoneRepository.save(new Zone("1", "Casablanca", "20000"));
        Livreur livreur = new Livreur(null, "Dupont", "Jean", "0600000001", "Moto", zone);
        livreurRepository.save(livreur);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = livreurRepository.findLivreursByZone(zone.getId(), pageable);

        assertThat(page).isNotEmpty();
        assertThat(page.getContent().get(0).getZoneAssignee().getNom()).isEqualTo("Casablanca");
    }

    @Test
    @DisplayName("delete → doit supprimer un livreur")
    void testDeleteLivreur() {
        Zone zone = zoneRepository.save(new Zone("1", "Casablanca", "20000"));
        Livreur livreur = new Livreur(null, "Dupont", "Jean", "0600000001", "Moto", zone);
        Livreur saved = livreurRepository.save(livreur);

        livreurRepository.deleteById(saved.getId());
        assertThat(livreurRepository.findById(saved.getId())).isEmpty();
    }
}
