package com.lumbseat.lumbseat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lumbseat.lumbseat.dataBase.SQLiteConnectionHelper;
import com.lumbseat.lumbseat.graphics.GraphicHelper;
import com.lumbseat.lumbseat.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HistoricosActivity extends FragmentActivity implements OnChartValueSelectedListener {

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    int mes_desde;
    int dia_desde;
    int anio_desde;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    SQLiteDatabase db;
    String desde_date_min;
    String hasta_date_min;
    String desde_date_max;
    String hasta_date_max;

    Button btnDesde;
    Button btnHasta;
    Button btnVer;
    EditText editTextDesde;
    EditText editTextHasta;
    PieChart pieChart;
    BarChart barChart;

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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar today = Calendar.getInstance();
        Date todayDate = today.getTime();

        desde_date_min = getMinDate();
        hasta_date_min = getMinDate();

        desde_date_max = sdf.format(todayDate);
        hasta_date_max = sdf.format(todayDate);

        //DATEPICKER
        btnDesde = (Button)findViewById(R.id.btnDesde);
        btnDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerFechaDesde("desde");
            }
        });

        btnHasta = (Button)findViewById(R.id.btnHasta);
        btnHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerFechaHasta("hasta");
            }
        });

        editTextDesde = (EditText) findViewById(R.id.editTextDesde);
        editTextHasta = (EditText) findViewById(R.id.editTextHasta);

        pieChart = (PieChart) findViewById(R.id.piechartHistorico);
        barChart = (BarChart) findViewById(R.id.barchartHistorico);

        pieChart.setNoDataText("");
        barChart.setNoDataText("");

        SQLiteConnectionHelper conn = new SQLiteConnectionHelper(this, Utilities.BASE_DATOS, null, 1);
        db = conn.getWritableDatabase();

        btnVer = (Button)findViewById(R.id.btnVer);
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerDatos();
            }
        });

    }

    private void ObtenerFechaDesde(final String tipo){
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

                editTextDesde.setText(year + "-" + mesFormateado + "-" + diaFormateado);
                hasta_date_min = editTextDesde.getText().toString();
            }
        },anio_desde, mes_desde, dia_desde);

        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(sdf.parse(desde_date_min));
            anio_desde = sdf.parse(desde_date_min).getYear();
            mes_desde = sdf.parse(desde_date_min).getMonth();
            dia_desde = sdf.parse(desde_date_min).getDay();
        } catch (Exception e) {
        }

        recogerFecha.getDatePicker().setMinDate(cal.getTimeInMillis());

        try {
            cal.setTime(sdf.parse(desde_date_max));
        } catch (Exception e) {

        }
        //el maxDate tiene que ser como el beginDate del Hasta como mínimo
        recogerFecha.getDatePicker().setMaxDate(cal.getTimeInMillis());

        recogerFecha.show();
    }

    private void ObtenerFechaHasta(final String tipo){
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

                editTextHasta.setText(year + "-" + mesFormateado + "-" + diaFormateado);
                desde_date_max = editTextHasta.getText().toString();

            }
        },anio, mes, dia);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(sdf.parse(hasta_date_min));
        } catch (Exception e) {

        }
        //el minDate tiene que ser como el beginDate como mínimo
        recogerFecha.getDatePicker().setMinDate(cal.getTimeInMillis());

        Calendar today = Calendar.getInstance();
        recogerFecha.getDatePicker().setMaxDate(today.getTimeInMillis());
        recogerFecha.show();
    }

    private void ObtenerDatos() {

        String beginDate = editTextDesde.getText().toString();
        String endDate = editTextHasta.getText().toString();

        if(!beginDate.matches("") && !endDate.matches(""))
        {
            PieChart pieChart = (PieChart) findViewById(R.id.piechartHistorico);
            GraphicHelper.configurateHistoricPieChart(pieChart, this, beginDate, endDate, db);
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar today = Calendar.getInstance();
            Date todayDate = today.getTime();

            GraphicHelper.configurateHistoricPieChart(pieChart, this, sdf.format(todayDate), sdf.format(todayDate), db);

            GraphicHelper.configurateHistoricBarChart(barChart, sdf.format(todayDate), sdf.format(todayDate), db);
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

    private String getMinDate()
    {
        SQLiteConnectionHelper conn = new SQLiteConnectionHelper(this, Utilities.BASE_DATOS, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        String piechart_query = "SELECT MIN(SUBSTR(" + Utilities.CAMPO_TIMESTAMP + ",1,10))" +
                "        FROM " + Utilities.TABLA_DATOS;

        Cursor cursor = db.rawQuery(piechart_query, null);
        String min_date = "";

        while(cursor.moveToNext()) {
            min_date = cursor.getString(0);
        }

        cursor.close();
        return min_date;
    }
}

