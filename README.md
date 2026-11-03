# Hamac

Application interne de gestion des congés : les salariés consultent leur solde
et déposent leurs demandes, les managers les approuvent ou les refusent.

## Démarrer

Prérequis : JDK 25. Maven n'a pas besoin d'être installé (wrapper fourni).

    ./mvnw spring-boot:run      # Windows : mvnw.cmd spring-boot:run

Application sur http://localhost:8080 (pages web et API).
La base H2 est en mémoire : elle est recréée à chaque démarrage à partir de
src/main/resources/data.sql.

## Configuration

Fichier : src/main/resources/application.properties

| Propriété | Rôle |
| --- | --- |
| hamac.leave.fixed-term-days-per-full-month | Droits des CDD par mois complet (2.08) |
| logging.level.org.hibernate.SQL | Affichage des requêtes SQL (debug en développement) |

Toute propriété peut être remplacée par une variable d'environnement, par exemple
HAMAC_LEAVE_FIXEDTERMDAYSPERFULLMONTH=2.2.

## Architecture

Packages par fonctionnalité, sous com.hamac :

- employee : entité Employee, type de contrat, repository.
- leave : contrôleurs REST, LeaveService (règles métier), politiques de congés
  (une classe par type de contrat), DTO, repository des demandes.
- notification : envoi des notifications.
- web : contrôleur de la page, gestion des erreurs de l'API (ProblemDetail).
- Pages : src/main/resources/templates (Thymeleaf) et static/js.
  leave-api.js appelle l'API, leave-api-mock.js fournit des données simulées.

Ajouter un type de contrat : une constante dans ContractType, une classe
@Component qui implémente LeaveEntitlementPolicy, un test unitaire.
L'application refuse de démarrer si un type de contrat n'a pas de politique.

## API

Contrat complet et essais : http://localhost:8080/swagger-ui.html
Description OpenAPI : http://localhost:8080/v3/api-docs

| Verbe | Route | Rôle |
| --- | --- | --- |
| GET | /api/employees/{id}/balance | Solde d'un salarié |
| GET | /api/leave-requests?status= | Liste des demandes, filtrable par statut |
| GET | /api/leave-requests/{id} | Détail d'une demande |
| POST | /api/leave-requests | Dépôt d'une demande (201) |
| POST | /api/leave-requests/{id}/approval | Approbation (409 si déjà traitée) |
| POST | /api/leave-requests/{id}/refusal | Refus (409 si déjà traitée) |

Les erreurs sont renvoyées au format ProblemDetail (RFC 9457).

## Tests

    ./mvnw test

Tests unitaires des politiques de congés, tests d'API du parcours principal
(solde, approbation, double approbation, validation).

## Règles métier et limites connues

Les règles de calcul des droits sont validées par Samuel Okafor (RH) :
ne modifiez pas une règle sans son accord.

- Les jours fériés ne sont pas déduits du nombre de jours ouvrés.
- Les messages d'erreur de l'API sont en français, quelle que soit la langue de la page.
- La politique des CDD renvoie un droit négatif pour une date d'entrée future (à confirmer avec Samuel).
- Aucune authentification : l'application ne doit pas être exposée hors du réseau interne.
- Base en mémoire : les données sont perdues à chaque redémarrage.
