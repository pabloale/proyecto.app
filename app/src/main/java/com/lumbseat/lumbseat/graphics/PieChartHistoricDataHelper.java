package com.lumbseat.lumbseat.graphics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PieChartHistoricDataHelper {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar today = Calendar.getInstance();
    private Date todayDate = today.getTime();

    private ArrayList<Entry> entries = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();

    public PieChartHistoricDataHelper(SQLiteDatabase db)
    {
        String piechart_query = "SELECT 100 - (d2.total * 100.0 / count(*)) as [BIEN SENTADO], d2.total * 100.0 / count(*) as [MAL SENTADO]" +
                "        FROM datos d1 JOIN ( SELECT timeStamp, count(*) total" +
                "                             FROM datos" +
                "                             WHERE bienSentado = 0 AND SUBSTR(timeStamp,1,10) = '"+ sdf.format(todayDate) +"') d2" +
                "        ON SUBSTR(d1.timeStamp,1,10) = SUBSTR(d2.timeStamp,1,10)";

        Cursor cursor = db.rawQuery(piechart_query, null);

        while(cursor.moveToNext()) {
            entries.add(new Entry(cursor.getFloat(0), 0));
            entries.add(new Entry(cursor.getFloat(1), 1));

            labels.add(cursor.getColumnName(0));
            labels.add(cursor.getColumnName(1));
        }

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



