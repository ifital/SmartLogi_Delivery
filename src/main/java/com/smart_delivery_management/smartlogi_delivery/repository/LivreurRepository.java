package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entities.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, String> {
    List<Livreur> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    List<Livreur> findByZoneAssigneeId(String zoneId);

    @Query("SELECT l FROM Livreur l WHERE l.zoneAssignee.id = :zoneId")
    List<Livreur> findLivreursByZone(String zoneId);
}
