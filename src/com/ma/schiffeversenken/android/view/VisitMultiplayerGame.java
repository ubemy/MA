package com.ma.schiffeversenken.android.view;

import java.util.Set;

import com.ma.schiffeversenken.android.R;
import com.ma.schiffeversenken.android.controller.Bluetooth;
import com.ma.schiffeversenken.android.controller.BluetoothConnectThread;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

/**
 * Android Activity zur Teilnahme an einem Mehrspieler Spiel
 * @author Maik Steinborn
 */
public class VisitMultiplayerGame extends Activity {
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	Bluetooth bt;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;
	private BroadcastReceiver mReceiver;
	
	private final OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final AdapterView<?> pAdapterView, final View pView, final int pPosition, final long pID) {
			// Cancel discovery because it's costly and we're about to connect
			bt.stopDiscoverDevices();

			// Get the device MAC address, which is the last 17 chars in the View
			final String info = ((TextView) pView).getText().toString();
			final String address = info.substring(info.length() - 17);

			// Create the result Intent and include the MAC address
			/*final Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);*/
			//bt.connectToServer();

			// Set result and finish this Activity
			//VisitMultiplayerGame.this.setResult(Activity.RESULT_OK, intent);
			VisitMultiplayerGame.this.finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visit_multiplayer_game);
		bt = new Bluetooth();
		
		try{
			this.mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
			this.mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
			final ListView pairedListView = (ListView) this.findViewById(R.id.pairedDevicesLV);
			pairedListView.setAdapter(this.mPairedDevicesArrayAdapter);
			pairedListView.setOnItemClickListener(this.mDeviceClickListener);

			final ListView newDevicesListView = (ListView) this.findViewById(R.id.detectedDevicesLV);
			newDevicesListView.setAdapter(this.mNewDevicesArrayAdapter);
			newDevicesListView.setOnItemClickListener(this.mDeviceClickListener);

			// Register for broadcasts when a device is discovered
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			this.registerReceiver(this.mReceiver, filter);

			// Register for broadcasts when discovery has finished
			filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			this.registerReceiver(this.mReceiver, filter);
			mReceiver = new BroadcastReceiver() {
			    public void onReceive(Context context, Intent intent) {
			        String action = intent.getAction();
			        // When discovery finds a device
			        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
						final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
							VisitMultiplayerGame.this.mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
						}
					} else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
						VisitMultiplayerGame.this.setProgressBarIndeterminateVisibility(false);
						VisitMultiplayerGame.this.setTitle("Select a device to connect...");
						if (VisitMultiplayerGame.this.mNewDevicesArrayAdapter.getCount() == 0) {
							VisitMultiplayerGame.this.mNewDevicesArrayAdapter.add("No devices found!");
						}
					}
			    }
			};
			
			// Get a set of currently paired devices
			final Set<BluetoothDevice> pairedDevices = bt.getPairedDevices();
			if (pairedDevices.size() > 0) {
				this.findViewById(R.id.pairedDevicesLV).setVisibility(View.VISIBLE);
				for (final BluetoothDevice device : pairedDevices) {
					this.mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				}
			} else {
				this.mPairedDevicesArrayAdapter.add("No devices have been paired!");
			}
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		
		
		/*
		try{
		bt = new Bluetooth();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}*/
		int btState = bt.blutoothOK();
		
		if(btState == 1){
			//Geraet unterstuetzt kein Bluetooth
			Toast t = Toast.makeText(getApplicationContext(), "Bluetooth auf diesem Gerät nicht verfügbar", Toast.LENGTH_LONG);
			t.show();
		}
		else if(btState == 2){
			//Bluetooth ist nicht enabled
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, Bluetooth.REQUEST_ENABLE_BT);
		}
		else if(btState == 0){
			IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			try{
			registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			bt.getPairedDevices();
			bt.discoverDevices();
			
			//bt.connectToServer();
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
