package com.lumbseat.lumbseat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lumbseat.lumbseat.dataBase.SQLiteConnectionHelper;
import com.lumbseat.lumbseat.utilities.Utilities;

public class ExercisesActivity extends Activity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ExercisesActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_configuration:
                    intent = new Intent(ExercisesActivity.this, ConfigurationActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_exercises:
                    return true;
                case R.id.navigation_backup:
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

        MainActivity.contextoActual = this;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_exercises);
        navigation.setItemTextColor(ColorStateList.valueOf(Color.WHITE));

        SQLiteConnectionHelper conn = new SQLiteConnectionHelper(this, Utilities.BASE_DATOS, null, 1);
        SQLiteDatabase db2 = conn.getReadableDatabase();
        String excersises_query = "SELECT (SUM(sentadoIzq) + SUM(sentadoDer))/30.0 [SENTADO COSTADO], (SUM(sentadoLejosAbajo))/30.0 [LEJOS DEBAJO], (SUM(sentadoLejosArriba))/30.0 [LEJOS ARRIBA] " +
                                  "FROM datos ";

        float sentadoCostado = 0;
        float sentadoLejosDebajo = 0;
        float sentadoLejosArriba = 0;

        Cursor cursor = db2.rawQuery(excersises_query, null);
        while(cursor.moveToNext()){
            sentadoCostado = cursor.getFloat(0);
            sentadoLejosDebajo = cursor.getFloat(1);
            sentadoLejosArriba = cursor.getFloat(2);
        }
        cursor.close();
        db2.close();

        ImageView imView1 = (ImageView)findViewById(R.id.imageView1);
        ImageView imView2 = (ImageView)findViewById(R.id.imageView2);
        ImageView imView3 = (ImageView)findViewById(R.id.imageView3);
        ImageView imView4 = (ImageView)findViewById(R.id.imageView4);

        TextView etv1 = (TextView)findViewById(R.id.etv1);
        TextView etv2 = (TextView)findViewById(R.id.etv2);
        TextView etv3 = (TextView)findViewById(R.id.etv3);
        TextView etv4 = (TextView)findViewById(R.id.etv4);

        String srcStretching = "Stretching";
        String srcSentado = "Sentado";
        String srcOficina = "Oficina";
        String srcAvanzado = "Avanzado";

        String textStretching = "Ejercicios típicos de stretching";
        String textSentado = "Para hacer sentado";
        String textOficina = "Stretching de oficina";
        String textAvanzado = "Ejercicios avanzados";

        String linkStretching = "duVd5lW_G9k";
        String linkSentado = "NlHYLMQELp0&feature=youtu.be&t=27";
        String linkOficina = "cXqdtUkmPpQ&feature=youtu.be&t=17";
        String linkAvanzado = "Zg7U9sySQt8&feature=youtu.be&t=14";


        if(sentadoLejosArriba > (sentadoCostado + sentadoLejosDebajo)){
            setProperties(imView1, srcAvanzado, etv1, textAvanzado, linkAvanzado);
            setProperties(imView2, srcSentado, etv2, textSentado, linkSentado);
            setProperties(imView3, srcOficina, etv3, textOficina, linkOficina);
            setProperties(imView4, srcStretching, etv4, textStretching, linkStretching);
        }else if(sentadoCostado > sentadoLejosArriba && sentadoCostado > sentadoLejosDebajo){
            setProperties(imView1, srcSentado, etv1, textSentado, linkAvanzado);
            setProperties(imView2, srcStretching, etv2, srcStretching, linkSentado);
            setProperties(imView3, srcAvanzado, etv3, textAvanzado, linkOficina);
            setProperties(imView4, srcOficina, etv4, textOficina, linkStretching);
        }else if(sentadoLejosDebajo > sentadoLejosArriba && sentadoLejosDebajo > sentadoCostado){
            setProperties(imView1, srcOficina, etv1, textOficina, linkAvanzado);
            setProperties(imView2, srcSentado, etv2, textSentado, linkSentado);
            setProperties(imView3, srcAvanzado, etv3, textAvanzado, linkOficina);
            setProperties(imView4, srcStretching, etv4, textStretching, linkStretching);
        }else if(sentadoLejosArriba > sentadoLejosDebajo && sentadoLejosArriba > sentadoCostado){
            setProperties(imView1, srcStretching, etv1, textStretching, linkAvanzado);
            setProperties(imView2, srcSentado, etv2, textSentado, linkSentado);
            setProperties(imView3, srcOficina, etv3, textOficina, linkOficina);
            setProperties(imView4, srcAvanzado, etv4, textAvanzado, linkStretching);
        }else{
            setProperties(imView1, srcStretching, etv1, textStretching, linkAvanzado);
            setProperties(imView2, srcSentado, etv2, textSentado, linkSentado);
            setProperties(imView3, srcOficina, etv3, textOficina, linkOficina);
            setProperties(imView4, srcAvanzado, etv4, textAvanzado, linkStretching);
        }

        Button btnTruncate = findViewById(R.id.btnTruncate);
        btnTruncate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                truncateDataBase();
            }
        });
    }

    private void setProperties(ImageView imageView, String image, TextView textView, String text, final String linkVideo){
        switch(image){
            case "Stretching":
                imageView.setImageResource(R.drawable.streching);
                break;
            case "Sentado":
                imageView.setImageResource(R.drawable.image_video_2);
                break;
            case "Oficina":
                imageView.setImageResource(R.drawable.image_video_3);
                break;
            case "Avanzado":
                imageView.setImageResource(R.drawable.image_video_4);
                break;
        }
        textView.setText(text);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo(linkVideo);
            }
        });
    }

    private void truncateDataBase() {
        SQLiteConnectionHelper conn = new SQLiteConnectionHelper(this, Utilities.BASE_DATOS, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.delete(Utilities.TABLA_DATOS, null, null);
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}
