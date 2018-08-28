package com.lumbseat.lumbseat;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SignedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);

        String userName = getIntent().getStringExtra("PERSON_NAME");
        //Uri personPhoto = getIntent().getExtras("PERSON_PHOTO");

        TextView tv = findViewById(R.id.textView);
        tv.setText("Bienvenido: " + userName);


    }
}
