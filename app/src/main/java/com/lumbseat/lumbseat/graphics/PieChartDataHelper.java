package com.lumbseat.lumbseat.graphics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class PieChartDataHelper {

    private SQLiteDatabase db;

    final private String PIECHART_QUERY = "SELECT d1.timeStamp, d2.total * 100.0 / count(*) as bien_sentado, 1 - (d2.total * 100.0 / count(*)) as mal_sentado" +
            "        FROM datos d1 JOIN ( SELECT timeStamp, count(*) total" +
            "                             FROM datos" +
            "                             WHERE bienSentado = 1 GROUP BY timeStamp) d2" +
            "        ON d1.timeStamp = d2.timeStamp GROUP BY d1.timeStamp" +
            "        ";
    private ArrayList<BarEntry> entries = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();

    public PieChartDataHelper(SQLiteDatabase db)
    {
        Cursor cursor = db.rawQuery(PIECHART_QUERY, null);

        int counter = 0;

        while(cursor.moveToNext()) {
            BarEntry entry = new BarEntry(cursor.getFloat(1), counter); //Percent
            counter++;
            entries.add(entry);
            labels.add(cursor.getString(0)); //Date
        }

        cursor.close();
    }

    public ArrayList<BarEntry> GetEntriesData()
    {
        return entries;
    }
}
