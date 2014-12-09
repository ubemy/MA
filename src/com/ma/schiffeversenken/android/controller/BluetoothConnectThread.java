package com.ma.schiffeversenken.android.controller;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import com.ma.schiffeversenken.android.view.CreateMultiplayerGame;
import com.ma.schiffeversenken.android.view.VisitMultiplayerGame;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Verbindet sich mit einem offenem Server Port (Client)
 * @author Maik Steinborn
 */
public class BluetoothConnectThread extends Thread {
	private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter bluetoothAdapter;
    VisitMultiplayerGame vmgClass;
    Game game;
 
    /**
     * Erstellt ein BluetoothConnectThread Objekt
     * @param device Verbundene Geraete
     * @param bluetoothAdapter Der Bluetooth Adapter des Geraetes
     */
    public BluetoothConnectThread(BluetoothDevice device, BluetoothAdapter bluetoothAdapter, VisitMultiplayerGame vmgClass, Game game) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        this.mmDevice = null;
        this.bluetoothAdapter = bluetoothAdapter;
        this.vmgClass = vmgClass;
        this.game = game;
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
        	tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
        } catch (IOException e) { }
        catch(Exception ex){
        	ex.printStackTrace();
        }
        mmSocket = tmp;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
    	bluetoothAdapter.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
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
    	BluetoothConnectedThread btConnectedThread = new BluetoothConnectedThread(mmSocket, vmgClass, null, this.bluetoothAdapter, this.game);
    	btConnectedThread.start();
    	
    	boolean attackHit = true;
    	boolean shipDestroyed = false;
    	btConnectedThread.write((new String("_RETURN_" + Boolean.toString(attackHit) + "_" + Boolean.toString(shipDestroyed))).getBytes());
	}

	/** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}
