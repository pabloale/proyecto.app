package com.lumbseat.lumbseat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ConfigPesoActivity extends Activity {

    private int peso = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_peso);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(ConfigPesoActivity.this);
        EditText edit = (EditText)findViewById(R.id.editText);
        Button btnIngresar = findViewById(R.id.btnIngresar);
        Intent intent = new Intent(ConfigPesoActivity.this, MainActivity.class);

        setOnClick(intent, edit, btnIngresar, myPreferences);
        peso = myPreferences.getInt("PESO", 0);
        if(peso > 0){
            startActivity(intent);
        }

    }

    private void setOnClick(final Intent intent, final EditText edit, final Button btnIngresar, final SharedPreferences myPreferences){
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valor = edit.getText().toString();
                if(!valor.isEmpty()){
                    peso = Integer.parseInt(valor);
                    if(peso > 0 && peso < 200){
                        SharedPreferences.Editor myEditor = myPreferences.edit();
                        myEditor.putInt("PESO",peso);
                        myEditor.commit();
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Peso no admitido",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Debe ingresar el peso para poder continuar",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
