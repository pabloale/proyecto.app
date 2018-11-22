package com.lumbseat.lumbseat.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lumbseat.lumbseat.ConfigurationActivity;
import com.lumbseat.lumbseat.MainActivity;
import com.lumbseat.lumbseat.R;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothConnection extends Activity {

    ArrayList<String> mArrayAdapter = new ArrayList<String>();
    Context contexto;
    ListView deviceList;
    public BluetoothDevice mDevice;
    Handler mHandler;

    public BluetoothConnection() {}

    public BluetoothConnection(Context context, ListView devicelist, Handler handler) {

        mHandler = handler;
        contexto = context;
        deviceList = devicelist;

        //Consulta de dispositivos sincronizados
        Set<BluetoothDevice> pairedDevices = ConfigurationActivity.mBluetoothAdapter.getBondedDevices();
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
            mDevice = ConfigurationActivity.mBluetoothAdapter.getRemoteDevice(address);

            BluetoothSerialService bss = new BluetoothSerialService(contexto,mHandler);
            try {
                bss.start();
                bss.connect(mDevice);
                if(bss.getState() == bss.STATE_CONNECTING){
                    showToastMethod(contexto,"Conectado con dispositivo " + info);
                }else{
                    showToastMethod(contexto,"No se pudo conectar con el dispositivo " + info);
                }
            }catch(Exception ex){
                showToastMethod(contexto,ex.getMessage());
            }
        }
    };

    public static void showToastMethod(Context context,String msj) {
        Toast.makeText(context, msj, Toast.LENGTH_SHORT).show();
    }

}
