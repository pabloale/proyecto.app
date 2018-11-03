package com.lumbseat.lumbseat.graphics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PieChartBadPostureDataHelper {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar today = Calendar.getInstance();
    private Date todayDate = today.getTime();

    private ArrayList<Entry> entries = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();

    public PieChartBadPostureDataHelper(SQLiteDatabase db)
    {
        String piechart_query = "SELECT (SUM(sentadoIzq) + SUM(sentadoDer))/30 [SENTADO COSTADO], (SUM(sentadoLejosAbajo))/30 [LEJOS DEBAJO], (SUM(sentadoLejosArriba))/30 [LEJOS ARRIBA]" +
                " FROM datos" +
                " WHERE SUBSTR(timeStamp,1,10) = '"+ sdf.format(todayDate) +"'";

        Cursor cursor = db.rawQuery(piechart_query, null);

        while(cursor.moveToNext()) {
            entries.add(new Entry(cursor.getFloat(0), 0));
            entries.add(new Entry(cursor.getFloat(1), 1));
            entries.add(new Entry(cursor.getFloat(2), 2));

            labels.add(cursor.getColumnName(0));
            labels.add(cursor.getColumnName(1));
            labels.add(cursor.getColumnName(2));
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



