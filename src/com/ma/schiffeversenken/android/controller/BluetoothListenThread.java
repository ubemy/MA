package com.ma.schiffeversenken.android.controller;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

/**
 * Ein Thread der auf einkommende Verbindungsanfragen wartet (Server)
 * @author Maik Steinborn 
 */
public class BluetoothListenThread extends Thread{
	private final BluetoothServerSocket mmServerSocket;

	/**
	 * Erstellt ein BluetoothListenThread Objekt
	 * @param bluetoothAdapter Der Bluetooth Adapter des Gereats
	 */
	public BluetoothListenThread(BluetoothAdapter bluetoothAdapter){
		// Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            //tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Schiffeversenken", UUID.randomUUID());
        	tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Schiffeversenken", UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
        } catch (IOException e) { }
        mmServerSocket = tmp;
	}
	
	public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
                manageConnectedSocket(socket);
                try {
					mmServerSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
                break;
            }
        }
    }

	private void manageConnectedSocket(BluetoothSocket mmSocket) {
    	BluetoothConnectedThread btConnectedThread = new BluetoothConnectedThread(mmSocket);
    	btConnectedThread.start();
	}
	
    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }

}
