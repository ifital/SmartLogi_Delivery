package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entity.*;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.PrioriteColis;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ColisProduitRepositoryTest {

    @Autowired
    private ColisProduitRepository colisProduitRepository;

    @Autowired
    private ColisRepository colisRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private ClientExpediteurRepository clientExpediteurRepository;

    @Autowired
    private DestinataireRepository destinataireRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    private Colis colis;
    private Produit produit;

    @BeforeEach
    void setup() {
        // Création d’un client expéditeur complet
        ClientExpediteur client = new ClientExpediteur();
        client.setNom("Client Test");
        client.setPrenom("Prenom Test");
        client.setEmail("client@test.com");
        client.setAdresse("Adresse Test");
        client.setTelephone("0600000000");
        client = clientExpediteurRepository.save(client);

        // Création d’un destinataire complet
        Destinataire destinataire = new Destinataire();
        destinataire.setNom("Destinataire Test");
        destinataire.setPrenom("Prenom Dest");
        destinataire.setEmail("dest@test.com");
        destinataire.setAdresse("Adresse Dest");
        destinataire.setTelephone("0611111111");
        destinataire = destinataireRepository.save(destinataire);

        // Création d’une zone complète
        Zone zone = new Zone();
        zone.setNom("Zone Test");
        zone.setCodePostal("20000"); // obligatoire
        zone = zoneRepository.save(zone);

        // Création d’un colis avec toutes les relations non-null
        colis = new Colis();
        colis.setDescription("Colis Test");
        colis.setPoids(BigDecimal.valueOf(5.5));
        colis.setStatut(StatutColis.CREE);
        colis.setPriorite(PrioriteColis.URGENTE);
        colis.setVilleDestination("Casablanca");
        colis.setClientExpediteur(client);
        colis.setDestinataire(destinataire);
        colis.setZone(zone);
        colis = colisRepository.save(colis);

        // Création d’un produit
        produit = new Produit();
        produit.setNom("Produit Test");
        produit.setCategorie("Catégorie Test");
        produit.setPoids(BigDecimal.valueOf(2.2));
        produit.setPrix(BigDecimal.valueOf(10.5));
        produit = produitRepository.save(produit);

        // Création d’un ColisProduit
        ColisProduit colisProduit = new ColisProduit();
        colisProduit.setId(new ColisProduitId(colis.getId(), produit.getId()));
        colisProduit.setColis(colis);
        colisProduit.setProduit(produit);
        colisProduit.setQuantite(3);
        colisProduit.setPrix(BigDecimal.valueOf(31.5));

        colisProduitRepository.save(colisProduit);
    }

    @Test
    void testFindByColisId() {
        Page<ColisProduit> page = colisProduitRepository.findByColisId(colis.getId(), PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getProduit().getNom()).isEqualTo("Produit Test");
    }

    @Test
    void testFindByProduitId() {
        Page<ColisProduit> page = colisProduitRepository.findByProduitId(produit.getId(), PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getColis().getDescription()).isEqualTo("Colis Test");
    }

    @Test
    void testDeleteByColisId() {
        colisProduitRepository.deleteByColisId(colis.getId());
        Page<ColisProduit> page = colisProduitRepository.findByColisId(colis.getId(), PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(0);
    }
}
