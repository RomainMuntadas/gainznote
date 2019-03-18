package com.example.menutp.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Modele.Exercice;
import com.example.menutp.R;

import java.util.ArrayList;
import java.util.List;

public class ajout_exercice extends AppCompatActivity {
    private static int idSeance;
    private static int idExAsuppr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_exercice);

        AccesLocal accesLocal = new AccesLocal(this);
        Controleur controleur = new Controleur();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleCreationSeance));

        //On récupère l'id de la séance passée en parametre
        idSeance = getIntent().getIntExtra("ID_SEANCE", -1);
        if (idSeance == -1) {
            Toast.makeText(this, getResources().getString(R.string.ErrorGettingId), Toast.LENGTH_LONG).show();
        }

        Button terminer = findViewById(R.id.btn_Terminer);
        Button creerExo = findViewById(R.id.Btn_Nouveau);

        creerExo.setOnClickListener(controleur);
        terminer.setOnClickListener(controleur);



    }

    @Override
    public void onStart() {
        super.onStart();
        //Pour mettre a jour les exercices.
        AccesLocal accesLocal = new AccesLocal(this);
        idSeance = getIntent().getIntExtra("ID_SEANCE", -1);

        rafraichirListeExercice(this);



    }

    /**
     * Met a jour le listView en fonction de la BD. Ajoute les onItemClick
     * @param context Activitée courante.
     */
    public void rafraichirListeExercice(Context context){

       AccesLocal accesLocal = new AccesLocal(ajout_exercice.this);
        List<Exercice> exerciceList = accesLocal.getExerciceSeance(idSeance);
        List<String> exerciceStrings = new ArrayList<>();
        for (Exercice ex : exerciceList) {
            exerciceStrings.add(ex.toString() + " " + accesLocal.getNomExercice(ex.getIdType()));
        }
        final ListView listView = findViewById(R.id.Lv_ExerciceSeance);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ajout_exercice.this, android.R.layout.simple_list_item_1, exerciceStrings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Button btnSuppr = findViewById(R.id.btn_Supprimer);
                //si le bouton supprimer est visible, on le désactive
                if(btnSuppr.getVisibility() == View.VISIBLE){
                    btnSuppr.setVisibility(View.INVISIBLE);
                }
                else{
                    AccesLocal accesLocal = new AccesLocal(ajout_exercice.this);
                    List<Exercice> exerciceList = accesLocal.getExerciceSeance(idSeance);
                    Intent intent = new Intent(ajout_exercice.this, creer_modifier_Exercice.class);
                    intent.putExtra("ID_SEANCE", ajout_exercice.idSeance);
                    intent.putExtra("ID_EXERCICE", exerciceList.get(position).getIdExercice());
                    startActivity(intent);
                }

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AccesLocal accesLocal = new AccesLocal(ajout_exercice.this);
                List<Exercice> exerciceList = accesLocal.getExerciceSeance(idSeance);
                Button btn_Suppr = findViewById(R.id.btn_Supprimer);
                btn_Suppr.setVisibility(View.VISIBLE);
                idExAsuppr = exerciceList.get(position).getIdExercice();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                btn_Suppr.setOnClickListener(new Controleur());

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.toString() == getResources().getString(R.string.Param)) {
            Intent i = new Intent(ajout_exercice.this, Parametres.class);
            startActivity(i);
        }
        else if (item.getTitle() == getResources().getString(R.string.stats))
        {
            Intent i = new Intent(ajout_exercice.this, Stats.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Vous ne pouvez pas revenir en arrière pendant la création d'une séance", Toast.LENGTH_LONG).show();
    }

    class Controleur implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v instanceof Button) {

                Button Bouton = (Button) v;
                if (Bouton.getText().toString().equals("Supprimer")) {
                    AccesLocal accesLocal = new AccesLocal(ajout_exercice.this);
                    accesLocal.supprimerExercice(idExAsuppr,idSeance);
                    rafraichirListeExercice(ajout_exercice.this);
                    v.setVisibility(View.INVISIBLE);
                    Toast.makeText(ajout_exercice.this,"Suppression" ,Toast.LENGTH_LONG).show();
                }
                else{

                    if (Bouton.getText().toString().equals(getString(R.string.newExercice))) {
                        Intent intent = new Intent(ajout_exercice.this, creer_modifier_Exercice.class);
                        intent.putExtra("ID_SEANCE", ajout_exercice.idSeance);

                        startActivity(intent);
                    } else {

                        Intent intent = new Intent(ajout_exercice.this, MainActivity.class);
                        startActivity(intent);

                    }

                }

            }

        }


    }
}
