package com.lumbseat.lumbseat.graphics;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lumbseat.lumbseat.MainActivity;

import java.util.ArrayList;

public class GraphicHelper {

    public static BarChart configurateBarChart(BarChart originalBarchart)
    {
        BarChart configuratedBarChart = originalBarchart;

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(8f, 0));
        entries.add(new BarEntry(2f, 1));
        entries.add(new BarEntry(5f, 2));
        entries.add(new BarEntry(2f, 3));
        entries.add(new BarEntry(7f, 4));
        entries.add(new BarEntry(5f, 5));
        entries.add(new BarEntry(10f, 6));

        BarDataSet bardataset = new BarDataSet(entries, "HORAS BIEN SENTADO");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Do");
        labels.add("Lu");
        labels.add("Ma");
        labels.add("Mi");
        labels.add("Ju");
        labels.add("Vi");
        labels.add("Sa");

        BarData dataBar = new BarData(labels, bardataset);
        configuratedBarChart.setData(dataBar); // set the data and list of labels into chart

        configuratedBarChart.setDescription("Postura corporal semanal");  // set the description
        bardataset.setColors(ColorTemplate.JOYFUL_COLORS);

        configuratedBarChart.animateY(2000);

        return configuratedBarChart;
    }


    public static PieChart configuratePieChart(PieChart originalPieChart, MainActivity mainActivity)
    {
        PieChart configuratedPieChart = originalPieChart;

        configuratedPieChart.setUsePercentValues(true);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(35f, 0));
        yvalues.add(new Entry(75f, 1));

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("BIEN SENTADO");
        xVals.add("MAL SENTADO");

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());

        configuratedPieChart.setData(data);
        configuratedPieChart.setHoleColor(android.R.color.transparent);
        configuratedPieChart.setDescriptionPosition(50,-50);
        configuratedPieChart.setDrawHoleEnabled(true);
        configuratedPieChart.setTransparentCircleRadius(30f);
        configuratedPieChart.setHoleRadius(25f);

        final int[] ROJOVERDE = {
            Color.rgb(87, 193, 75), Color.rgb(209, 87, 87)
        };

        dataSet.setColors(ROJOVERDE);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);
        configuratedPieChart.setOnChartValueSelectedListener(mainActivity);
        configuratedPieChart.setDrawSliceText(false);
        configuratedPieChart.setDescriptionColor(Color.WHITE);
        configuratedPieChart.animateXY(2000, 2000);
        configuratedPieChart.setDescription("POSTURA CORPORAL DIARIA");

        return configuratedPieChart;
    }

}
