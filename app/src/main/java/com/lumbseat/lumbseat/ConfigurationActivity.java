package com.lumbseat.lumbseat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class ConfigurationActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ConfigurationActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_configuration:
                    return true;
                case R.id.navigation_exercises:
                    intent = new Intent(ConfigurationActivity.this, ExercisesActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_backup:
                    intent = new Intent(ConfigurationActivity.this, BackupActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    private GoogleApiClient mGoogleApiClient;
    private Button btnSignOut;

    int peso = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        setTitle("LumbSeat");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_configuration);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        peso = myPreferences.getInt("PESO", 0);

        EditText etPeso = (EditText)findViewById(R.id.et1);
        etPeso.setText(""+peso);
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();


        btnSignOut = findViewById(R.id.sign_out_button);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(i);
                    }
                });
            }
        });

        EditText etPeso = (EditText)findViewById(R.id.et1);
        etPeso.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String valorPeso = s.toString();
                if(!valorPeso.isEmpty()){
                    peso = Integer.parseInt(valorPeso);
                }
                SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(ConfigurationActivity.this);
                SharedPreferences.Editor myEditor = myPreferences.edit();
                myEditor.putInt("PESO",peso);
                myEditor.commit();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

}
