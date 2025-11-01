package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.dto.DestinataireDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.Destinataire;
import com.smart_delivery_management.smartlogi_delivery.exception.ResourceNotFoundException;
import com.smart_delivery_management.smartlogi_delivery.mapper.DestinataireMapper;
import com.smart_delivery_management.smartlogi_delivery.repository.DestinataireRepository;
import com.smart_delivery_management.smartlogi_delivery.service.DestinataireService;
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
public class DestinataireServiceImpl implements DestinataireService {

    private final DestinataireRepository repository;
    private final DestinataireMapper mapper;

    @Override
    public DestinataireDTO create(DestinataireDTO dto) {
        log.info("Création d'un nouveau destinataire");
        Destinataire entity = mapper.toEntity(dto);
        Destinataire saved = repository.save(entity);
        log.info("Destinataire créé avec succès, ID: {}", saved.getId());
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DestinataireDTO getById(String id) {
        log.debug("Récupération du destinataire ID: {}", id);
        Destinataire entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destinataire introuvable"));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DestinataireDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public DestinataireDTO update(String id, DestinataireDTO dto) {
        log.info("Mise à jour du destinataire ID: {}", id);
        Destinataire existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destinataire introuvable"));

        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setEmail(dto.getEmail());
        existing.setTelephone(dto.getTelephone());
        existing.setAdresse(dto.getAdresse());

        return mapper.toDto(repository.save(existing));
    }

    @Override
    public void delete(String id) {
        log.info("Suppression du destinataire ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Destinataire introuvable");
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DestinataireDTO> search(String keyword) {
        log.debug("Recherche de destinataires avec le mot-clé: {}", keyword);
        return repository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}

