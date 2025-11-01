package com.smart_delivery_management.smartlogi_delivery.service;


import com.smart_delivery_management.smartlogi_delivery.dto.ProduitDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProduitService {

    ProduitDTO create(ProduitDTO dto);
    ProduitDTO getById(String id);
    Page<ProduitDTO> getAll(Pageable pageable);
    ProduitDTO update(String id, ProduitDTO dto);
    void delete(String id);
    List<ProduitDTO> search(String keyword);
}

