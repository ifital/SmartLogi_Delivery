package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.dto.*;
import com.smart_delivery_management.smartlogi_delivery.entities.*;
import com.smart_delivery_management.smartlogi_delivery.entities.enums.PrioriteColis;
import com.smart_delivery_management.smartlogi_delivery.entities.enums.StatutColis;
import com.smart_delivery_management.smartlogi_delivery.exception.ResourceNotFoundException;
import com.smart_delivery_management.smartlogi_delivery.mapper.ColisMapper;
import com.smart_delivery_management.smartlogi_delivery.repository.*;
import com.smart_delivery_management.smartlogi_delivery.service.ColisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ColisServiceImpl implements ColisService {

    private final ColisRepository colisRepository;
    private final ClientExpediteurRepository clientRepository;
    private final DestinataireRepository destinataireRepository;
    private final ZoneRepository zoneRepository;
    private final LivreurRepository livreurRepository;
    private final HistoriqueLivraisonRepository historiqueRepository;
    private final ColisMapper colisMapper;

    @Override
    public ColisDTO createColis(ColisCreateDTO createDTO) {
        log.info("Création d'un nouveau colis pour le client {}", createDTO.getClientExpediteurId());
        ClientExpediteur client = clientRepository.findById(createDTO.getClientExpediteurId())
                .orElseThrow(() -> new ResourceNotFoundException("Client expéditeur introuvable"));
        Destinataire destinataire = destinataireRepository.findById(createDTO.getDestinataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Destinataire introuvable"));
        Zone zone = zoneRepository.findById(createDTO.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable"));

        Colis colis = colisMapper.toEntityFromCreate(createDTO);
        colis.setClientExpediteur(client);
        colis.setDestinataire(destinataire);
        colis.setZone(zone);

        Colis savedColis = colisRepository.save(colis);
        addHistorique(savedColis, StatutColis.CREE, "Colis créé");

        log.info("Colis créé avec succès, ID: {}", savedColis.getId());
        return colisMapper.toDto(savedColis);
    }

    @Override
    @Transactional(readOnly = true)
    public ColisDTO getColisById(String id) {
        Colis colis = colisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colis introuvable avec l'ID: " + id));
        return colisMapper.toDto(colis);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ColisDTO> getAllColis(Pageable pageable) {
        return colisRepository.findAll(pageable).map(colisMapper::toDto);
    }

    @Override
    public ColisDTO updateColis(String id, ColisDTO colisDTO) {
        Colis existingColis = colisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colis introuvable"));

        existingColis.setDescription(colisDTO.getDescription());
        existingColis.setPoids(colisDTO.getPoids());
        existingColis.setPriorite(colisDTO.getPriorite());
        existingColis.setVilleDestination(colisDTO.getVilleDestination());

        return colisMapper.toDto(colisRepository.save(existingColis));
    }

    @Override
    public void deleteColis(String id) {
        if (!colisRepository.existsById(id)) throw new ResourceNotFoundException("Colis introuvable");
        colisRepository.deleteById(id);
    }

    @Override
    public ColisDTO assignerLivreur(String colisId, String livreurId) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new ResourceNotFoundException("Colis introuvable"));
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new ResourceNotFoundException("Livreur introuvable"));

        colis.setLivreur(livreur);
        Colis savedColis = colisRepository.save(colis);
        addHistorique(savedColis, colis.getStatut(),
                "Colis assigné au livreur: " + livreur.getNom() + " " + livreur.getPrenom());

        return colisMapper.toDto(savedColis);
    }

    @Override
    public ColisDTO updateStatut(String colisId, StatutColis nouveauStatut, String commentaire) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new ResourceNotFoundException("Colis introuvable"));

        colis.setStatut(nouveauStatut);
        Colis savedColis = colisRepository.save(colis);
        addHistorique(savedColis, nouveauStatut, commentaire);

        return colisMapper.toDto(savedColis);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ColisDTO> searchColis(ColisSearchCriteria criteria, Pageable pageable) {
        Specification<Colis> spec = Specification.where(null);

        if (criteria.getStatut() != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("statut"), criteria.getStatut()));
        if (criteria.getPriorite() != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("priorite"), criteria.getPriorite()));
        if (criteria.getZoneId() != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zone").get("id"), criteria.getZoneId()));
        if (criteria.getVille() != null)
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("villeDestination")),
                    "%" + criteria.getVille().toLowerCase() + "%"));
        if (criteria.getLivreurId() != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("livreur").get("id"), criteria.getLivreurId()));
        if (criteria.getClientExpediteurId() != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("clientExpediteur").get("id"), criteria.getClientExpediteurId()));
        if (criteria.getDateDebut() != null)
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dateCreation"), criteria.getDateDebut()));
        if (criteria.getDateFin() != null)
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("dateCreation"), criteria.getDateFin()));

        return colisRepository.findAll(spec, pageable).map(colisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColisDTO> getColisByLivreur(String livreurId) {
        return colisRepository.findByLivreurId(livreurId).stream().map(colisMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ColisDTO> getColisByClient(String clientId, Pageable pageable) {
        return colisRepository.findByClientExpediteurId(clientId, pageable).map(colisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColisDTO> getColisByDestinataire(String destinataireId) {
        return colisRepository.findByDestinataireId(destinataireId).stream().map(colisMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueLivraisonDTO> getHistorique(String colisId, Pageable pageable) {
        if (!colisRepository.existsById(colisId)) {
            throw new ResourceNotFoundException("Colis introuvable");
        }

        return historiqueRepository
                .findByColisIdOrderByDateChangementDesc(colisId, pageable)
                .map(h -> new HistoriqueLivraisonDTO(
                        h.getId(),
                        h.getStatut(),
                        h.getDateChangement(),
                        h.getCommentaire()
                ));
    }


    @Override
    @Transactional(readOnly = true)
    public ColisStatisticsDTO getStatisticsByLivreur(String livreurId) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new ResourceNotFoundException("Livreur introuvable"));

        Long count = colisRepository.countByLivreurId(livreurId);
        BigDecimal poids = colisRepository.sumPoidsByLivreurId(livreurId);

        return new ColisStatisticsDTO(
                livreur.getId(),
                livreur.getNom() + " " + livreur.getPrenom(),
                null, null,
                count != null ? count : 0L,
                poids != null ? poids : BigDecimal.ZERO
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ColisStatisticsDTO getStatisticsByZone(String zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable"));

        Long count = colisRepository.countByZoneId(zoneId);
        BigDecimal poids = colisRepository.sumPoidsByZoneId(zoneId);

        return new ColisStatisticsDTO(
                null, null,
                zone.getId(), zone.getNom(),
                count != null ? count : 0L,
                poids != null ? poids : BigDecimal.ZERO
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColisDTO> getColisEnRetard() {
        LocalDateTime limitDate = LocalDateTime.now().minusDays(3);
        return colisRepository.findColisEnRetard(limitDate).stream().map(colisMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColisDTO> getColisPrioritairesNonAssignes() {
        return colisRepository.findColisPrioritairesNonAssignes().stream().map(colisMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ColisDTO> getColisByStatut(StatutColis statut, Pageable pageable) {
        return colisRepository.findByStatut(statut, pageable).map(colisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ColisDTO> getColisByPriorite(PrioriteColis priorite, Pageable pageable) {
        return colisRepository.findByPriorite(priorite, pageable).map(colisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ColisDTO> getColisByZone(String zoneId, Pageable pageable) {
        return colisRepository.findByZoneId(zoneId, pageable).map(colisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ColisDTO> getColisByVille(String ville, Pageable pageable) {
        return colisRepository.findByVilleDestinationContainingIgnoreCase(ville, pageable).map(colisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ColisDTO> getColisByLivreur(String livreurId, Pageable pageable) {
        return colisRepository.findByLivreurId(livreurId, pageable).map(colisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> countByStatutGroupBy() {
        return colisRepository.countByStatutGroupBy();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> countByZoneGroupBy() {
        return colisRepository.countByZoneGroupBy();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> countByPrioriteGroupBy() {
        return colisRepository.countByPrioriteGroupBy();
    }

    private void addHistorique(Colis colis, StatutColis statut, String commentaire) {
        HistoriqueLivraison historique = new HistoriqueLivraison();
        historique.setColis(colis);
        historique.setStatut(statut);
        historique.setCommentaire(commentaire);
        historiqueRepository.save(historique);
    }
}
