package com.ma.schiffeversenken.android.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.ma.schiffeversenken.android.view.CreateMultiplayerGame;
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
	private List<BluetoothDevice> allDevices;
	
	
	/**
	 * Erzeugt ein neues Bluetooth Objekt
	 */
	public Bluetooth(){
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		allDevices = new ArrayList();
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
		pairedDevices = bluetoothAdapter.getBondedDevices();	
		
		for(Iterator<BluetoothDevice> it = pairedDevices.iterator(); it.hasNext();){
			allDevices.add(it.next());
		}
		
		return pairedDevices; 
	}
	
	public void addPairedDevices(Set<BluetoothDevice> devices){
		for(Iterator<BluetoothDevice> it = devices.iterator(); it.hasNext();){
			pairedDevices.add(it.next());
		}
	}
	
	public void addPairedDevice(BluetoothDevice device){
		allDevices.add(device);
	}
	
	/**
	 * Server starten
	 */
	public void startServer(CreateMultiplayerGame cmgClass){
		BluetoothListenThread btListenThread = new BluetoothListenThread(bluetoothAdapter, cmgClass);
		btListenThread.start();
	}
	
	/**
	 * Verbindung mit Server aufbauen.
	 * pairedDevice = Der Server, mit dem verbunden werden soll
	 */
	public void connectToServer(String mac){
		BluetoothDevice device = null;
		
		/*
		for(Iterator<BluetoothDevice> it = pairedDevices.iterator(); it.hasNext();){
			if(it.next().getAddress().equals(mac)){
				device = it.next();
				break;
			}
		}*/
		
		for(BluetoothDevice dev : allDevices){
			if(dev.getAddress().equals(mac)){
				device = dev;
				break;
			}
		}
		
		if(device != null){
			BluetoothConnectThread btConnectThread = new BluetoothConnectThread(device, bluetoothAdapter);
			btConnectThread.start();
		}
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
