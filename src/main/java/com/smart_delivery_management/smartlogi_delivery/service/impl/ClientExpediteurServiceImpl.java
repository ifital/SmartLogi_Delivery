package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.dto.ClientExpediteurDTO;
import com.smart_delivery_management.smartlogi_delivery.entities.ClientExpediteur;
import com.smart_delivery_management.smartlogi_delivery.exception.ResourceNotFoundException;
import com.smart_delivery_management.smartlogi_delivery.mapper.ClientExpediteurMapper;
import com.smart_delivery_management.smartlogi_delivery.repository.ClientExpediteurRepository;
import com.smart_delivery_management.smartlogi_delivery.service.ClientExpediteurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClientExpediteurServiceImpl implements ClientExpediteurService {

    private final ClientExpediteurRepository repository;
    private final ClientExpediteurMapper mapper;

    @Override
    public ClientExpediteurDTO create(ClientExpediteurDTO dto) {
        log.info("Création d'un nouveau client: {}", dto.getEmail());
        ClientExpediteur entity = mapper.toEntity(dto);
        ClientExpediteur saved = repository.save(entity);
        log.info("Client créé avec succès, ID: {}", saved.getId());
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientExpediteurDTO getById(String id) {
        log.debug("Récupération du client ID: {}", id);
        ClientExpediteur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientExpediteurDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public ClientExpediteurDTO update(String id, ClientExpediteurDTO dto) {
        log.info("Mise à jour du client ID: {}", id);
        ClientExpediteur existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));

        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setEmail(dto.getEmail());
        existing.setTelephone(dto.getTelephone());
        existing.setAdresse(dto.getAdresse());

        ClientExpediteur updated = repository.save(existing);
        log.info("Client mis à jour avec succès, ID: {}", updated.getId());
        return mapper.toDto(updated);
    }

    @Override
    public void delete(String id) {
        log.info("Suppression du client ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Client introuvable");
        }
        repository.deleteById(id);
        log.info("Client supprimé avec succès, ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientExpediteurDTO> search(String keyword, Pageable pageable) {
        log.debug("Recherche paginée de clients avec le mot-clé: {}", keyword);

        // Recherche par nom ou prénom
        Page<ClientExpediteur> pageResult =
                repository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(keyword, keyword, pageable);

        // Si aucun résultat, on tente la recherche par téléphone
        if (pageResult.isEmpty()) {
            pageResult = repository.findByTelephone(keyword, pageable);
        }

        // Si toujours aucun résultat, on tente par email
        if (pageResult.isEmpty()) {
            repository.findByEmail(keyword).ifPresent(client -> {
                log.debug("Client trouvé par email: {}", client.getEmail());
            });
        }

        return pageResult.map(mapper::toDto);
    }
}
