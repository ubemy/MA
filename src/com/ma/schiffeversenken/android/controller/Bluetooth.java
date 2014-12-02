package com.ma.schiffeversenken.android.controller;

import java.util.Set;

import com.ma.schiffeversenken.android.view.VisitMultiplayerGame;

import android.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * Verwaltet die Bluetooth Verbindung
 * @author Maik Steinborn
 */
public class Bluetooth extends Activity {
	/**Eine Konstante, die an die Methode startActivityForResult() uebergeben wird,
	 * wenn das Bluetooth nicht eingeschaltet*/
	public static final int REQUEST_ENABLE_BT = 12;
	/**Der Bluetooth Adapter des Geraets*/
	private BluetoothAdapter bluetoothAdapter;
	/**Die verbundenen Geraete*/
	private Set<BluetoothDevice> pairedDevices;
	
	
	/**
	 * Erzeugt ein neues Bluetooth Objekt
	 */
	public Bluetooth(){
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	
	/**Sucht verfuegbare Geraete*/
	public boolean discoverDevices(){
		return bluetoothAdapter.startDiscovery();		
	}

	public void stopDiscoverDevices(){
		bluetoothAdapter.cancelDiscovery();
	}
		
	/**
	 * Gibt die verbundenen Geraete zurueck
	 * @return Die verbundenen Geraete
	 */
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
		BluetoothConnectThread btConnectThread = new BluetoothConnectThread(pairedDevices, bluetoothAdapter);
		btConnectThread.start();
	}
	
	/**
	 * Prueft ob Bluetooth verfuegbar und enabled ist
	 * @return 0=Bluetooth ist eingeschaltet, 1=Geraet unterstuetzt kein Bluetooth, 2=Bluetooth ist disabled
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
