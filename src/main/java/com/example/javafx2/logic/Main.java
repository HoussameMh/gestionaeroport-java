package com.example.javafx2.logic;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main{
    public static void main(String[] args){
        System.out.println("=== TEST GESTION AEROPORT ===\n");
        
        try {
            // 1. Création de l'aéroport
            gestion_aeroport aeroport = new gestion_aeroport();
            System.out.println("✓ Aéroport créé\n");
            
            // 2. Création des avions
            Avion avion1 = new Avion(
                "AIR-001",      // matricule
                50.0, 20.0,     // dimensions (largeur, hauteur)
                100.0, 300.0,   // longueur_arret, temps_arret
                120.0, 360.0,   // longueur_depart, temps_depart
                30.0,           // masse_supportee par personne (kg)
                150,            // seat_capacite
                "disponible",   // etat
                0.0, 0.0        // coordonnees initiales
            );
            
            Avion avion2 = new Avion(
                "AIR-002",
                40.0, 18.0,
                80.0, 240.0,
                90.0, 270.0,
                25.0,
                100,
                "disponible",
                0.0, 0.0
            );
            
            aeroport.ajouter_avion(avion1);
            aeroport.ajouter_avion(avion2);
            System.out.println("✓ Avions créés et ajoutés:");
            System.out.println("  - " + avion1.getMatricule() + " (Capacité: " + avion1.getSeat_capacite() + " sièges)");
            System.out.println("  - " + avion2.getMatricule() + " (Capacité: " + avion2.getSeat_capacite() + " sièges)\n");
            
            // 3. Création des pistes
            Piste pisteEscale1 = new Piste(100.0, 200.0, true, "escale", 60.0, 25.0);
            Piste pisteEscale2 = new Piste(300.0, 400.0, true, "escale", 55.0, 22.0);
            Piste pisteParking1 = new Piste(500.0, 600.0, true, "parking", 70.0, 30.0);
            Piste pisteParking2 = new Piste(700.0, 800.0, true, "parking", 65.0, 28.0);
            
            aeroport.ajouter_piste(pisteEscale1);
            aeroport.ajouter_piste(pisteEscale2);
            aeroport.ajouter_piste(pisteParking1);
            aeroport.ajouter_piste(pisteParking2);
            System.out.println("✓ Pistes créées et ajoutées:");
            System.out.println("  - 2 pistes d'escale");
            System.out.println("  - 2 pistes de parking\n");
            
            // 4. Création des employés
            Calendar cal = Calendar.getInstance();
            cal.set(1990, Calendar.JANUARY, 1);
            Date dateNaissance = cal.getTime();
            List<Employee> employees = new ArrayList<>();
            
            Employee pilote = new Employee("EMP001", "Dupont", "Jean", "M", dateNaissance, null, "Captaine");
            Employee copilote = new Employee("EMP002", "Martin", "Marie", "F", dateNaissance, null, "Copilote");
            Employee steward = new Employee("EMP003", "Bernard", "Paul", "M", dateNaissance, null, "STEWARD");
            
            employees.add(pilote);
            employees.add(copilote);
            employees.add(steward);
            System.out.println("✓ Employés créés: " + employees.size() + " employés\n");
            
            // 5. Création d'un vol (avec la date du jour pour tester afficherTableauDesvol)
            List<Passager> passagersInitiaux = new ArrayList<>(); // Vol créé sans passagers initialement
            // Utiliser la date du jour pour que le test de afficherTableauDesvol fonctionne
            cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 10);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date dateVol = cal.getTime();
            
            Vol vol1 = new Vol(
                avion1,
                employees,
                passagersInitiaux,
                "Paris",
                "Lyon",
                dateVol,
                3600 // 1 heure en secondes
            );
            
            System.out.println("✓ Vol créé:");
            System.out.println("  - ID: " + vol1.getId_vol());
            System.out.println("  - De: " + vol1.getAvion().getMatricule());
            System.out.println("  - État avion après création: " + vol1.getAvion().getEtat());
            System.out.println("  - Sièges occupés: " + vol1.getSeat_occupee() + "/" + vol1.getAvion().getSeat_capacite() + "\n");
            
            // Note: Le vol sera sauvegardé lors de la planification (mais l'avion est déjà en "envol")
            // Pour sauvegarder, on utilise ajouter_vol qui ne fait que l'ajouter à la liste
            // La méthode planifier sauvegarde automatiquement via la sérialisation
            aeroport.ajouter_vol(vol1);
            
            // Sauvegarder manuellement après avoir ajouté le vol (car planifier nécessite un avion "disponible")
            try{
                java.io.FileOutputStream fileout = new java.io.FileOutputStream("Vols.ser");
                java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(fileout);
                out.writeObject(aeroport.getVols());
                out.close();
                fileout.close();
                System.out.println("✓ Vols sauvegardés via sérialisation dans Vols.ser\n");
            } catch(Exception e){
                System.out.println("✗ Erreur lors de la sauvegarde: " + e.getMessage() + "\n");
            }
            
            // 7. Création de passagers
            System.out.println("=== Ajout de passagers ===");
            List<Passager> passagers = new ArrayList<>();
            
            for (int i = 1; i <= 5; i++) {
                try {
                    cal.set(1995 + i, Calendar.JANUARY, i);
                    Passager passager = new Passager(
                        "PASS00" + i,
                        "Nom" + i,
                        "Prenom" + i,
                        i % 2 == 0 ? "F" : "M",
                        cal.getTime(),
                        vol1,
                        20.0 + i // Masse bagage différente pour chaque passager
                    );
                    passagers.add(passager);
                    System.out.println("✓ Passager " + i + " ajouté (Sièges occupés: " + vol1.getSeat_occupee() + "/" + vol1.getAvion().getSeat_capacite() + ")");
                } catch (VolCompletException e) {
                    System.out.println("✗ Erreur: " + e.getMessage());
                }
            }
            System.out.println();
            
            // 8. Vérification des bagages
            System.out.println("=== Vérification des bagages ===");
            for (Passager p : passagers) {
                try {
                    if (p.verifier_bag()) {
                        System.out.println("✓ Bagage du passager " + p.getNom() + " OK (" + p.masse_bag() + " kg)");
                    }
                } catch (BagageTropLourdException e) {
                    System.out.println("✗ Erreur bagage: " + e.getMessage());
                }
            }
            System.out.println();
            
            // 9. Test autorisation de descente
            System.out.println("=== Autorisation de descente ===");
            try {
                Piste pisteDescente = aeroport.autoriser_descente_(vol1);
                if (pisteDescente != null) {
                    System.out.println("✓ Descente autorisée sur piste d'escale");
                    System.out.println("  - Coordonnées avion: [" + vol1.getAvion().getCoordonnees()[0] + ", " + vol1.getAvion().getCoordonnees()[1] + "]");
                    System.out.println("  - Piste disponible: " + pisteDescente.getEst_dispo());
                }
            } catch (PisteNonDisponibleException e) {
                System.out.println("✗ Erreur: " + e.getMessage());
            }
            System.out.println();
            
            // 10. Test parking
            System.out.println("=== Parking de l'avion ===");
            Piste pisteParking = aeroport.Parker(vol1);
            if (pisteParking != null) {
                System.out.println("✓ Avion garé sur piste de parking");
                System.out.println("  - Coordonnées avion: [" + vol1.getAvion().getCoordonnees()[0] + ", " + vol1.getAvion().getCoordonnees()[1] + "]");
            } else {
                System.out.println("✗ Aucune piste de parking disponible");
            }
            System.out.println();
            
            // 11. Test autorisation de départ
            System.out.println("=== Autorisation de départ ===");
            Piste pisteDepart = aeroport.autoriser_depart(vol1);
            if (pisteDepart != null) {
                System.out.println("✓ Départ autorisé sur piste d'escale");
                System.out.println("  - Coordonnées avion: [" + vol1.getAvion().getCoordonnees()[0] + ", " + vol1.getAvion().getCoordonnees()[1] + "]");
            } else {
                System.out.println("✗ Aucune piste d'escale disponible pour le départ");
            }
            System.out.println();
            
            // 12. Test exception - vol complet
            System.out.println("=== Test exception: Vol complet ===");
            try {
                // Remplir le vol jusqu'à la capacité maximale
                while (vol1.getSeat_occupee() < vol1.getAvion().getSeat_capacite()) {
                    cal.set(1995, Calendar.JANUARY, 1);
                    new Passager(
                        "PASS" + vol1.getSeat_occupee(),
                        "Nom",
                        "Prenom",
                        "M",
                        cal.getTime(),
                        vol1,
                        20.0
                    );
                }
                // Essayer d'ajouter un passager supplémentaire
                cal.set(1995, Calendar.JANUARY, 1);
                new Passager(
                    "PASS999",
                    "Nom",
                    "Prenom",
                    "M",
                    cal.getTime(),
                    vol1,
                    20.0
                );
                System.out.println("✗ L'exception n'a pas été levée!");
            } catch (VolCompletException e) {
                System.out.println("✓ Exception correctement levée: " + e.getMessage());
            }
            System.out.println();
            
            // 13. Test exception - avion indisponible
            System.out.println("=== Test exception: Avion indisponible ===");
            try {
                // Créer un avion disponible pour le vol
                Avion avionTest = new Avion(
                    "AIR-999", 10.0, 5.0, 50.0, 120.0, 60.0, 180.0,
                    20.0, 50, "disponible", 0.0, 0.0
                );
                aeroport.ajouter_avion(avionTest);
                List<Passager> passagersVides = new ArrayList<>();
                Vol vol2 = new Vol(avionTest, new ArrayList<>(), passagersVides, "A", "B", new Date(), 3600);
                // Maintenant l'avion est en "envol", essayons de planifier à nouveau (devrait échouer)
                aeroport.planifier(vol2);
                System.out.println("✗ L'exception n'a pas été levée!");
            } catch (AvionIndisponibleException e) {
                System.out.println("✓ Exception correctement levée: " + e.getMessage());
            } catch (Exception e) {
                // Si le vol n'a pas été créé (avion pas disponible au moment de la création)
                System.out.println("Note: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
            
            // 14. Test exception - bagage trop lourd
            System.out.println("=== Test exception: Bagage trop lourd ===");
            try {
                Avion petitAvion = new Avion(
                    "AIR-SMALL", 20.0, 10.0, 50.0, 120.0, 60.0, 180.0,
                    15.0, // Masse max supportée: 15 kg
                    50, "disponible", 0.0, 0.0
                );
                List<Passager> passagersVides2 = new ArrayList<>();
                Vol vol3 = new Vol(petitAvion, new ArrayList<>(), passagersVides2, "A", "B", new Date(), 3600);
                cal.set(1995, Calendar.JANUARY, 1);
                Passager passagerLourd = new Passager(
                    "PASS-LOURD",
                    "Nom",
                    "Prenom",
                    "M",
                    cal.getTime(),
                    vol3,
                    50.0 // Bagage de 50 kg, limite: 15 kg
                );
                passagerLourd.verifier_bag();
                System.out.println("✗ L'exception n'a pas été levée!");
            } catch (BagageTropLourdException e) {
                System.out.println("✓ Exception correctement levée: " + e.getMessage());
            } catch (VolCompletException e) {
                // Ignorer si le vol est complet
            }
            System.out.println();
            
            // 15. Sauvegarde finale des vols via sérialisation
            System.out.println("=== Sauvegarde finale des vols (sérialisation) ===");
            try{
                java.io.FileOutputStream fileout = new java.io.FileOutputStream("Vols.ser");
                java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(fileout);
                out.writeObject(aeroport.getVols());
                out.close();
                fileout.close();
                System.out.println("✓ Vols sauvegardés avec succès dans Vols.ser");
            } catch(Exception e){
                System.out.println("✗ Erreur lors de la sauvegarde: " + e.getMessage());
            }
            System.out.println();
            
            // 16. Afficher les vols du jour
            System.out.println("=== Affichage des vols du jour ===");
            aeroport.afficherTableauDesvol();
            System.out.println();
            
            System.out.println("=== TESTS TERMINÉS ===");
            
        } catch (Exception e) {
            System.out.println("✗ ERREUR INATTENDUE: " + e.getMessage());
            e.printStackTrace();
        }
    }
}