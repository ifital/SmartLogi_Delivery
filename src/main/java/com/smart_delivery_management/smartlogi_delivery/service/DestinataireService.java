package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.dto.DestinataireDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface DestinataireService {

    DestinataireDTO create(DestinataireDTO dto);

    DestinataireDTO getById(String id);

    Page<DestinataireDTO> getAll(Pageable pageable);

    DestinataireDTO update(String id, DestinataireDTO dto);

    void delete(String id);

    List<DestinataireDTO> search(String keyword);
}
