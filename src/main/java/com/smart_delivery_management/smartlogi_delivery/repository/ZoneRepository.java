package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entities.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, String> {
    Page<Zone> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    Page<Zone> findByCodePostal(String codePostal, Pageable pageable);
}
