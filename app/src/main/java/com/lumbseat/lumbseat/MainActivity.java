package com.lumbseat.lumbseat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.lumbseat.lumbseat.dataBase.SQLiteConnectionHelper;
import com.lumbseat.lumbseat.utilities.Utilities;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_configuration:
                    intent = new Intent(MainActivity.this, ConfigurationActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_exercises:
                    intent = new Intent(MainActivity.this, ExercisesActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_backup:
                    intent = new Intent(MainActivity.this, BackupActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("LumbSeat");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SQLiteConnectionHelper conn = new SQLiteConnectionHelper(this, Utilities.BASE_DATOS, null, 1);
        //SQLiteConnectionHelper conn = new SQLiteConnectionHelper(this);
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utilities.CAMPO_ID, 1);
        values.put(Utilities.CAMPO_SENS_RESISTIVO_ATRAS_DER, 3.50 );
        values.put(Utilities.CAMPO_SENS_RESISTIVO_ATRAS_IZQ, 3.50);
        values.put(Utilities.CAMPO_SENS_RESISTIVO_ADEL_DER, 3.41);
        values.put(Utilities.CAMPO_SENS_RESISTIVO_ADEL_IZQ, 3.43);
        values.put(Utilities.CAMPO_SENS_DIST_LUMBAR, 1);
        values.put(Utilities.CAMPO_SENS_DIST_CERVICAL, 1);
        values.put(Utilities.CAMPO_BIEN_SENTADO, 1);

        Long idResultante = db.insert(Utilities.TABLA_DATOS, Utilities.CAMPO_ID, values);
        Toast.makeText(getApplicationContext(),"ID: "+ idResultante,Toast.LENGTH_SHORT).show();
        db.close();
    }

}
