package com.example.menutp.Modele;

public class TypeExercice {
    private String nom;
    private String groupeMusculaire;
    private boolean est_polyarticulaire;

    public TypeExercice(String nom, String musclePrimaire, boolean est_polyarticulaire) {
        this.nom = nom;
        this.groupeMusculaire = musclePrimaire;
        this.est_polyarticulaire = est_polyarticulaire;
    }

    public String getNom() {
        return nom;
    }

    public String getGroupeMusculaire() {
        return groupeMusculaire;
    }

    public boolean estPolyarticulaire() {
        return est_polyarticulaire;
    }

    @Override
    public String toString() {
        String res = nom  + ", " + groupeMusculaire;
        if(est_polyarticulaire){
            res+=", Poly-articulaire";
        }
        return res;

    }
}
