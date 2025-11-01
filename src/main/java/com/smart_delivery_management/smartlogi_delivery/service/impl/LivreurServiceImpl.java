package com.smart_delivery_management.smartlogi_delivery.service.impl;


import com.smart_delivery_management.smartlogi_delivery.dto.LivreurDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.Livreur;
import com.smart_delivery_management.smartlogi_delivery.exception.ResourceNotFoundException;
import com.smart_delivery_management.smartlogi_delivery.mapper.LivreurMapper;
import com.smart_delivery_management.smartlogi_delivery.repository.LivreurRepository;
import com.smart_delivery_management.smartlogi_delivery.service.LivreurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LivreurServiceImpl implements LivreurService {

    private final LivreurRepository repository;
    private final LivreurMapper mapper;

    @Override
    public LivreurDTO create(LivreurDTO dto) {
        log.info("Création d'un nouveau livreur: {} {}", dto.getNom(), dto.getPrenom());
        Livreur entity = mapper.toEntity(dto);
        Livreur saved = repository.save(entity);
        log.info("Livreur créé avec succès, ID: {}", saved.getId());
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LivreurDTO getById(String id) {
        log.debug("Récupération du livreur ID: {}", id);
        Livreur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livreur introuvable"));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LivreurDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public LivreurDTO update(String id, LivreurDTO dto) {
        log.info("Mise à jour du livreur ID: {}", id);
        Livreur existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livreur introuvable"));

        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setTelephone(dto.getTelephone());
        existing.setVehicule(dto.getVehicule());

        return mapper.toDto(repository.save(existing));
    }

    @Override
    public void delete(String id) {
        log.info("Suppression du livreur ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Livreur introuvable");
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreurDTO> search(String keyword) {
        return repository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreurDTO> getByZone(String zoneId) {
        log.debug("Récupération des livreurs de la zone {}", zoneId);
        return repository.findByZoneAssigneeId(zoneId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}

