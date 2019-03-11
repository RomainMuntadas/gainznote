package com.example.menutp.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_exercice);

        AccesLocal accesLocal = new AccesLocal(this);
        Controleur controleur = new Controleur();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Création de la séance");
        //On récupère l'id de la séance passée en parametre
        this.idSeance = getIntent().getIntExtra("ID_SEANCE", -1);
        if (this.idSeance == -1) {
            Toast.makeText(this, "Erreur de récupération de l'id", Toast.LENGTH_LONG).show();
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

        List<Exercice> exerciceList = accesLocal.getExerciceSeance(idSeance);
        List<String> exerciceStrings = new ArrayList<>();
        for (Exercice ex : exerciceList) {
            exerciceStrings.add(ex.toString() + " " +accesLocal.getNomExercice(ex.getIdType()));
        }
        ListView listView = findViewById(R.id.Lv_ExerciceSeance);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciceStrings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class Controleur implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(v instanceof Button){
                Button Bouton = (Button) v;
                if(Bouton.getText().toString().equals(getString(R.string.newExercice))){
                    Intent intent = new Intent(ajout_exercice.this, creer_modifier_Exercice.class);
                    intent.putExtra("ID_SEANCE", ajout_exercice.idSeance);

                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(ajout_exercice.this, MainActivity.class);
                    startActivity(intent);
                }

            }

        }
    }
}
