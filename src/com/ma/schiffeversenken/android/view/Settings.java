package com.ma.schiffeversenken.android.view;

import com.ma.schiffeversenken.android.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Android Activity zum �ndern der Spieleinstellungen
 * @author Maik Steinborn
 */
public class Settings extends Activity {
	public static final String SETTINGS_SOUNDOFF = "soundOff";
	public static final String SETTINGS_VIBRATIONOFF = "vibrationOff";
	private static final String trueString = String.valueOf(true);
	private static final String falseString = String.valueOf(false);
	
	/**
	 * Erstellt eine Einstellungen Activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setCheckBoxes();
	}
	
	private void setCheckBoxes(){
		
		final CheckBox lautlosCheckBox = (CheckBox)findViewById(R.id.Lautlos_Checkbox);
		final CheckBox vibrationausCheckBox = (CheckBox)findViewById(R.id.Vibration_aus_Checkbox);

		if(Boolean.parseBoolean(getValue(SETTINGS_SOUNDOFF))){
			lautlosCheckBox.setChecked(true);
		}
		
		if(Boolean.parseBoolean(getValue(SETTINGS_SOUNDOFF))){
			vibrationausCheckBox.setChecked(true);
		}
		
		lautlosCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			/*
			 * Wenn die Lautlos Checkbox betaetigt wird,
			 * wird der entsprechende Eintrag in den Shared Preferences geaendert
			 */
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					setSharedPreferences(SETTINGS_SOUNDOFF, trueString);
				}
				else{
					setSharedPreferences(SETTINGS_SOUNDOFF, falseString);
				}
			}
		});
		
		vibrationausCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			/*
			 * Wenn die Vibration aus Checkbox betaetigt wird,
			 * wird der entsprechende Eintrag in den Shared Preferences geaendert
			 */
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					setSharedPreferences(SETTINGS_VIBRATIONOFF, trueString);
				}
				else{
					setSharedPreferences(SETTINGS_VIBRATIONOFF, falseString);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
	
	public String getValue(String name){
		//Gibt den Wert des entsprechendes Eintrags zurueck
		String ret = null;
		SharedPreferences sp = getSharedPreferences("Main_Preferences", MODE_MULTI_PROCESS);
		ret = sp.getString(name, "");
		return ret;
	}
	
	public void setSharedPreferences(String name, String value){
		//Setzt einen Wert in Shared Preferences
		SharedPreferences sp = getSharedPreferences("Main_Preferences", MODE_MULTI_PROCESS);
		Editor editor = sp.edit();
		editor.putString(name, value);
		editor.apply();
	}
}
