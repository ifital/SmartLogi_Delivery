package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.dto.LivreurDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LivreurService {
    LivreurDTO create(LivreurDTO dto);
    LivreurDTO getById(String id);
    Page<LivreurDTO> getAll(Pageable pageable);
    LivreurDTO update(String id, LivreurDTO dto);
    void delete(String id);
    List<LivreurDTO> search(String keyword);
    List<LivreurDTO> getByZone(String zoneId);
}
