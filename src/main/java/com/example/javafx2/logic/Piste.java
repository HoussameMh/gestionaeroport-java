package com.example.javafx2.logic;
public class Piste {
    private double[] coordonnees =new double[2];
    private String type;
    private double[] dimensions =new double[2];
    private boolean est_dispo;
    private double temps_occup;     // durée d'occupation en secondes
    private long heureDebutOccupation; // timestamp en millisecondes quand la piste a été occupée
    
    public Piste( Double coordonnees_X,Double coordonnees_Y,boolean est_dispo,String type,Double dimensions_W,Double dimensions_H){
        this.coordonnees[0]=coordonnees_X;
        this.coordonnees[1]=coordonnees_Y;
        this.dimensions[0]=dimensions_W;
        this.dimensions[1]=dimensions_H;
        this.type=type;
        this.est_dispo=est_dispo;
        this.temps_occup=0;
        this.heureDebutOccupation=0;

    }
    public void occuper(double temps_occup){
            this.temps_occup=temps_occup;
            this.est_dispo=false;
            this.heureDebutOccupation = System.currentTimeMillis(); // Enregistrer l'heure de début
    }
    public void disponible(){
        this.temps_occup=0;
        this.est_dispo=true;
        this.heureDebutOccupation=0;
    }
    
    /**
     * Vérifie si le temps d'occupation est écoulé et remet la piste disponible si c'est le cas
     * @return true si la piste est maintenant disponible, false sinon
     */
    public boolean verifierEtMettreDisponible(){
        // Si temps_occup == 0, cela signifie parking sans limite de temps (ne pas libérer automatiquement)
        if (!this.est_dispo && this.temps_occup > 0 && this.heureDebutOccupation > 0) {
            long tempsEcouleMs = System.currentTimeMillis() - this.heureDebutOccupation;
            double tempsEcouleSecondes = tempsEcouleMs / 1000.0;
            
            if (tempsEcouleSecondes >= this.temps_occup) {
                // Le temps d'occupation est écoulé
                this.disponible();
                return true;
            }
        }
        return this.est_dispo;
    }
    
    /**
     * Retourne le temps restant avant que la piste redevienne disponible (en secondes)
     * @return temps restant en secondes, ou 0 si disponible
     */
    public double getTempsRestantOccupation() {
        if (this.est_dispo || this.temps_occup <= 0 || this.heureDebutOccupation <= 0) {
            return 0;
        }
        long tempsEcouleMs = System.currentTimeMillis() - this.heureDebutOccupation;
        double tempsEcouleSecondes = tempsEcouleMs / 1000.0;
        double tempsRestant = this.temps_occup - tempsEcouleSecondes;
        return Math.max(0, tempsRestant);
    }

    public double[] getCoordonnees() {
        return this.coordonnees;
    }

    public double getTemps_occup() {
        return this.temps_occup;
    }
    public boolean getEst_dispo(){
        return this.est_dispo;
    }
    public String getType(){
      return this.type;
    }
    public double[] getDimensions() {
        return this.dimensions;
    }
}
