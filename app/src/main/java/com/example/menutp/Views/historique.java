package com.example.menutp.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menutp.Controles.Controle;
import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Modele.Seance;
import com.example.menutp.R;

import java.util.ArrayList;
import java.util.List;

public class historique extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    private static Controle controle;
    private AccesLocal accesLocal;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        this.controle = Controle.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Historique des séances");

        //On recupere la liste de toutes les seances
        final List<Seance> seanceList = controle.getToutesSeances(this);
        List<String> seancesStr = new ArrayList<String>();

        //On convertit le tout en chaîne de caractère pour pouvoir l'afficher
        for (Seance s : seanceList) {
            seancesStr.add(s.toString());
        }
        //On place les chaines de chaque séance dans le listview.
        ListView listView = findViewById(R.id.lv_Seance);
        ArrayAdapter<String> adapterString = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, seancesStr);
        listView.setAdapter(adapterString);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccesLocal accesLocal = new AccesLocal(historique.this);
                List<Seance> seanceList = controle.getToutesSeances(historique.this);
                int idSeance =seanceList.get(position).getIdSeance();
                Intent intent = new Intent(historique.this, ajout_exercice.class);
                intent.putExtra("ID_SEANCE", idSeance);
                startActivity(intent);
            }
        });
        
              listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AccesLocal accesLocal = new AccesLocal(historique.this);
                List<Seance> SeanceList = accesLocal.getAllSeance();
                Button btn_Suppr = findViewById(R.id.btn_SupprimerSeance);
                btn_Suppr.setVisibility(View.VISIBLE);
                idExAsuppr = seanceList.get(position).getIdSeance();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                btn_Suppr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(historique.this,"Suppression", Toast.LENGTH_LONG).show();
                    }
                });

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
            Intent i = new Intent(historique.this, Parametres.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

}
