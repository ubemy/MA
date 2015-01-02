package com.ma.schiffeversenken.android;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ma.schiffeversenken.MyGdxGameField;
import com.ma.schiffeversenken.android.controller.BluetoothConnectedThread;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		//setup um GL20 anstelle GL10 zu nutzen.
		cfg.useGLSurfaceView20API18=true;
		//Sichert eine menge Batteriekapazität, wird deaktiviert solange nicht genutzt.
		cfg.useAccelerometer = false;
		cfg.useCompass=false;
		cfg.useImmersiveMode=false;
		cfg.useWakelock=false;
		
		boolean primaryBTGame = Boolean.parseBoolean(getIntent().getExtras().get("primaryBTGame").toString());
		boolean secondaryBTGame = Boolean.parseBoolean(getIntent().getExtras().get("secondaryBTGame").toString());
		
		initialize(new MyGdxGameField(primaryBTGame, secondaryBTGame), cfg);
		
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		BroadcastReceiver mReceiver;
		
		
		mReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();

	            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
	            	AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AndroidLauncher.this);
	            		dialogBuilder.setMessage(getString(R.string.bluetooth_reconnect_question))
	            		.setPositiveButton(getString(R.string.yes), dialogClickListener)
	            		.setNegativeButton(getString(R.string.no), dialogClickListener)
	            		.show();
	               
	            }    
	        }
	    };
	    
		this.registerReceiver(mReceiver, filter);
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    	if(which == DialogInterface.BUTTON_POSITIVE){
	    		BluetoothConnectedThread btcThread = BluetoothConnectedThread.getInstance();
                btcThread.reconnect();
	    	}
	    	else if(which == DialogInterface.BUTTON_NEGATIVE){
	    		finish();
	    	}
	    }
	};
}
