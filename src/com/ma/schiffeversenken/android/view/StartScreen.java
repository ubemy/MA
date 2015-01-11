package com.ma.schiffeversenken.android.view;


import com.ma.schiffeversenken.android.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Android Activity zur Auswahl folgender Optionen:
 * Spiel starten, Einstellungen, Hilfe
 * @author Maik Steinborn
 * @author Klaus Schlender
 */
public class StartScreen extends Activity {
	private int buttonWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startscreen);
		
		try{
			
			createButtons(R.id.Start_Spiel_Button, GameMode.class);
			createButtons(R.id.Einstellungen_Button, Settings.class);
			createButtons(R.id.Hilfe_Button, Help.class);
			
			//Beenden Button
			Button exitButton = (Button) findViewById(R.id.Button_Exit);
			RelativeLayout.LayoutParams lParams = (android.widget.RelativeLayout.LayoutParams) exitButton.getLayoutParams();
			lParams.width = buttonWidth;
			exitButton.setOnClickListener(new OnClickListener() {
				
		        @Override
		        public void onClick(View v) {
		        	//Schließen von offenen Meldungen der App und endgültiges Beenden.
		        	NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		        	nManager.cancelAll();
		            finish();
		            System.exit(0);
		        }
		    });
			
			
			SharedPreferences sp = getSharedPreferences("Main_Preferences", MODE_MULTI_PROCESS);
			Editor editor = sp.edit();
			editor.putString("lautlos", "false");
			editor.putString("vibrationaus", "false");
			editor.apply();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private <E> void createButtons(int id, final Class<E> c){
		/*
		 * Buttons erstellen
		 */
		Point p = new Point();
		getWindowManager().getDefaultDisplay().getSize(p);
		buttonWidth = p.x / 2;
		
		Button button = (Button) findViewById(id);
		RelativeLayout.LayoutParams lParams = (android.widget.RelativeLayout.LayoutParams) button.getLayoutParams();
		lParams.width = buttonWidth;
		
		button.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				try{
					Intent intent = new Intent(StartScreen.this, c);
					startActivity(intent);
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.startscreen, menu);
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
