package com.lumbseat.lumbseat.bluetooth;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.lumbseat.lumbseat.HistoricosActivity;
import com.lumbseat.lumbseat.MainActivity;
import com.lumbseat.lumbseat.R;

public class BluetoothList extends Activity {

    // Message types sent from the BluetoothReadService Handler
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);

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
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Toast.makeText(BluetoothList.this, readMessage, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
