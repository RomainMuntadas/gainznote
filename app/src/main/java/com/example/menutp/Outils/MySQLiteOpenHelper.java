package com.example.menutp.Outils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {


    private String type_Exercice =
            "CREATE TABLE TYPE_EXERICE("
                    + " ID_TYPE PRIMARY KEY AUTOINCREMENT,"
                    + " NOM TEXT NOT NULL,"
                    + " MUSCLE TEXT NOT NULL);";

    private String utilisateur =
            "CREATE TABLE UTILISATEUR("
                    + "ID_UTILISATEUR PRIMARY KEY AUTOINCREMENT,"
                    + "NOM TEXT NOT NULL,"
                    + "PRENOM TEXT NOT NULL,"
                    + "POID TEXT NOT NULL,"
                    + "DATE_NAISSANCE TEXT NOT NULL,"
                    + "TAILLE INTEGER NOT NULL,"
                    + "NB_SEANCE INTEGER NOT NULL);";


    private String seance =
            "CREATE TABLE Seance("
                    + "idSeance INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nomSeance TEXT NOT NULL ,"
                    + "dateSeance TEXT NOT NULL,"
                    + "typeSeance TEXT NOT NULL,"
                    + "dureeSeance INTEGER,"
                    + "notes TEXT,"
                    + "ID_UTILISATEUR INT REFERENCES UTILISATEUR(ID_UTILISATEUR) NOT NULL) ;";

    private String exercice =
            "CREATE TABLE Exercice("
                    + "idExcercice INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "TPS_REPOS INTEGER NOT NULL,"
                    + " ID_SEANCE INT REFERENCES SEANCE(ID_SEANCE) NOT NULL,"
                    + " ID_TYPE INT REFERENCES TYPE_EXERCICE(ID_TYPE) NOT NULL);";

    private String serie =
            "CREATE TABLE SERIE(" +
                    "   ID_SERIE COUNTER PRIMARY KEY AUTOINCREMENT," +
                    "   REPETITIONS INT," +
                    "   POIDS INT," +
                    "   ID_EXERCICE INT REFERENCES EXERCICE(ID_EXERCICE) NOT NULL" +
                    ");";

    /**
     * Constructeur
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Si changement de BDD
     *
     * @param db Base de donnee
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(type_Exercice);
        db.execSQL(utilisateur);
        db.execSQL(seance);
        db.execSQL(exercice);
        db.execSQL(serie);


    }

    /**
     * Si changement de version
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
