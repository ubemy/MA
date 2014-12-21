package com.ma.schiffeversenken.android.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ma.schiffeversenken.android.model.GamePreferences;
import com.ma.schiffeversenken.android.R;

public class GamePreferencesActivity extends Activity implements
		OnClickListener {

	private GamePreferences mGamePreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_preferences);

		// If activity is being recreated due to a configuration change, restore
		// saved game preferences
		if (savedInstanceState != null) {
			mGamePreferences = savedInstanceState
					.getParcelable(GamePreferences.GAME_PREFERENCES_TAG);
		}

		// If the bundle didn't contain any game preference, creates a new one
		if (mGamePreferences == null) {
			mGamePreferences = new GamePreferences();
		}

		((Button) findViewById(R.id.zerstoerer_minus)).setOnClickListener(this);
		((Button) findViewById(R.id.zerstoerer_plus)).setOnClickListener(this);
		((Button) findViewById(R.id.schlachtschiff_minus))
				.setOnClickListener(this);
		((Button) findViewById(R.id.schlachtschiff_plus))
				.setOnClickListener(this);
		((Button) findViewById(R.id.uboot_minus)).setOnClickListener(this);
		((Button) findViewById(R.id.uboot_plus)).setOnClickListener(this);
		((Button) findViewById(R.id.kreuzer_minus)).setOnClickListener(this);
		((Button) findViewById(R.id.kreuzer_plus)).setOnClickListener(this);
		((Button) findViewById(R.id.schwierigkeitstufe_minus))
				.setOnClickListener(this);
		((Button) findViewById(R.id.schwierigkeitstufe_plus))
				.setOnClickListener(this);

		drawGamePreferences();
	}

	/**
	 * Hilfsmethode um die Enstellungen zu aktualisieren. TODO
	 */
	public void drawGamePreferences() {

		((TextView) findViewById(R.id.zerstoerer_size)).setText(Integer
				.toString(mGamePreferences.getZerstoererNumber()));
		((TextView) findViewById(R.id.schlachtschiff_size)).setText(Integer
				.toString(mGamePreferences.getSchlachtschiffNumber()));
		((TextView) findViewById(R.id.uboot_size)).setText(Integer
				.toString(mGamePreferences.getUbootNumber()));
		((TextView) findViewById(R.id.kreuzer_size)).setText(Integer
				.toString(mGamePreferences.getKreuzerNumber()));
		((TextView) findViewById(R.id.schwierigkeitstufe_size)).setText(Integer
				.toString(mGamePreferences.getSchwierigkeitStufe()));
	}

	// Saves game preferences for activity recreation
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelable(GamePreferences.GAME_PREFERENCES_TAG,
				mGamePreferences);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.zerstoerer_minus:
			if (mGamePreferences.getZerstoererNumber() > GamePreferences.MIN_ZERSTOERER_NUMBER) {
				mGamePreferences.setZerstoererNumber(mGamePreferences
						.getZerstoererNumber() - 1);
			}
			((TextView) findViewById(R.id.zerstoerer_size)).setText(Integer
					.toString(mGamePreferences.getZerstoererNumber()));
			break;

		case R.id.zerstoerer_plus:
			if (mGamePreferences.getZerstoererNumber() < GamePreferences.MAX_ZERSTOERER_NUMBER) {
				mGamePreferences.setZerstoererNumber(mGamePreferences
						.getZerstoererNumber() + 1);
			}
			((TextView) findViewById(R.id.zerstoerer_size)).setText(Integer
					.toString(mGamePreferences.getZerstoererNumber()));
			break;

		case R.id.schlachtschiff_minus:
			if (mGamePreferences.getSchlachtschiffNumber() > GamePreferences.MIN_SCHLACHTSCHIFF_NUMBER) {
				mGamePreferences.setSchlachtschiffNumber(mGamePreferences
						.getSchlachtschiffNumber() - 1);
			}
			((TextView) findViewById(R.id.schlachtschiff_size)).setText(Integer
					.toString(mGamePreferences.getSchlachtschiffNumber()));
			break;

		case R.id.schlachtschiff_plus:
			if (mGamePreferences.getSchlachtschiffNumber() < GamePreferences.MAX_SCHLACHTSCHIFF_NUMBER) {
				mGamePreferences.setSchlachtschiffNumber(mGamePreferences
						.getSchlachtschiffNumber() + 1);
			}
			((TextView) findViewById(R.id.schlachtschiff_size)).setText(Integer
					.toString(mGamePreferences.getSchlachtschiffNumber()));
			break;

		case R.id.uboot_minus:
			if (mGamePreferences.getUbootNumber() > GamePreferences.MIN_UBOOT_NUMBER) {
				mGamePreferences.setUbootNumber(mGamePreferences
						.getUbootNumber() - 1);
			}
			((TextView) findViewById(R.id.uboot_size)).setText(Integer
					.toString(mGamePreferences.getUbootNumber()));
			break;

		case R.id.uboot_plus:
			if (mGamePreferences.getUbootNumber() < GamePreferences.MAX_UBOOT_NUMBER) {
				mGamePreferences.setUbootNumber(mGamePreferences
						.getUbootNumber() + 1);
			}
			((TextView) findViewById(R.id.uboot_size)).setText(Integer
					.toString(mGamePreferences.getUbootNumber()));
			break;

		case R.id.kreuzer_minus:
			if (mGamePreferences.getKreuzerNumber() > GamePreferences.MIN_KREUZER_NUMBER) {
				mGamePreferences.setKreuzerNumber(mGamePreferences
						.getKreuzerNumber() - 1);
			}
			((TextView) findViewById(R.id.kreuzer_size)).setText(Integer
					.toString(mGamePreferences.getKreuzerNumber()));
			break;

		case R.id.kreuzer_plus:
			if (mGamePreferences.getKreuzerNumber() < GamePreferences.MAX_KREUZER_NUMBER) {
				mGamePreferences.setKreuzerNumber(mGamePreferences
						.getKreuzerNumber() + 1);
			}
			((TextView) findViewById(R.id.kreuzer_size)).setText(Integer
					.toString(mGamePreferences.getKreuzerNumber()));
			break;

		case R.id.schwierigkeitstufe_minus:
			if (mGamePreferences.getSchwierigkeitStufe() > GamePreferences.MIN_SCHWIERIGKEIT_NUMBER) {
				mGamePreferences.setSchwierigkeitStufe(mGamePreferences
						.getSchwierigkeitStufe() - 1);
			}
			((TextView) findViewById(R.id.schwierigkeitstufe_size))
					.setText(Integer.toString(mGamePreferences
							.getSchwierigkeitStufe()));
			break;

		case R.id.schwierigkeitstufe_plus:
			if (mGamePreferences.getSchwierigkeitStufe() < GamePreferences.MAX_SCHWIERIGKEIT_NUMBER) {
				mGamePreferences.setSchwierigkeitStufe(mGamePreferences
						.getSchwierigkeitStufe() + 1);
			}
			((TextView) findViewById(R.id.schwierigkeitstufe_size))
					.setText(Integer.toString(mGamePreferences
							.getSchwierigkeitStufe()));
			break;

		// case R.id.Start_Spiel_Button:
		//
		// finish();
		//
		// break;

		case R.id.start_button_preferences:

			if (checkMinimumShips()) {
				Intent intent = new Intent(getApplicationContext(),
						ShipConfigurationActivity.class);
				intent.putExtra(GamePreferences.GAME_PREFERENCES_TAG,
						mGamePreferences);

				startActivity(intent);
			}

			break;

		default:
			break;
		}
	}

	private boolean checkMinimumShips() {

		if ((mGamePreferences.getZerstoererNumber()
				+ mGamePreferences.getSchlachtschiffNumber()
				+ mGamePreferences.getUbootNumber() + mGamePreferences
					.getKreuzerNumber()) > 0) {
			return true;
		} else {

			return false;
		}
	}
}
