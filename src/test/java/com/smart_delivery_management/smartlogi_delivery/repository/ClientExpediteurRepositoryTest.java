package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entity.ClientExpediteur;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClientExpediteurRepositoryTest {

    @Autowired
    private ClientExpediteurRepository repository;

    @Test
    @DisplayName("Devrait sauvegarder et retrouver un client par email")
    void testFindByEmail() {
        ClientExpediteur client = new ClientExpediteur(
                null, "Latifi", "Abdelali", "ali@mail.com", "0600000000", "Casablanca"
        );
        repository.save(client);

        Optional<ClientExpediteur> found = repository.findByEmail("ali@mail.com");

        assertThat(found).isPresent();
        assertThat(found.get().getNom()).isEqualTo("Latifi");
        assertThat(found.get().getPrenom()).isEqualTo("Abdelali");
    }

    @Test
    @DisplayName("Devrait trouver un client par nom ou prénom (ignore case)")
    void testFindByNomOrPrenom() {
        ClientExpediteur client = new ClientExpediteur(
                null, "Latifi", "Ali", "ali@test.com", "0600000000", "Casa"
        );
        repository.save(client);

        var result = repository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
                "ali", "ali", PageRequest.of(0, 10)
        );

        assertThat(result.getTotalElements()).isGreaterThan(0);
        assertThat(result.getContent().get(0).getPrenom()).isEqualTo("Ali");
    }

    @Test
    @DisplayName("Devrait trouver un client par téléphone")
    void testFindByTelephone() {
        ClientExpediteur client = new ClientExpediteur(
                null, "Latifi", "Youssef", "youssef@test.com", "0611223344", "Rabat"
        );
        repository.save(client);

        var result = repository.findByTelephone("0611223344", PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("youssef@test.com");
    }
}
