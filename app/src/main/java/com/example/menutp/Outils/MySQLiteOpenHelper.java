package com.example.menutp.Outils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {


    private String type_Exercice =
            "CREATE TABLE IF NOT EXISTS TYPE_EXERCICE("
                    + " ID_TYPE INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " NOM TEXT NOT NULL,"
                    + " GROUPE_MUSCULAIRE TEXT NOT NULL,"
                    + " EST_POLY INTEGER NOT NULL);";


    private String utilisateur =
            "CREATE TABLE IF NOT EXISTS UTILISATEUR("
                    + "ID_UTILISATEUR INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "USERNAME TEXT NOT NULL,"
                    + "SEXE TEXT NOT NULL,"
                    + "SEANCES TEXT NOT NULL);";


    private String seance =
            "CREATE TABLE Seance("
                    + "idSeance INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nomSeance TEXT NOT NULL ,"
                    + "dateSeance TEXT NOT NULL,"
                    + "typeSeance TEXT NOT NULL,"
                    + "dureeSeance INTEGER,"
                    + "notes TEXT);";

    private String exercice =
            "CREATE TABLE Exercice("
                    + "ID_EXERCICE INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "TPS_REPOS TEXT NOT NULL,"
                    + "NOTES TEXT,"
                    + "ID_SEANCE INT REFERENCES SEANCE(idSeance) NOT NULL,"
                    + "ID_TYPE INT REFERENCES TYPE_EXERCICE(ID_TYPE) NOT NULL);";

    private String serie =
            "CREATE TABLE SERIE(" +
                    "   ID_SERIE INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "   REPETITIONS INT," +
                    "   POIDS TEXT," +
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


        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Développé militaire\",\"Epaules\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Curl haltères\",\"Bras\",0);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Leg extension\",\"Jambes\",0);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES (\"Leg curl\",\"Jambes\",0);");


        //region Exercice Pectoraux
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Butterfly\",\"Pectoraux\",0);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Dips\",\"Pectoraux\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Développé décliné\",\"Pectoraux\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Développé incliné\",\"Pectoraux\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Ecarté avec haltères\",\"Pectoraux\",0);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Ecarté au poulies\",\"Pectoraux\",0);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY) VALUES(\"Développé couché barre\",\"Pectoraux\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Développé couché haltères\",\"Pectoraux\",1);");
        //endregion

        //region Exercices Dos
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Traction Prognation\",\"Dos\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Rowing\",\"Dos\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Tirage Vertical\",\"Dos\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Tirage Horizontal\",\"Dos\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Extension lombaire\",\"Dos\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Pullover\",\"Dos\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Shrug incliné\",\"Dos\",0);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Rowing unilateral\",\"Dos\",0);");


        //endregion

        //region Exercices Jambes
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Squat\",\"Jambes\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Soulevé de terre\",\"Jambes\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Presse\",\"Jambes\",1);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Leg Extension\",\"Jambes\",0);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Leg Curl\",\"Jambes\",0);");
        db.execSQL("INSERT INTO TYPE_EXERCICE(NOM,GROUPE_MUSCULAIRE,EST_POLY)VALUES(\"Mollets debouts\",\"Jambes\",0);");



        //endregion

        //region Exercices Bras

        //endregion








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
