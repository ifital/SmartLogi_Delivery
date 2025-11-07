package com.smart_delivery_management.smartlogi_delivery.repository;

import com.smart_delivery_management.smartlogi_delivery.entity.Colis;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.PrioriteColis;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ColisRepository extends JpaRepository<Colis, String>, JpaSpecificationExecutor<Colis> {

    // Recherche par statut
    Page<Colis> findByStatut(StatutColis statut, Pageable pageable);

    // Recherche par priorité
    Page<Colis> findByPriorite(PrioriteColis priorite, Pageable pageable);

    // Recherche par zone
    Page<Colis> findByZoneId(String zoneId, Pageable pageable);

    // Recherche par ville
    Page<Colis> findByVilleDestinationContainingIgnoreCase(String ville, Pageable pageable);

    // Recherche par livreur
    Page<Colis> findByLivreurId(String livreurId, Pageable pageable);
    List<Colis> findByLivreurId(String livreurId);

    // Recherche par client expéditeur
    Page<Colis> findByClientExpediteurId(String clientId, Pageable pageable);

    // Recherche par destinataire
    List<Colis> findByDestinataireId(String destinataireId);

    // Recherche combinée
    @Query("SELECT c FROM Colis c WHERE " +
            "(:statut IS NULL OR c.statut = :statut) AND " +
            "(:priorite IS NULL OR c.priorite = :priorite) AND " +
            "(:zoneId IS NULL OR c.zone.id = :zoneId) AND " +
            "(:livreurId IS NULL OR c.livreur.id = :livreurId) AND " +
            "(:ville IS NULL OR LOWER(c.villeDestination) LIKE LOWER(CONCAT('%', :ville, '%')))")
    Page<Colis> searchColis(
            @Param("statut") StatutColis statut,
            @Param("priorite") PrioriteColis priorite,
            @Param("zoneId") String zoneId,
            @Param("livreurId") String livreurId,
            @Param("ville") String ville,
            Pageable pageable
    );

    // Statistiques
    @Query("SELECT COUNT(c) FROM Colis c WHERE c.livreur.id = :livreurId")
    Long countByLivreurId(@Param("livreurId") String livreurId);

    @Query("SELECT SUM(c.poids) FROM Colis c WHERE c.livreur.id = :livreurId")
    BigDecimal sumPoidsByLivreurId(@Param("livreurId") String livreurId);

    @Query("SELECT COUNT(c) FROM Colis c WHERE c.zone.id = :zoneId")
    Long countByZoneId(@Param("zoneId") String zoneId);

    @Query("SELECT SUM(c.poids) FROM Colis c WHERE c.zone.id = :zoneId")
    BigDecimal sumPoidsByZoneId(@Param("zoneId") String zoneId);

    // Colis en retard (exemple: en transit depuis plus de 3 jours)
    @Query("SELECT c FROM Colis c WHERE c.statut = 'EN_TRANSIT' AND c.dateCreation < :dateLimit")
    List<Colis> findColisEnRetard(@Param("dateLimit") LocalDateTime dateLimit);

    // Colis prioritaires non assignés
    @Query("SELECT c FROM Colis c WHERE c.priorite IN ('URGENTE', 'EXPRESS') AND c.livreur IS NULL")
    List<Colis> findColisPrioritairesNonAssignes();

    // Groupement par statut
    @Query("SELECT c.statut, COUNT(c) FROM Colis c GROUP BY c.statut")
    List<Object[]> countByStatutGroupBy();

    // Groupement par zone
    @Query("SELECT c.zone.nom, COUNT(c) FROM Colis c GROUP BY c.zone.nom")
    List<Object[]> countByZoneGroupBy();

    // Groupement par priorité
    @Query("SELECT c.priorite, COUNT(c) FROM Colis c GROUP BY c.priorite")
    List<Object[]> countByPrioriteGroupBy();
}
