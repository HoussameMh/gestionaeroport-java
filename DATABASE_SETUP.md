# Configuration de la Base de Données - Gestion Aéroport

## Vue d'ensemble

Ce projet utilise JDBC pour la persistance des données dans une base de données SQL. La classe `DataManager` implémente l'accès aux données selon les pratiques enseignées dans le cours JDBC.

## Structure des Tables

### Table `vol`
- `id_vol` : INTEGER PRIMARY KEY AUTO_INCREMENT
- `destination` : VARCHAR(100) NOT NULL
- `depart` : VARCHAR(100) NOT NULL
- `date_vol` : DATE NOT NULL
- `duree_vol` : INTEGER NOT NULL (en secondes)
- `seat_occupee` : INTEGER DEFAULT 0
- `id_avion` : VARCHAR(50) (référence à la table avion)

### Table `passager`
- `num_passeport` : VARCHAR(50) PRIMARY KEY
- `nom` : VARCHAR(100) NOT NULL
- `prenom` : VARCHAR(100) NOT NULL
- `genre` : VARCHAR(10)
- `date_naissance` : DATE NOT NULL
- `masse_bag` : DECIMAL(10,2) NOT NULL (en kg)
- `id_vol` : INTEGER NOT NULL (FOREIGN KEY référençant vol.id_vol)

## Configuration

### Option 1 : H2 Database (Recommandé pour développement/test)

H2 est une base de données en mémoire, déjà configurée par défaut. Aucune installation supplémentaire n'est requise.

**Avantages :**
- Facile à utiliser, pas d'installation nécessaire
- Base de données en mémoire, réinitialisée à chaque exécution
- Idéal pour le développement et les tests

**Configuration actuelle :**
```java
private static final String DB_URL_H2 = "jdbc:h2:mem:aeroport_db;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:database/schema.sql'";
private static final boolean USE_H2 = true;
```

### Option 2 : MySQL (Production)

Pour utiliser MySQL en production :

1. **Installer MySQL** sur votre machine

2. **Créer la base de données :**
```sql
CREATE DATABASE aeroport_db;
USE aeroport_db;
```

3. **Exécuter le script de création des tables :**
```bash
mysql -u root -p aeroport_db < src/main/resources/database/schema.sql
```

4. **Modifier DataManager.java :**
```java
private static final boolean USE_H2 = false; // Changer à false
```

5. **Configurer les identifiants :**
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/aeroport_db";
private static final String DB_USER = "root";  // Votre utilisateur MySQL
private static final String DB_PASSWORD = "votre_mot_de_passe";  // Votre mot de passe
```

## Utilisation

### Ajout d'un Passager via JDBC

La méthode `ajouterPassager()` utilise `PreparedStatement` pour sécuriser et accélérer l'exécution :

```java
DataManager dataManager = new DataManager();
dataManager.initialiserTables();

Passager passager = new Passager(...);
dataManager.ajouterPassager(passager);  // Utilise PreparedStatement avec executeUpdate()
```

### Gestion des Erreurs SQLException

Toutes les opérations JDBC sont encapsulées dans des blocs `try-catch` pour intercepter les `SQLException` :

```java
try {
    dataManager.ajouterPassager(passager);
    System.out.println("Nombre de lignes mises à jour: " + lignesMiseAJour);
} catch (SQLException e) {
    System.err.println("Erreur SQL: " + e.getMessage());
    System.err.println("Code SQL: " + e.getSQLState());
    System.err.println("Code fournisseur: " + e.getErrorCode());
    e.printStackTrace();
}
```

## Mapping des Types Java vers SQL

Comme enseigné dans le cours JDBC :

| Type Java | Type SQL | Méthode PreparedStatement |
|-----------|----------|---------------------------|
| `String` | `VARCHAR` | `setString(index, value)` |
| `int` | `INTEGER` | `setInt(index, value)` |
| `double` | `DECIMAL` | `setDouble(index, value)` |
| `Date` | `DATE` | `setDate(index, new java.sql.Date(date.getTime()))` |

## Exemple d'Utilisation dans PassagersTab

```java
// 1. Vérifier la disponibilité des sièges
selectedVol.seatDispo();

// 2. Vérifier si le passager existe déjà
if (dataManager.passagerExiste(passport)) {
    // Afficher un message d'erreur
}

// 3. Vérifier le poids du bagage
if (masseBagage >= selectedVol.getAvion().getMasse_supportee()) {
    throw new BagageTropLourdException(...);
}

// 4. Créer le passager en mémoire
Passager passager = new Passager(...);

// 5. Insérer dans la base de données via PreparedStatement
try {
    dataManager.insererVol(selectedVol);  // S'assurer que le vol existe
    dataManager.ajouterPassager(passager);  // Utilise PreparedStatement
} catch (SQLException e) {
    // Gérer l'erreur
}
```

## Tests

Pour tester la connexion à la base de données :

1. Exécutez l'application
2. Allez dans l'onglet "Passagers & Employés"
3. Sélectionnez un vol
4. Remplissez le formulaire de passager
5. Cliquez sur "Ajouter Passager"
6. Vérifiez la console pour voir les messages de succès ou d'erreur

## Notes Techniques

- **PreparedStatement** : Utilisé pour toutes les insertions afin d'éviter les injections SQL et améliorer les performances
- **Gestion des transactions** : Les opérations sont automatiquement commitées après chaque `executeUpdate()`
- **Connexion** : Une seule connexion est maintenue par instance de `DataManager` (pattern Singleton)
- **Fermeture des ressources** : Utilisation de try-with-resources pour garantir la fermeture automatique des ressources JDBC
