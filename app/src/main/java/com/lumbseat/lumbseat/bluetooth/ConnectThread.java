package com.lumbseat.lumbseat.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import com.lumbseat.lumbseat.MainActivity;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private final UUID MY_UUID = UUID.fromString("c5b91ed0-c591-11e8-a355-529269fb1459");
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    //Conexión como cliente
    public ConnectThread(BluetoothDevice device) {

        // Use a temporary object that is later assigned to mmSocket, because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        MainActivity.mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        manageConnectedSocket(mmSocket);
    }

    private void manageConnectedSocket(BluetoothSocket mmSocket) {
        ConnectedThread cnnThread = new ConnectedThread(mmSocket);
        cnnThread.run();
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}