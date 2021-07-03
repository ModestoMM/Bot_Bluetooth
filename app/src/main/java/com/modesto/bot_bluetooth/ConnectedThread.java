package com.modesto.bot_bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


class ConnectedThread extends Thread {
private final InputStream mmInStream;
private final OutputStream mmOutStream;


//creation of the connect thread
public ConnectedThread(BluetoothSocket socket) {
    InputStream tmpIn = null;
    OutputStream tmpOut = null;

    try {
        //Create I/O streams for connection
        tmpIn = socket.getInputStream();
        tmpOut = socket.getOutputStream();
    } catch ( IOException e) { }

    mmInStream = tmpIn;
    mmOutStream = tmpOut;
}


public void run() {
    byte[] buffer = new byte[256];
    int bytes;
}
//write method
public boolean write(String input) {
    boolean envio_exito = true;
    byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
    try {
        mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
    } catch (IOException e) {
        Log.d("ERROR", "metodo write fallo: "+e.getMessage());
        envio_exito = false;
    }
    return envio_exito;
}

}
