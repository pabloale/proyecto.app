package com.lumbseat.lumbseat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lumbseat.lumbseat.bluetooth.BluetoothConnection;
import com.lumbseat.lumbseat.dataBase.SQLiteConnectionHelper;
import com.lumbseat.lumbseat.graphics.GraphicHelper;
import com.lumbseat.lumbseat.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends Activity implements OnChartValueSelectedListener {

    final int REQUEST_ENABLE_BT = 1;
    public static BluetoothAdapter mBluetoothAdapter;


    // Message types sent from the BluetoothReadService Handler
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;


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
        SQLiteDatabase db = conn.getWritableDatabase();


/*
        ContentValues values = new ContentValues();

        try {
            values.put(Utilities.CAMPO_ID, 1);
            values.put(Utilities.CAMPO_TIMESTAMP, "07/10/2018");
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ATRAS_DER, 3.50 );
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ATRAS_IZQ, 3.50);
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ADEL_DER, 3.41);
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ADEL_IZQ, 3.43);
            values.put(Utilities.CAMPO_SENS_DIST_LUMBAR, 1);
            values.put(Utilities.CAMPO_SENS_DIST_CERVICAL, 1);
            values.put(Utilities.CAMPO_BIEN_SENTADO, 1);
            values.put(Utilities.CAMPO_MAL_ABAJO_LEJOS, 1);
            values.put(Utilities.CAMPO_MAL_ARRIBA_LEJOS, 1);
            values.put(Utilities.CAMPO_MAL_SENTADO_DER, 1);
            values.put(Utilities.CAMPO_MAL_SENTADO_IZQ, 1);

            db.insert(Utilities.TABLA_DATOS, Utilities.CAMPO_ID, values);

        } catch (Exception e) {

        }
*/
        //Toast.makeText(getApplicationContext(),"ID: "+ idResultante,Toast.LENGTH_SHORT).show();
        db.close();

        SQLiteDatabase db2 = conn.getReadableDatabase();
        String path = db2.getPath().toString();
        //Toast.makeText(getApplicationContext(),"El path de la base es: "+ path,Toast.LENGTH_SHORT).show();

        //GRAFICO DE BARRAS
        BarChart barChart = (BarChart) findViewById(R.id.barchart);
        GraphicHelper.configurateBarChart(barChart, db2);

        //GRAFICO DE TORTA
        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        GraphicHelper.configuratePieChart(pieChart,this, db2);

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
                ListView devicelist = (ListView)findViewById(R.id.listViewDispositivos);
                BluetoothConnection btConn = new BluetoothConnection(MainActivity.this, devicelist, mHandlerBT);
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

    // The Handler that gets information back from the BluetoothService
    private final Handler mHandlerBT = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    Toast.makeText(MainActivity.this, writeBuf.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Toast.makeText(MainActivity.this, readMessage, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}