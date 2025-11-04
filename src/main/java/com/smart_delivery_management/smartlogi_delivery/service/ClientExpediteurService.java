package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.dto.ClientExpediteurDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientExpediteurService {

    ClientExpediteurDTO create(ClientExpediteurDTO dto);

    ClientExpediteurDTO getById(String id);

    Page<ClientExpediteurDTO> getAll(Pageable pageable);

    ClientExpediteurDTO update(String id, ClientExpediteurDTO dto);

    void delete(String id);

    Page<ClientExpediteurDTO> search(String keyword, Pageable pageable);
}