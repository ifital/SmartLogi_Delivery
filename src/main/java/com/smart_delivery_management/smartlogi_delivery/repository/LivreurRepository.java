package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entity.Livreur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, String> {
    Page<Livreur> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable);
    Page<Livreur> findByZoneAssigneeId(String zoneId, Pageable pageable);
    Optional<Livreur> findByEmail(String email);
    @Query("SELECT l FROM Livreur l WHERE l.zoneAssignee.id = :zoneId")
    Page<Livreur> findLivreursByZone(String zoneId, Pageable pageable);
}
