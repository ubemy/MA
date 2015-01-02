package com.ma.schiffeversenken.android.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.ma.schiffeversenken.android.AndroidLauncher;
import com.ma.schiffeversenken.android.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class StartGame extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_game);
		
		Button einfach=null, mittel=null, schwer=null;
		
		try{
			
			createButtons(einfach, R.id.button_ki_einfach, getString(R.string.ki_simple), AndroidLauncher.class);
			createButtons(mittel, R.id.button_ki_normal, getString(R.string.ki_normal), AndroidLauncher.class);
			createButtons(schwer, R.id.button_ki_schwer, getString(R.string.ki_difficult), AndroidLauncher.class);
			//testButton();

		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_game, menu);
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
	
	private <E> void createButtons(Button button, int id, final String text, final Class <E> c){
		/*
		 * Buttons erstellen
		 */
		Button startSpielButton = (Button) findViewById(id);
		startSpielButton.setText(text);
		startSpielButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					SharedPreferences sp = getSharedPreferences("Main_Preferences", MODE_MULTI_PROCESS);
					Editor editor = sp.edit();
					editor.putString("ki", text);
					editor.apply();
				
					Intent intent = new Intent(StartGame.this, c);
					intent.putExtra("primaryBTGame", "false");
					intent.putExtra("secondaryBTGame", "false");
					startActivity(intent);
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
	}
}