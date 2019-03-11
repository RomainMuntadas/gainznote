package com.example.menutp.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Modele.Exercice;
import com.example.menutp.Modele.Serie;
import com.example.menutp.Modele.TypeExercice;
import com.example.menutp.R;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class creer_modifier_Exercice extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private static int idSeance;
    private AccesLocal accesLocal;
    Spinner spinnerGroupeMusculaire;
    List<TypeExercice> listExoGroup = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_modifier__exercice);
        accesLocal = new AccesLocal(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Création de la séance");
        //On récupère l'id de la séance passée en parametre
        this.idSeance = getIntent().getIntExtra("ID_SEANCE", -1);
        if (this.idSeance == -1) {
            Toast.makeText(this, "Erreur de récupération de l'id", Toast.LENGTH_LONG).show();
        }



        List<CharSequence> groupesMusculaire = accesLocal.groupesMusculaire();
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.my_spinner,groupesMusculaire);
        adapter.setDropDownViewResource(R.layout.my_spinner_drop_down);

        spinnerGroupeMusculaire = findViewById(R.id.Spinner_TypeExo);

        spinnerGroupeMusculaire.setAdapter(adapter);
        spinnerGroupeMusculaire.setOnItemSelectedListener(this);


        Button valider = (Button) findViewById(R.id.Btn_Valider);
        valider.setOnClickListener(new Controleur());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerGroupeMusculaire = findViewById(R.id.Spinner_TypeExo);
        listExoGroup = accesLocal.getTypeFromGroupe((String) spinnerGroupeMusculaire.getSelectedItem());
        List<String> listExoGrpStr = new ArrayList<>();
        for(TypeExercice tE: listExoGroup){

            listExoGrpStr.add(tE.toString());
        }

        ArrayAdapter<String> adapteurType = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listExoGrpStr);
        ListView listViewType = findViewById(R.id.Lv_Type);
        listViewType.setAdapter(adapteurType);
        listViewType.setOnItemClickListener(creer_modifier_Exercice.this);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listExoGroup = accesLocal.getTypeFromGroupe((String) spinnerGroupeMusculaire.getSelectedItem());
        TypeExercice typeSelected = listExoGroup.get(position);
        TextView exerciceSelected = findViewById(R.id.TypeChoisi);
        exerciceSelected.setText(typeSelected.getNom());



    }

    class Controleur implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            AccesLocal accesLocal = new AccesLocal(creer_modifier_Exercice.this);

            if(v instanceof  Button){
                //
                TextView exerciceSelected = findViewById(R.id.TypeChoisi);
                TextView tempsRepos = (TextView) findViewById(R.id.ET_TempsRepos);
                TextView notes = findViewById(R.id.ET_Notes);
                String nomType = exerciceSelected.getText().toString();
                int idType = accesLocal.getIdTypeFromString(nomType);
                idSeance = getIntent().getIntExtra("ID_SEANCE", -1);
                Exercice exercice = null;


                //creation des exercices
                Double tempsReposDbl = 0D;
                String tempsReposStr = tempsRepos.getText().toString();

                    //Pour ne pas insert un null  => Double.parseDouble("") ne peut pas marcher
                    if(tempsReposStr.equals("")){

                        exercice = new Exercice(0.0,notes.getText().toString(), idSeance,idType);
                    }
                    else{

                        tempsReposDbl = Double.parseDouble(tempsReposStr);
                        exercice = new Exercice(tempsReposDbl,notes.getText().toString(), idSeance,idType);
                    }

                //Creation des séances

                TextView Et_NbSeries = findViewById(R.id.ET_NbSeries);
                TextView poid = findViewById(R.id.ET_Poid);
                Integer nbSeries = Integer.parseInt(Et_NbSeries.getText().toString());
                for(int i = 0; i<nbSeries;i++ ){
                    Serie s = new Serie(nbSeries,Double.parseDouble(poid.getText().toString()), exercice.getIdExercice() );
                    accesLocal.addSerie(s);

                }
                //on sauvegarde l'exercice
                accesLocal.addExerciceToSeance(exercice);
                Intent intent = new Intent(creer_modifier_Exercice.this, ajout_exercice.class);



                intent.putExtra("ID_SEANCE",idSeance);
                startActivity(intent);


            }
        }
    }
}
