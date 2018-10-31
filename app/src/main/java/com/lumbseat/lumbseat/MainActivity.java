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

    final int REQUEST_ENABLE_BT = 1;
    public static BluetoothAdapter mBluetoothAdapter;
    public static String path;
    SQLiteDatabase db2;

    Context context = this;
    PieChart pieChart;
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
        /*SQLiteDatabase db = conn.getWritableDatabase();

        //db.delete(Utilities.TABLA_DATOS, null, null);

        ContentValues values = new ContentValues();

        try {
            values.put(Utilities.CAMPO_ID, 1);
            values.put(Utilities.CAMPO_TIMESTAMP, "2018-10-20 12:33:50.698023");
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ATRAS_DER, 3.50 );
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ATRAS_IZQ, 3.50);
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ADEL_DER, 3.41);
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ADEL_IZQ, 3.43);
            values.put(Utilities.CAMPO_SENS_DIST_LUMBAR, 1);
            values.put(Utilities.CAMPO_SENS_DIST_CERVICAL, 1);
            values.put(Utilities.CAMPO_BIEN_SENTADO, 0);
            values.put(Utilities.CAMPO_MAL_ABAJO_LEJOS, 1);
            values.put(Utilities.CAMPO_MAL_ARRIBA_LEJOS, 1);
            values.put(Utilities.CAMPO_MAL_SENTADO_DER, 1);
            values.put(Utilities.CAMPO_MAL_SENTADO_IZQ, 1);

            db.insert(Utilities.TABLA_DATOS, Utilities.CAMPO_ID, values);

        } catch (Exception e) {

        }

        //Toast.makeText(getApplicationContext(),"ID: "+ idResultante,Toast.LENGTH_SHORT).show();
        db.close();*/

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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SQLiteConnectionHelper conn = new SQLiteConnectionHelper(context, Utilities.BASE_DATOS, null, 1);
                db2 = conn.getReadableDatabase();
                GraphicHelper.configuratePieChart(pieChart,main, db2);
                db2.close();
                handler.postDelayed(this, 100);
            }
        }, 100);


        /*Runnable refresh = new Runnable() {
            @Override
            public void run() {
                new BackgroundTask().execute();
                //Se ejecuta cada 1 segundo el codigo del metodo run de la interfaz Runnable refresh
                handler.postDelayed(this, 1000);
            }
        };*/
        //Se ejecuta una vez el codigo del metodo run de la interfaz Runnable refresh
        //handler.postDelayed(refresh, 1000);


        //BLUETOOTH
        mBluetoothAdapter  = BluetoothAdapter.getDefaultAdapter();
        String status = "";
        if (mBluetoothAdapter == null) {
            status = "Este dispositivo no soporta Bluetooth";
        }else{
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_ENABLE_BT  && resultCode  == RESULT_OK) {
                Intent i = new Intent(MainActivity.this, BluetoothList.class);
                startActivity(i);
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
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

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SQLiteConnectionHelper conn = new SQLiteConnectionHelper(context, Utilities.BASE_DATOS, null, 1);
            db2 = conn.getReadableDatabase();
            GraphicHelper.configuratePieChart(pieChart,MainActivity.this, db2);
            db2.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

}