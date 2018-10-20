package com.lumbseat.lumbseat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lumbseat.lumbseat.dataBase.SQLiteConnectionHelper;
import com.lumbseat.lumbseat.graphics.GraphicHelper;
import com.lumbseat.lumbseat.utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;

public class HistoricosActivity extends FragmentActivity implements OnChartValueSelectedListener {

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    Button btnDesde;
    Button btnHasta;
    Button btnVer;
    EditText editTextDesde;
    EditText editTextHasta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historicos);

        FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoricosActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //DATEPICKER
        btnDesde = (Button)findViewById(R.id.btnDesde);
        btnDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerFecha("desde");
            }
        });

        btnHasta = (Button)findViewById(R.id.btnHasta);
        btnHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerFecha("hasta");
            }
        });

        editTextDesde = (EditText) findViewById(R.id.editTextDesde);
        editTextHasta = (EditText) findViewById(R.id.editTextHasta);

        btnVer = (Button)findViewById(R.id.btnVer);
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerDatos();
            }
        });

        SQLiteConnectionHelper conn = new SQLiteConnectionHelper(this, Utilities.BASE_DATOS, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        //GRAFICO DE TORTA
        PieChart pieChart = (PieChart) findViewById(R.id.piechartHistorico);
        GraphicHelper.configurateHistoricPieChart(pieChart, this, db);
    }


    private void ObtenerFecha(final String tipo){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(android.widget.DatePicker  view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? 0 + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? 0 + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                if(tipo.contains("desde")){
                    editTextDesde.setText(year + "/" + mesFormateado + "/" + diaFormateado);
                }else{
                    editTextHasta.setText(year + "/" + mesFormateado + "/" + diaFormateado);
                }
            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    private void ObtenerDatos() {
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
}
