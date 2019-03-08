package com.example.menutp.Modele;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.menutp.Outils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AccesLocal {
    //propriétés
    private String nomBase = "bdGainzNote.sqlite";
    private Integer versionBase = 1;
    private MySQLiteOpenHelper accesBd;
    private SQLiteDatabase bd;


    /**
     * Constructeur
     *
     * @param contexte
     */
    public AccesLocal(Context contexte) {
        accesBd = new MySQLiteOpenHelper(contexte, nomBase, null, versionBase);
    }

    public void addSeance(Seance seance) {
        bd = accesBd.getWritableDatabase();
        String req = "Insert into Seance (nomSeance,dateSeance,typeSeance,dureeSeance,notes) values";
        req += "(\"" + seance.getNomSeance() + "\",\"" + seance.getDateSeance() + "\",\"" + seance.getTypeSeance() + "\",\"" + seance.getDureeSeance() + "\",\"" + seance.getNotes() + "\");";
        bd.execSQL(req);
    }

    /**
     * Recuperation de la derniere seance de la base de donnee.
     *
     * @return
     */
    public Seance getLastSeance() {
        bd = accesBd.getReadableDatabase();
        Seance seance = null;
        String req = "Select * from Seance";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToLast();
        if (!curseur.isAfterLast()) {
            seance = cursorToSeance(curseur);
        }
        curseur.close();
        return seance;
    }

    /**
     * Cree une liste de toutes les séances
     *
     * @return une liste des séances
     */
    public List<Seance> getAllSeance() {
        bd = accesBd.getReadableDatabase();
        List<Seance> seances = new ArrayList<Seance>();
        String req = "Select * from Seance";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()) {
            seances.add(cursorToSeance(curseur));
            while (!curseur.isLast()) {
                curseur.moveToNext();
                seances.add(cursorToSeance(curseur));

            }
        }
        return seances;
    }

    /**
     * Permet de créer une seance directement avec un curseur
     *
     * @param curseur
     * @return
     */
    public Seance cursorToSeance(Cursor curseur) {
        int idSeance = curseur.getInt(0);
        String nomSeance = curseur.getString(1);
        String dateSeance = curseur.getString(2);
        String typeSeance = curseur.getString(3);
        int dureeSeance = curseur.getInt(4);
        String notes = curseur.getString(5);
        return new Seance(nomSeance, dateSeance, typeSeance, dureeSeance, notes);
    }


    public void addExercice(Exercice exercice) {
        bd = accesBd.getWritableDatabase();
        String req = "insert into Excercice values(" + exercice.getIdSeance() + "," + exercice.getIdType() + "\",\"" + exercice.getNotes() + "\");";
        bd.execSQL(req);
    }

    public List<Exercice> getExerciceSeance(Seance seance) {
        bd = accesBd.getReadableDatabase();
        String req = "Select * from Exercice WHERE Exercice.idSeance = " + seance.getIdSeance() + ";";
        bd.execSQL(req);
        return null;

    }

    public Exercice cursorToExercice(Cursor curseur) {
        String nomExercice;
        return null;
    }

    /**
     * Clean la bd
     */
    public void viderBdd() {
        bd = accesBd.getWritableDatabase();
        String req = "delete from Seance";
        bd.execSQL(req);
    }


}
