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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lumbseat.lumbseat.MainActivity;
import com.lumbseat.lumbseat.R;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothConnection extends Activity {

    ArrayList<String> mArrayAdapter = new ArrayList<String>();
    Context contexto;
    ListView deviceList;

    public BluetoothConnection(Context context, ListView devicelist) {

        contexto = context;
        deviceList = devicelist;

        //Consulta de dispositivos sincronizados
        Set<BluetoothDevice> pairedDevices = MainActivity.mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }else{
            showToastMethod(context,"NO HAY DISPOSITIVOS ASOCIADOS");
        }

        final ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1, mArrayAdapter);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            showToastMethod(contexto,address);
            BluetoothDevice device = MainActivity.mBluetoothAdapter.getRemoteDevice(address);
            ConnectThread cThread = new ConnectThread(device);
            cThread.start();
        }
    };

    public static void showToastMethod(Context context,String msj) {
        Toast.makeText(context, msj, Toast.LENGTH_SHORT).show();
    }

}
