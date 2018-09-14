package com.lumbseat.lumbseat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class BackupActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(BackupActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_configuration:
                    intent = new Intent(BackupActivity.this, ConfigurationActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_exercises:
                    intent = new Intent(BackupActivity.this, ExercisesActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_backup:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        setTitle("LumbSeat");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_backup);

    }

}
