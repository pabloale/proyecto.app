package com.lumbseat.lumbseat.graphics;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
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
import com.lumbseat.lumbseat.utilities.Utilities;

import java.util.ArrayList;

public class BarChartDataHelper {

    private SQLiteDatabase db;

    public BarChartDataHelper(SQLiteDatabase db)
    {
        this.db = db;
    }

    public ArrayList<BarEntry> GetEntriesData(BarChart originalBarchart)
    {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();


        db.rawQuery( "SELECT Rate, count(*) * 100.0 / (select count(*) from MyTable)" +
                "FROM " + Utilities.TABLA_DATOS +
                "GROUP BY " + Utilities.CAMPO_TIMESTAMP, null);

        /*
        SELECT d1.timeStamp, d2.total * 100.0 / count(*)
        FROM datos d1 JOIN ( SELECT timeStamp, count(*) total
                             FROM datos
                             WHERE bienSentado = 1 GROUP BY timeStamp) d2
        ON d1.timeStamp = d2.timeStamp GROUP BY d1.timeStamp
        ORDER BY d1.timeStamp desc
        LIMIT 7
         */
        //new BarEntry(entry.noseque, entry.noseque);

        return entries;
    }

    public ArrayList<String> GetLabelsData(BarChart originalBarchart)
    {
        ArrayList<String> list = new ArrayList<String>();
        return list;
    }
}
