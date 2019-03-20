package com.example.menutp.Views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Modele.Exercice;
import com.example.menutp.Modele.Seance;
import com.example.menutp.Modele.TypeExercice;
import com.example.menutp.Outils.FileOperation;
import com.example.menutp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Stats extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_stat));


    }

    @Override
    protected void onStart() {
        super.onStart();
        AccesLocal accesLocal = new AccesLocal(this);
        afficherTypeSeance();


        Spinner spinnerExercice = findViewById(R.id.Spinner_Exercice);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.exercicesStat, R.layout.my_spinner);
        adapter.setDropDownViewResource(R.layout.my_spinner_drop_down);
        spinnerExercice.setAdapter(adapter);
        spinnerExercice.setOnItemSelectedListener(this);
        afficherPoidExercice(spinnerExercice.getSelectedItem().toString());

        Button btnAccueil = findViewById(R.id.Accueil);
        btnAccueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Stats.this, MainActivity.class);
                startActivity(intent);

            }
        });

        //Traitement du passage en mode paysage
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            setContentView(R.layout.activity_stat_landscape);

            spinnerExercice = findViewById(R.id.Spinner_Exercice);

            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.exercicesStat, R.layout.my_spinner);
            spinnerExercice.setAdapter(adapter2);
            adapter2.setDropDownViewResource(R.layout.my_spinner_drop_down);
            spinnerExercice.setOnItemSelectedListener(this);
            afficherPoidExercice(spinnerExercice.getSelectedItem().toString());
            afficherTypeSeance();
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.title_stat));

            btnAccueil = findViewById(R.id.Accueil);
            btnAccueil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Stats.this, MainActivity.class);
                    startActivity(intent);

                }
            });


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void afficherPoidExercice(String nomType) {

        AccesLocal accesLocal = new AccesLocal(Stats.this);
        GraphView graph = (GraphView) findViewById(R.id.graphExercice);
        graph.removeAllSeries();
        TypeExercice typeExercice = accesLocal.getTypeFromString(nomType);

        Map<Date, Exercice> mapExercice = accesLocal.getToutLesExerciceDeType(typeExercice);

        List<Date> dates = new ArrayList<>();

        Set keys = mapExercice.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            Date key = (Date) it.next();
            dates.add(key);
        }
        if(dates.size()>=1){
            Collections.sort(dates, new Comparator<Date>() {

                @Override
                public int compare(Date o1, Date o2) {
                    return o1.compareTo(o2);
                }
            });



            Map<Date, Double> mapPoids = new HashMap<>();

            for (Date d : dates) {
                mapPoids.put(d, accesLocal.getPoidsMaxExercice(mapExercice.get(d)));
            }


            //Toast.makeText(this, "" + mapPoids, Toast.LENGTH_LONG).show();
            LineGraphSeries<DataPoint> exercices = new LineGraphSeries<>(new DataPoint[]{});
            int i = 0;
            for (Date d : dates) {
                DataPoint dp = new DataPoint(d, mapPoids.get(d));
                exercices.appendData(dp, true, dates.size() + 1);
                i++;
            }
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
            graph.getGridLabelRenderer().setNumHorizontalLabels(3);

            // set manual x bounds to have nice steps
            graph.getViewport().setMinX(dates.get(0).getTime());
            graph.getViewport().setMaxX(dates.get(dates.size()-1).getTime());
            graph.getViewport().setXAxisBoundsManual(true);



            graph.getGridLabelRenderer().setHumanRounding(false);



            exercices.setDrawDataPoints(true);

            graph.getViewport().setMinY(0);
            graph.addSeries(exercices);

        }
        else{

            Toast.makeText(this, "Tu n'as pas encore effectué cet exercice", Toast.LENGTH_SHORT).show();
        }


    }


    public void afficherTypeSeance() {
        GraphView graph = (GraphView) findViewById(R.id.graphSeance);
        AccesLocal accesLocal = new AccesLocal(Stats.this);
        List<Integer> list = new ArrayList<>();
        String[] TypeSeances = getResources().getStringArray(R.array.seances);


        for (String typeSeance : TypeSeances) {
            list.add(accesLocal.countNbSeanceType(typeSeance));
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{});
        for (int i = 0; i < list.size(); i++) {

            DataPoint dp = new DataPoint(i, list.get(i));
            series.appendData(dp, true, list.size() + 1);
        }
        series.setTitle("Répartitions des types de séance");

        graph.getViewport().setScrollable(true);

        //Labels
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(TypeSeances);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });

        series.setValuesOnTopColor(Color.RED);
        series.setDrawValuesOnTop(true);
        series.setSpacing(10);

        graph.addSeries(series);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.toString() == getResources().getString(R.string.Param)) {
            Intent i = new Intent(Stats.this, Parametres.class);
            startActivity(i);
        } else if (item.getTitle() == getResources().getString(R.string.stats)) {
            Intent i = new Intent(Stats.this, Stats.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner_Exercice = (Spinner) findViewById(R.id.Spinner_Exercice);
        afficherPoidExercice(spinner_Exercice.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
