package com.lumbseat.lumbseat.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.lumbseat.lumbseat.BackupActivity;
import com.lumbseat.lumbseat.HistoricosActivity;
import com.lumbseat.lumbseat.MainActivity;
import com.lumbseat.lumbseat.R;
import com.lumbseat.lumbseat.dataBase.SQLiteConnectionHelper;
import com.lumbseat.lumbseat.utilities.Utilities;


public class BluetoothList extends Activity {

    // Message types sent from the BluetoothReadService Handler
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;

    public static int contadorAlerta = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);

        MainActivity.contextoActual = this;

        FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.btnVolver);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BluetoothList.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ListView devicelist = (ListView)findViewById(R.id.listViewDispositivos);
        BluetoothConnection btConn = new BluetoothConnection(BluetoothList.this, devicelist, mHandlerBT);
    }

    // The Handler that gets information back from the BluetoothService
    private final Handler mHandlerBT = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    Toast.makeText(BluetoothList.this, writeBuf.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_READ:

                    if(contadorAlerta == 61)
                        contadorAlerta = 0;

                    contadorAlerta = contadorAlerta + 1;

                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    String[] strParse = readMessage.split(";");

                    SQLiteConnectionHelper conn = new SQLiteConnectionHelper(BluetoothList.this, Utilities.BASE_DATOS, null, 1);
                    SQLiteDatabase db = conn.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        values.put(Utilities.CAMPO_ID, 1);
                        values.put(Utilities.CAMPO_TIMESTAMP, strParse[0]);
                        values.put(Utilities.CAMPO_SENS_RESISTIVO_ATRAS_IZQ, strParse[1] );
                        values.put(Utilities.CAMPO_SENS_RESISTIVO_ATRAS_DER, strParse[2]);
                        values.put(Utilities.CAMPO_SENS_RESISTIVO_ADEL_IZQ, strParse[3]);
                        values.put(Utilities.CAMPO_SENS_RESISTIVO_ADEL_DER, strParse[4]);
                        values.put(Utilities.CAMPO_SENS_DIST_LUMBAR, strParse[5]);
                        values.put(Utilities.CAMPO_SENS_DIST_CERVICAL, strParse[6]);

                        values.put(Utilities.CAMPO_BIEN_SENTADO, ((strParse[7].equals("False")) ? "0" : "1"));
                        values.put(Utilities.CAMPO_MAL_SENTADO_IZQ, ((strParse[8].equals("False")) ? "0" : "1"));
                        values.put(Utilities.CAMPO_MAL_SENTADO_DER, ((strParse[9].equals("False")) ? "0" : "1"));
                        values.put(Utilities.CAMPO_MAL_ABAJO_LEJOS, ((strParse[10].equals("False")) ? "0" : "1"));
                        values.put(Utilities.CAMPO_MAL_ARRIBA_LEJOS, ((strParse[11].equals("False")) ? "0" : "1"));

                        db.insert(Utilities.TABLA_DATOS, Utilities.CAMPO_ID, values);
                    } catch (Exception e) {
                    }
                    db.close();

                    //Toast.makeText(BluetoothList.this,"Insertado en db: " + readMessage, Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    new AlertDialog.Builder(MainActivity.contextoActual)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("¡Arriba! Llevas mucho tiempo sentado. A dar una vuelta!")
                            .setPositiveButton("OK", null).show();
                    break;
            }
        }
    };
}
