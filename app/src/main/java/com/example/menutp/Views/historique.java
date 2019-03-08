package com.example.menutp.Views;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
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
    private AccesLocal accesLocal;
    @SuppressLint("SetTextI18n")
    private static Controle controle;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        this.controle = Controle.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ListView listView = findViewById(R.id.lv_Seance);
        final List<Seance> seanceList = controle.getToutesSeances(this);
        ArrayAdapter<Seance> adapter= new ArrayAdapter<Seance>(this,android.R.layout.simple_list_item_1,
                seanceList);

        List<String> seancesStr = new ArrayList<String>();

        for(int i=0; i<adapter.getCount()-1;i++){
            seancesStr.add(seanceList.get(i).toString());

        }
        ArrayAdapter<String> adapterString= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                seancesStr);

        listView.setAdapter(adapterString);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(historique.this, seanceList.get(position).getNomSeance(),Toast.LENGTH_SHORT).show();
            }
        });


    }


    public static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
