package com.lumbseat.lumbseat.graphics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PieChartDataHelper {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar today = Calendar.getInstance();
    private Date todayDate = today.getTime();

    final private String PIECHART_QUERY = "SELECT d2.total * 100.0 / count(*) as [BIEN SENTADO], 1 - (d2.total * 100.0 / count(*)) as [MAL SENTADO]" +
            "        FROM datos d1 LEFT JOIN ( SELECT timeStamp, count(*) total" +
            "                             FROM datos" +
            "                             WHERE bienSentado = 1 AND SUBSTR(timeStamp,1,10) = "+ todayDate +") d2" +
            "        ON SUBSTR(d1.timeStamp,1,10) = SUBSTR(d2.timeStamp,1,10)";
    private ArrayList<Entry> entries = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();

    public PieChartDataHelper(SQLiteDatabase db)
    {
        Cursor cursor = db.rawQuery(PIECHART_QUERY, null);

        int counter = 0;

        while(cursor.moveToNext()) {
            Entry entry = new Entry(cursor.getFloat(1), counter); //Percent
            counter++;
            entries.add(entry);
            labels.add(cursor.getString(0)); //Date
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
