package com.lumbseat.lumbseat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class ExercisesActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    intent = new Intent(ExercisesActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_configuration:
                    mTextMessage.setText(R.string.title_configuracion);
                    intent = new Intent(ExercisesActivity.this, ConfigurationActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_exercises:
                    mTextMessage.setText(R.string.title_ejercicios);
                    return true;
                case R.id.navigation_backup:
                    mTextMessage.setText(R.string.title_respaldo);
                    intent = new Intent(ExercisesActivity.this, BackupActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
