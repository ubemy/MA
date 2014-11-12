package com.ma.schiffeversenken.controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class Bluetooth extends Activity {
	private BluetoothAdapter bluetoothAdapter;
	private final BluetoothManager bluetoothManager;
	
	public Bluetooth(){
		bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();
	}
	
	public int blutoothOK(){
		/*
		 * Prueft ob Bluetooth verfuegbar und enabled ist
		 * 
		 * Bluetooth kann über folgenden Dialog in einer Avtivity eingeschaltet werden:
		 * 
		 * Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	 * startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		 */
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
