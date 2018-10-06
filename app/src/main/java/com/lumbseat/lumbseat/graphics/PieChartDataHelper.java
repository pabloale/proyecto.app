package com.lumbseat.lumbseat.graphics;

import android.database.sqlite.SQLiteDatabase;

public class PieChartDataHelper {

    private SQLiteDatabase db;

    public PieChartDataHelper(SQLiteDatabase db)
    {
        this.db = db;
    }
}
