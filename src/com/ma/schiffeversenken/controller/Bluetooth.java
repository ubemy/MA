package com.ma.schiffeversenken.controller;

import android.bluetooth.BluetoothAdapter;

public class Bluetooth {
	private BluetoothAdapter bluetoothAdapter;
	
	public Bluetooth(){
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if(bluetoothAdapter == null){
			String hallo="test";
		}
	}
}
