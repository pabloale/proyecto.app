package com.lumbseat.lumbseat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lumbseat.lumbseat.bluetooth.BluetoothList;
import com.lumbseat.lumbseat.dataBase.SQLiteConnectionHelper;
import com.lumbseat.lumbseat.graphics.GraphicHelper;
import com.lumbseat.lumbseat.utilities.Utilities;


public class MainActivity extends Activity implements OnChartValueSelectedListener, Runnable {

    public static String path;
    SQLiteDatabase db2;

    Context context = this;
    PieChart pieChart;
    PieChart badPosturePieChart;
    MainActivity main;

    Handler handler = new Handler();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_configuration:
                    intent = new Intent(MainActivity.this, ConfigurationActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_exercises:
                    intent = new Intent(MainActivity.this, ExercisesActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_backup:
                    intent = new Intent(MainActivity.this, BackupActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHistorico = (Button) findViewById(R.id.btnHistorico);
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoricosActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemTextColor(ColorStateList.valueOf(Color.WHITE));

        SQLiteConnectionHelper conn = new SQLiteConnectionHelper(this, Utilities.BASE_DATOS, null, 1);

        db2 = conn.getReadableDatabase();
        /*path = db2.getPath().toString();
        Toast.makeText(getApplicationContext(),"El path de la base es: "+ path,Toast.LENGTH_LONG).show();*/

        main = MainActivity.this;

        //GRAFICO DE BARRAS
        BarChart barChart = (BarChart) findViewById(R.id.barchart);
        GraphicHelper.configurateBarChart(barChart, db2);

        //GRAFICO DE TORTA
        pieChart = (PieChart) findViewById(R.id.piechart);
        GraphicHelper.configuratePieChart(pieChart,this, db2);

        //GRAFICO DE TORTA DE MALAS POSTURAS
        badPosturePieChart = (PieChart) findViewById(R.id.badposturepiechart);
        GraphicHelper.configurateBadPosturePieChart(badPosturePieChart,this, db2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SQLiteConnectionHelper conn = new SQLiteConnectionHelper(context, Utilities.BASE_DATOS, null, 1);
                db2 = conn.getReadableDatabase();
                GraphicHelper.configuratePieChart(pieChart,main, db2);
                db2.close();
                handler.postDelayed(this, 200);
            }
        }, 100);


    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
        "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
            + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SQLiteConnectionHelper conn = new SQLiteConnectionHelper(this, Utilities.BASE_DATOS, null, 1);
        db2 = conn.getReadableDatabase();
        GraphicHelper.configuratePieChart(pieChart,this, db2);
        db2.close();
    }

    private void hilos() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SQLiteConnectionHelper conn = new SQLiteConnectionHelper(context, Utilities.BASE_DATOS, null, 1);
                db2 = conn.getReadableDatabase();
                GraphicHelper.configuratePieChart(pieChart,main, db2);
                db2.close();
            }
        }).start();
    }

}