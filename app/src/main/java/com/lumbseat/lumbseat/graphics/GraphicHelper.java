package com.lumbseat.lumbseat.graphics;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lumbseat.lumbseat.HistoricosActivity;
import com.lumbseat.lumbseat.MainActivity;

import java.util.ArrayList;

public class GraphicHelper {

    public static BarChart configurateBarChart(BarChart originalBarchart, SQLiteDatabase db)
    {
        BarChart configuratedBarChart = originalBarchart;

        EmptyDateFiller edf = new EmptyDateFiller(db);

        BarChartDataHelper barChartDataHelper = new BarChartDataHelper(db);
        ArrayList<BarEntry> entries = barChartDataHelper.GetEntriesData();
        ArrayList<String> labels = barChartDataHelper.GetLabelsData();

        BarDataSet barDataSet = new BarDataSet(entries, "PORCENTAJE BIEN SENTADO");
        BarData dataBar = new BarData(labels, barDataSet);

        configuratedBarChart.setData(dataBar); // set the data and list of labels into chart
        configuratedBarChart.setDescription(" ");

        final int[] LUMBSEAT_COLORS = {
                Color.rgb(15, 60, 79), Color.rgb(18, 70, 91), Color.rgb(22, 88, 114),
                Color.rgb(26, 98, 124), Color.rgb(30, 116, 140), Color.rgb(34, 136, 156),
                Color.rgb(38, 156, 170)
        };
        barDataSet.setColors(LUMBSEAT_COLORS);

        configuratedBarChart.animateY(2000);
        configuratedBarChart.getAxisLeft().setAxisMinValue(0);
        configuratedBarChart.getAxisLeft().setAxisMaxValue(110);

        configuratedBarChart.getAxisRight().setEnabled(false);

        configuratedBarChart.setDrawGridBackground(false);

        return configuratedBarChart;
    }

    public static PieChart configuratePieChart(PieChart originalPieChart, MainActivity mainActivity, SQLiteDatabase db)
    {
        PieChartDataHelper PieChartGdh = new PieChartDataHelper(db);
        PieChart configuratedPieChart = originalPieChart;

        configuratedPieChart.setUsePercentValues(true);

        ArrayList<Entry> yvalues = PieChartGdh.GetEntriesData(db);

        ArrayList<String> xVals = PieChartGdh.GetLabelsData(db);

       /*ArrayList<Entry> yvalues = new ArrayList<Entry>();
       yvalues.add(new Entry(35f, 0));
       yvalues.add(new Entry(75f, 1));

       ArrayList<String> xVals = new ArrayList<String>();
       xVals.add("BIEN SENTADO");
       xVals.add("MAL SENTADO");
*/
        PieDataSet dataSet = new PieDataSet(yvalues, "");
        final int[] ROJOVERDE = {Color.rgb(87, 193, 75), Color.rgb(209, 87, 87)};
        dataSet.setColors(ROJOVERDE);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);

        configuratedPieChart.setData(data);
        configuratedPieChart.setDescription("Postura corporal diaria");
        configuratedPieChart.setDescriptionTextSize(12f);
        configuratedPieChart.setHoleColor(android.R.color.transparent);
        configuratedPieChart.setDrawHoleEnabled(true);
        configuratedPieChart.setTransparentCircleRadius(30f);
        configuratedPieChart.setHoleRadius(25f);
        configuratedPieChart.setOnChartValueSelectedListener(mainActivity);
        configuratedPieChart.setDrawSliceText(false);
        configuratedPieChart.setDescriptionColor(Color.BLACK);
        configuratedPieChart.animateXY(2000, 2000);

        return configuratedPieChart;
    }

    public static PieChart configurateHistoricPieChart(PieChart originalPieChart, HistoricosActivity historicosActivity, String beginDate, String endDate, SQLiteDatabase db)
    {
        PieChartHistoricDataHelper PieChartGdh = new PieChartHistoricDataHelper(beginDate, endDate, db);
        PieChart configuratedPieChart = originalPieChart;

        configuratedPieChart.setUsePercentValues(true);

        ArrayList<Entry> yvalues = PieChartGdh.GetEntriesData(db);

        ArrayList<String> xVals = PieChartGdh.GetLabelsData(db);

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        final int[] ROJOVERDE = {Color.rgb(87, 193, 75), Color.rgb(209, 87, 87)};
        dataSet.setColors(ROJOVERDE);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);

        configuratedPieChart.setData(data);
        configuratedPieChart.setDescription("Postura corporal histórica");
        configuratedPieChart.setDescriptionTextSize(12f);
        configuratedPieChart.setHoleColor(android.R.color.transparent);
        configuratedPieChart.setDrawHoleEnabled(true);
        configuratedPieChart.setTransparentCircleRadius(30f);
        configuratedPieChart.setHoleRadius(25f);
        configuratedPieChart.setOnChartValueSelectedListener(historicosActivity);
        configuratedPieChart.setDrawSliceText(false);
        configuratedPieChart.setDescriptionColor(Color.BLACK);
        configuratedPieChart.animateXY(2000, 2000);

        return configuratedPieChart;
    }

}

