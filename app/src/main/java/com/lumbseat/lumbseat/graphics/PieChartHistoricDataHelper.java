package com.lumbseat.lumbseat.graphics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PieChartHistoricDataHelper {

    private ArrayList<Entry> entries = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();

    public PieChartHistoricDataHelper(String beginDate, String endDate, SQLiteDatabase db)
    {
        String piechart_query = "SELECT  SUBSTR(d1.timeStamp,1,10), d2.total as [BIEN SENTADO], count(*) - d2.total as [MAL SENTADO]" +
                "        FROM datos d1 JOIN ( SELECT timeStamp, count(*) total" +
                "                             FROM datos" +
                "                             WHERE bienSentado = 0 AND SUBSTR(timeStamp,1,10) BETWEEN '"+ beginDate +"' AND '"+ endDate +"' GROUP BY SUBSTR(timeStamp,1,10)) d2 " +
                "        ON SUBSTR(d1.timeStamp,1,10) = SUBSTR(d2.timeStamp,1,10) GROUP BY SUBSTR(d1.timeStamp,1,10)";

        Cursor cursor = db.rawQuery(piechart_query, null);
        int bien_sentado = 0;
        int mal_sentado = 0;

        while(cursor.moveToNext()) {
            mal_sentado += cursor.getInt(1);
            bien_sentado += cursor.getInt(2);
        }

        float total_ambos = bien_sentado + mal_sentado;
        float total_bien_sentado = bien_sentado * 100 / total_ambos;
        float total_mal_sentado = 100 - total_bien_sentado;

        entries.add(new Entry(total_bien_sentado, 0));
        entries.add(new Entry(total_mal_sentado, 1));

        labels.add("BIEN SENTADO");
        labels.add("MAL SENTADO");

        cursor.close();
    }

    public ArrayList<Entry> GetEntriesData(SQLiteDatabase db)
    {
        return entries;
    }

    public ArrayList<String> GetLabelsData(SQLiteDatabase db)
    {
        return labels;
    }
}




