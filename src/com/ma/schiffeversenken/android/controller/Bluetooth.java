package com.ma.schiffeversenken.android.controller;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;

/**
 * Bluetooth baut Verbindungen auf
 * und sendet/empfaengt Werte
 * @author Maik Steinborn
 */
public class Bluetooth extends Activity {
	public static final int REQUEST_ENABLE_BT = 12;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothDevice pairedDevice;
	
	/**
	 * Erzeugt ein neues Bluetooth Objekt
	 */
	public Bluetooth(){
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	public boolean discoverDevices(){
		return bluetoothAdapter.startDiscovery();
	}
	
	public Set<BluetoothDevice> getPairedDevices(){
		return bluetoothAdapter.getBondedDevices();
	}
	
	/**
	 * Server starten
	 */
	public void startServer(){
		BluetoothListenThread btListenThread = new BluetoothListenThread(bluetoothAdapter);
		btListenThread.start();
	}
	
	/**
	 * Verbindung mit Server aufbauen.
	 * pairedDevice = Der Server, mit dem verbunden werden soll
	 */
	public void connectToServer(){
		BluetoothConnectThread btConnectThread = new BluetoothConnectThread(pairedDevice, bluetoothAdapter);
		btConnectThread.start();
	}
	
	/**
	 * Prueft ob Bluetooth verfuegbar und enabled ist
	 * 
	 * Bluetooth kann ueber folgenden Dialog in einer Avtivity eingeschaltet werden:
	 * 
	 * Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	 * startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	 */
	public int blutoothOK(){
		int ret = 0;
		
		if(bluetoothAdapter == null){
			//Geraet unterstuetzt kein Bluetooth
			ret = 1;
		}
		else{
			if(!bluetoothAdapter.isEnabled()){
				//Wenn Bluetooth disabled ist, Bluetooth enablen
				ret = 2;
			}
		}
		
		return ret;
	}
}
