package com.smart_delivery_management.smartlogi_delivery.service.impl;


import com.smart_delivery_management.smartlogi_delivery.dto.ZoneDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.Zone;
import com.smart_delivery_management.smartlogi_delivery.exception.ResourceNotFoundException;
import com.smart_delivery_management.smartlogi_delivery.mapper.ZoneMapper;
import com.smart_delivery_management.smartlogi_delivery.repository.ZoneRepository;
import com.smart_delivery_management.smartlogi_delivery.service.ZoneService;
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
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository repository;
    private final ZoneMapper mapper;

    @Override
    public ZoneDTO create(ZoneDTO dto) {
        log.info("Création d'une nouvelle zone: {}", dto.getNom());
        Zone entity = mapper.toEntity(dto);
        Zone saved = repository.save(entity);
        log.info("Zone créée avec succès, ID: {}", saved.getId());
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ZoneDTO getById(String id) {
        log.debug("Récupération de la zone ID: {}", id);
        Zone entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable"));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ZoneDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ZoneDTO> getAllList() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ZoneDTO update(String id, ZoneDTO dto) {
        log.info("Mise à jour de la zone ID: {}", id);
        Zone existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable"));

        existing.setNom(dto.getNom());
        existing.setCodePostal(dto.getCodePostal());

        return mapper.toDto(repository.save(existing));
    }

    @Override
    public void delete(String id) {
        log.info("Suppression de la zone ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Zone introuvable");
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ZoneDTO> search(String keyword) {
        return repository.findByNomContainingIgnoreCase(keyword)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
