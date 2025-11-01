package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.dto.ProduitDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.Produit;
import com.smart_delivery_management.smartlogi_delivery.exception.ResourceNotFoundException;
import com.smart_delivery_management.smartlogi_delivery.mapper.ProduitMapper;
import com.smart_delivery_management.smartlogi_delivery.repository.ProduitRepository;
import com.smart_delivery_management.smartlogi_delivery.service.ProduitService;
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
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository repository;
    private final ProduitMapper mapper;

    @Override
    public ProduitDTO create(ProduitDTO dto) {
        log.info("Création d'un nouveau produit: {}", dto.getNom());
        Produit entity = mapper.toEntity(dto);
        Produit saved = repository.save(entity);
        log.info("Produit créé avec succès, ID: {}", saved.getId());
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProduitDTO getById(String id) {
        log.debug("Récupération du produit ID: {}", id);
        Produit entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProduitDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public ProduitDTO update(String id, ProduitDTO dto) {
        log.info("Mise à jour du produit ID: {}", id);
        Produit existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));

        existing.setNom(dto.getNom());
        existing.setCategorie(dto.getCategorie());
        existing.setPoids(dto.getPoids());
        existing.setPrix(dto.getPrix());

        return mapper.toDto(repository.save(existing));
    }

    @Override
    public void delete(String id) {
        log.info("Suppression du produit ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Produit introuvable");
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDTO> search(String keyword) {
        return repository.findByNomContainingIgnoreCase(keyword)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}

