package com.example.menutp.Views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menutp.Controles.Controle;
import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Modele.Seance;
import com.example.menutp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class historique extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    private static Controle controle;

    private AccesLocal accesLocal;

    private int idSeanceASuppr;
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        this.controle = Controle.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Historique des séances");
        rafraichirSeries(this);
    }

    private ArrayList<Map<String, String>> buildData() {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<Seance> seanceList = controle.getToutesSeances(this);
        for(Seance s : seanceList)
            list.add(putData(s.getNomSeance(), s.getTypeSeance() + " | " + s.getDureeSeance() + " minutes"));
        return list;
    }

    private HashMap<String, String> putData(String name, String purpose) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("Text1", name);
        item.put("Text2", purpose);
        return item;
    }

    public void rafraichirSeries(Context context){
        //On recupere la liste de toutes les seances
        final List<Seance> seanceList = controle.getToutesSeances(this);

        ListView listView = findViewById(R.id.lv_Seance);

        ArrayList<Map<String, String>> seances = buildData();
        String[] from = { "Text1", "Text2" };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), seances, android.R.layout.simple_list_item_2, from, to);
        listView.setAdapter(adapter);
        listView.setBackgroundColor(getResources().getColor(R.color.colorBackGround));

        //Un click simple
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccesLocal accesLocal = new AccesLocal(historique.this);
                Button btn_Suppr = findViewById(R.id.btn_SupprimerSeance);
                if(btn_Suppr.getVisibility() == View.VISIBLE){
                    btn_Suppr.setVisibility(View.INVISIBLE);
                }
                List<Seance> seanceList = controle.getToutesSeances(historique.this);
                int idSeance =seanceList.get(position).getIdSeance();
                Intent intent = new Intent(historique.this, NouvelleSeance.class);
                intent.putExtra("ID_SEANCE", idSeance);
                startActivity(intent);

            }
        });

        //un click long
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AccesLocal accesLocal = new AccesLocal(historique.this);
                List<Seance> SeanceList = accesLocal.getAllSeance();
                Button btn_Suppr = findViewById(R.id.btn_SupprimerSeance);
                btn_Suppr.setVisibility(View.VISIBLE);
                idSeanceASuppr = seanceList.get(position).getIdSeance();
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
            Intent i = new Intent(historique.this, Parametres.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    class Controleur implements View.OnClickListener{

        public void onClick(View v) {
            if(v instanceof Button){
                Button button = (Button) v;
                AccesLocal accesLocal = new AccesLocal(historique.this);
                accesLocal.supprimerSeance(idSeanceASuppr);
                rafraichirSeries(historique.this);
                button.setVisibility(View.INVISIBLE);
            }
        }
    }
}
