package com.lumbseat.lumbseat.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lumbseat.lumbseat.MainActivity;
import com.lumbseat.lumbseat.R;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothConnection extends Activity {

    public BluetoothConnection(Context context, BluetoothAdapter mBluetoothAdapter) {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            ArrayList<String> mArrayAdapter = new ArrayList<String>();
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            for (String device:mArrayAdapter) {
                showToastMethod(context, device);
            }
        }else{
            showToastMethod(context,"NO HAY DISPOSITIVOS ASOCIADOS");
        }
    }

    public static void showToastMethod(Context context,String msj) {
        Toast.makeText(context, msj, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

    }
}
