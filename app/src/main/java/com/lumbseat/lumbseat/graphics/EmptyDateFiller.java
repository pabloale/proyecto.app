package com.lumbseat.lumbseat.graphics;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lumbseat.lumbseat.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmptyDateFiller {

    private SQLiteDatabase db;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar today = Calendar.getInstance();
    private Date todayDate = today.getTime();
    private int MAX_DAYS = 7;

    public EmptyDateFiller(SQLiteDatabase db)
    {
        this.db = db;

        int counter = 0;

        while ( counter < MAX_DAYS )
        {
            if( !thereIsDataForDate() )
            {
                addDataForDate();
            }

            minusOneDay();

            counter++;
        }
    }

    private boolean thereIsDataForDate() {

        String query = "SELECT * " +
                "FROM " + Utilities.TABLA_DATOS +
                " WHERE SUBSTR(" + Utilities.CAMPO_TIMESTAMP + ", 1,10) = " + "'"+ sdf.format(todayDate) + "'";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private void minusOneDay() {
        today.add(Calendar.DAY_OF_YEAR, -1);
        todayDate = today.getTime();
    }

    private void addDataForDate() {
        ContentValues values = new ContentValues();

        try {
            values.put(Utilities.CAMPO_ID, 99999);
            values.put(Utilities.CAMPO_TIMESTAMP, sdf.format(todayDate));
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ATRAS_DER, 0 );
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ATRAS_IZQ, 0);
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ADEL_DER, 0);
            values.put(Utilities.CAMPO_SENS_RESISTIVO_ADEL_IZQ, 0);
            values.put(Utilities.CAMPO_SENS_DIST_LUMBAR, 0);
            values.put(Utilities.CAMPO_SENS_DIST_CERVICAL, 0);
            values.put(Utilities.CAMPO_BIEN_SENTADO, 0);
            values.put(Utilities.CAMPO_MAL_ABAJO_LEJOS, 0);
            values.put(Utilities.CAMPO_MAL_ARRIBA_LEJOS, 0);
            values.put(Utilities.CAMPO_MAL_SENTADO_DER, 0);
            values.put(Utilities.CAMPO_MAL_SENTADO_IZQ, 0);

            db.insert(Utilities.TABLA_DATOS, Utilities.CAMPO_ID, values);

        } catch (Exception e) {

        }
    }
}
