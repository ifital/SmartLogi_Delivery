package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entity.Destinataire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DestinataireRepositoryTest {

    @Autowired
    private DestinataireRepository destinataireRepository;

    private Destinataire destinataire1;
    private Destinataire destinataire2;
    private Destinataire destinataire3;

    @BeforeEach
    void setUp() {
        destinataireRepository.deleteAll();

        destinataire1 = new Destinataire(null, "Dupont", "Jean", "jean.dupont@example.com", "0600000001", "Rue de Paris");
        destinataire2 = new Destinataire(null, "Durand", "Sophie", "sophie.durand@example.com", "0600000002", "Rue de Lyon");
        destinataire3 = new Destinataire(null, "Dupuis", "Paul", "paul.dupuis@example.com", "0600000003", "Rue de Marseille");

        destinataireRepository.save(destinataire1);
        destinataireRepository.save(destinataire2);
        destinataireRepository.save(destinataire3);
    }

    @Test
    @DisplayName("Doit trouver les destinataires par nom ou prénom (insensible à la casse)")
    void testFindByNomOrPrenomContainingIgnoreCase() {
        Page<Destinataire> results = destinataireRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
                "dup", "dup", PageRequest.of(0, 10)
        );

        assertThat(results).isNotNull();
        assertThat(results.getContent()).hasSize(2);
        assertThat(results.getContent()).extracting("nom").contains("Dupont", "Dupuis");
    }

    @Test
    @DisplayName("Doit trouver les destinataires par téléphone exact")
    void testFindByTelephone() {
        Page<Destinataire> results = destinataireRepository.findByTelephone("0600000002", PageRequest.of(0, 10));

        assertThat(results).isNotNull();
        assertThat(results.getContent()).hasSize(1);
        assertThat(results.getContent().get(0).getPrenom()).isEqualTo("Sophie");
    }

    @Test
    @DisplayName(" Doit retourner une page vide si aucun résultat trouvé")
    void testFindByNomOrPrenomEmptyResult() {
        Page<Destinataire> results = destinataireRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
                "Inexistant", "Inexistant", PageRequest.of(0, 10)
        );

        assertThat(results).isNotNull();
        assertThat(results.getContent()).isEmpty();
    }
}
