package com.ma.schiffeversenken.android.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ma.schiffeversenken.android.view.CreateMultiplayerGame;
import com.ma.schiffeversenken.android.view.VisitMultiplayerGame;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

/**
 * Wird aufgerufen, wenn eine Verbindung hergestellt wurde.
 * Wird auf Server- und Clientseite verwendet
 * @author Maik Steinborn
 */
public class BluetoothConnectedThread extends Thread {
	private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Game game;
    VisitMultiplayerGame vmgClass;
    CreateMultiplayerGame cmgClass;
    BluetoothAdapter bluetoothAdapter;

    /**
     * Erstellt ein BluetoothConnectedThread Objekt
     * @param socket ?? 
     */
    public BluetoothConnectedThread(BluetoothSocket socket, VisitMultiplayerGame vmgClass, CreateMultiplayerGame cmgClass, BluetoothAdapter bluetoothAdapter, Game game) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.vmgClass = vmgClass;
        this.cmgClass = cmgClass;
        this.bluetoothAdapter = bluetoothAdapter;
        this.game = game;
 
        this.game.setConnectedThread(this);
        
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
 
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    	sendHello();
    }
 
    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()
 
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI activity
                /*mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                        .sendToTarget();*/
                
                String readMsg = new String(buffer, 0, bytes);
                String attackString = "_ATTACK_";
                String welcomeString = "_HELLO_";
                String returnString = "_RETURN_";
                if(readMsg.startsWith(attackString)){
                	game.secondGamerAngriff(Integer.parseInt(readMsg.substring(readMsg.indexOf(attackString) + attackString.length() + 1))); 
                }
                else if(readMsg.startsWith(welcomeString)){
                	String helloString = "Erfolgreich verbunden mit " + readMsg.substring(welcomeString.length());
                	if(vmgClass != null){
                		this.vmgClass.showToast(helloString);
                	}
                	else{
                		this.cmgClass.showToast(helloString);
                	}
                }
                else if(readMsg.startsWith(returnString)){
                	boolean returnAttackHit = Boolean.parseBoolean(readMsg.substring(readMsg.indexOf(returnString) + returnString.length() + 1, returnString.indexOf("_", returnString.indexOf("_" + 1))));                	
                	boolean returnShipDestroyed = Boolean.parseBoolean(readMsg.substring(readMsg.indexOf(returnString) + returnString.length() + 1, returnString.indexOf("_", returnString.indexOf("_" + 1))));;
                	
                	game.setReturnValues(returnAttackHit, returnShipDestroyed);
                }
            } catch (IOException e) {
                break;
            }
        }
    }
 
    private void sendHello(){
    	String sendString = new String("_HELLO_" + bluetoothAdapter.getName());
    	write(sendString.getBytes());
    }
    
    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { 
        	e.printStackTrace();
        }
    }
 
    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}
