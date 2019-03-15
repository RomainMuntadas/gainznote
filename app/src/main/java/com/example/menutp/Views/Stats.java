package com.example.menutp.Views;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Modele.Exercice;
import com.example.menutp.Modele.Seance;
import com.example.menutp.Modele.TypeExercice;
import com.example.menutp.Outils.FileOperation;
import com.example.menutp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Stats extends AppCompatActivity {

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
        afficherTypeSeance();
        afficherPoidExercice("Développé couché barre");
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            setContentView(R.layout.activity_stat_landscape);
        }
        ;
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void afficherPoidExercice(String nomType) {
        GraphView graph = (GraphView) findViewById(R.id.graphExercice);
        AccesLocal accesLocal = new AccesLocal(Stats.this);
        List<Double> list = new ArrayList<>();

        TypeExercice typeExercice = accesLocal.getTypeFromString(nomType);

        List<Exercice> listExercice = accesLocal.getToutLesExerciceDeType(typeExercice);

        List<Double> poidsMoyen = new ArrayList<>();
        String[] dates = new String[listExercice.size()];

        int i = 0;
        for (Exercice e : listExercice) {
            Seance s = accesLocal.getSeanceFromId(e.getIdSeance());
            poidsMoyen.add(accesLocal.getPoidMoyenExercice(e));
            dates[i] = FileOperation.dateToString(s.getDateSeance());
            i++;
        }


        Toast.makeText(this, listExercice.size() + "", Toast.LENGTH_LONG).show();
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        if (dates.length >= 2) {
            staticLabelsFormatter.setHorizontalLabels(dates);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

            LineGraphSeries<DataPoint> exercices = new LineGraphSeries<>(new DataPoint[]{});
            for (i = 0; i < poidsMoyen.size(); i++) {
                DataPoint dataPoint = new DataPoint(i, poidsMoyen.get(i));
                exercices.appendData(dataPoint, true, poidsMoyen.size());
            }

            graph.getViewport().setScrollable(true);
            graph.addSeries(exercices);
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

}
