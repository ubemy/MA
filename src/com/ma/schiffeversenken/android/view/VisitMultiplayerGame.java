package com.ma.schiffeversenken.android.view;

import com.ma.schiffeversenken.android.R;
import com.ma.schiffeversenken.android.controller.Bluetooth;
import com.ma.schiffeversenken.android.controller.BluetoothConnectThread;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Android Activity zur Teilnahme an einem Mehrspieler Spiel
 * @author Maik Steinborn
 */
public class VisitMultiplayerGame extends Activity {
	Bluetooth bt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visit_multiplayer_game);
		
		bt = new Bluetooth();
		int btState = bt.blutoothOK();
		
		if(btState == 1){
			//Geraet unterstuetzt kein Bluetooth
			Toast t = Toast.makeText(getApplicationContext(), "Bluetooth auf diesem Ger�t nicht verf�gbar", Toast.LENGTH_LONG);
			t.show();
		}
		else if(btState == 2){
			//Bluetooth ist nicht enabled
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, Bluetooth.REQUEST_ENABLE_BT);
		}
		else if(btState == 0){
			bt.connectToServer();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visit_multiplayer_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
