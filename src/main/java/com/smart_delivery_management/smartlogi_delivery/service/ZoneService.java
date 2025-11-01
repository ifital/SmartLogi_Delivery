package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.dto.ZoneDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ZoneService {

    ZoneDTO create(ZoneDTO dto);
    ZoneDTO getById(String id);
    Page<ZoneDTO> getAll(Pageable pageable);
    List<ZoneDTO> getAllList();
    ZoneDTO update(String id, ZoneDTO dto);
    void delete(String id);
    List<ZoneDTO> search(String keyword);
}

