package com.lumbseat.lumbseat.dataBase;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.lumbseat.lumbseat.utilities.Utilities;

public class SQLiteConnectionHelper extends SQLiteOpenHelper {

    public SQLiteConnectionHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteConnectionHelper(Context context) {
        super(context, "/data/data/com.lumbseat.lumbseat/databases/bd_datos.db", null, 0);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Este método crea por primera vez las tablas
        db.execSQL(Utilities.CREAR_TABLA_DATOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Este método cuando posteriormente abrimos la app y hay una nueva version
        db.execSQL("DROP TABLE IF EXISTS datos");
        onCreate(db);
    }
}
