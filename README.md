
## Contexte du projet
La société **SmartLogi**, spécialisée dans la livraison de colis à travers le Maroc, souhaite moderniser et automatiser la gestion de ses opérations logistiques.  

Actuellement, les livraisons sont gérées manuellement à l’aide de fichiers Excel et de registres papier, ce qui engendre :
- Erreurs de saisie
- Pertes de données
- Retards dans les livraisons
- Manque de visibilité sur le suivi des colis

Pour répondre à ce besoin, **SmartLogi** a sollicité le développement d’une solution web : le **Smart Delivery Management System (SDMS)**.

### Objectifs du projet
L’application permettra de :
- Collecter les colis depuis les clients expéditeurs
- Stocker temporairement les colis dans les entrepôts
- Planifier et assurer la livraison aux destinataires
- Fournir une traçabilité complète via un historique des statuts
- Optimiser la planification logistique par zones géographiques et priorité des colis
- Réduire les erreurs humaines et améliorer la fiabilité du suivi

---

## Utilisateurs
| Rôle | Fonctionnalités principales |
|------|----------------------------|
| **Gestionnaire logistique** | Supervise toutes les opérations, planifie les tournées, gère les livreurs et le stock |
| **Livreur** | Consulte ses colis assignés et met à jour leur statut |
| **Client expéditeur** | Crée des demandes de livraison et suit ses colis |
| **Destinataire** | Suit l’état des colis reçus |

---

## User Stories

### Client expéditeur
- Créer une demande de livraison pour envoyer un colis
- Consulter la liste de ses colis en cours et livrés
- Recevoir des notifications par email (bonus)

### Destinataire
- Consulter le statut des colis destinés

### Livreur
- Voir la liste de ses colis assignés avec priorité et zone
- Mettre à jour le statut des colis lors de la collecte et de la livraison

### Gestionnaire logistique
- Voir toutes les demandes de livraison et les assigner aux livreurs
- Modifier ou supprimer des informations erronées
- Filtrer et paginer les colis par statut, zone, ville, priorité ou date
- Regrouper les colis par zone, statut ou priorité
- Rechercher un colis, client ou livreur par mot-clé
- Calculer le poids total et le nombre de colis par livreur et par zone
- Identifier les colis en retard ou prioritaires et recevoir des alertes
- Associer plusieurs produits à un colis
- Consulter l’historique complet d’un colis avec toutes les étapes et commentaires

---

## Modèle métier / Tables principales
- **ClientExpéditeur** : id, nom, prénom, email, téléphone, adresse  
- **Destinataire** : id, nom, prénom, email, téléphone, adresse  
- **Livreur** : id, nom, prénom, téléphone, véhicule, zoneAssignée  
- **Colis** : id, description, poids, statut, priorité, idLivreur, idClientExpéditeur, idDestinataire, idZone, villeDestination  
- **Zone** : id, nom, codePostal  
- **HistoriqueLivraison** : id, idColis, statut, dateChangement, commentaire  
- **Produit** : id, nom, catégorie, poids, prix  
- **Colis_Produit** : idColis, idProduit, quantité, prix, dateAjout  

---

## Exigences techniques

### Technologies utilisées
- **Backend** : Spring Boot (API REST)
- **Base de données** : PostgreSQL, gestion des versions via Liquibase
- **DTO / Mapping** : MapStruct
- **Documentation API** : Swagger / OpenAPI
- **Logging** : SLF4J
- **Validation** : @Valid, @NotNull, @Size …
- **Pagination & tri** : pour toutes les listes importantes
- **Emails** : SMTP (bonus)
- **Configuration & outils** : YAML, Maven, Git/GitHub, Jira

### Contraintes et bonnes pratiques
- Architecture en couches : Controller → Service → Repository
- Gestion centralisée des exceptions
- Respect des bonnes pratiques Spring et Java (noms, packaging, lisibilité)

---

## Installation et exécution

1. **Cloner le projet**
```bash
git clone https://github.com/votre-utilisateur/sdms.git](https://github.com/ifital/SmartLogi_Delivery.git
cd sdms

````

---

## Diagramme de classe

![SmartLogi SDMS v1 (1)](https://github.com/user-attachments/assets/718e8806-2336-432d-bba1-cce15f1b5d9b)
