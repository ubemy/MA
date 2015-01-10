package com.ma.schiffeversenken;

import com.badlogic.gdx.Game;
import com.ma.schiffeversenken.android.AndroidLauncher;

public class MyGdxGameField extends Game {
	
	private AndroidLauncher androidLauncher;
	private boolean primaryBTGame, secondaryBTGame;

	public MyGdxGameField(boolean primaryBTGame, boolean secondaryBTGame,AndroidLauncher a){
		this.primaryBTGame = primaryBTGame;
		this.secondaryBTGame = secondaryBTGame;
		this.androidLauncher=a;
	}
	
	/**
	 * create initialisiert das Grundgerüst für das Zeichnen mit OpenGL ES 2.0
	 * es wird nur einmal ausgeführt
	 */
	@Override
	public void create() {
		//Wenn spiel erstellt wird wollen wir den Screen setzen
		setScreen(new GameFieldScreen(false,this,primaryBTGame, secondaryBTGame));
	}
	
	/**
	 * Wird beim Neustart aufgerufen.
	 * @param newGame true, wenn neues spiel
	 */
	public void create(boolean restart) {
		//Wenn spiel erstellt wird wollen wir den Screen setzen
		setScreen(new GameFieldScreen(restart,this,primaryBTGame, secondaryBTGame));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
		// TODO SPEICHERN
	}

	@Override
	public void resume() {
		super.resume();
		// TODO LADEN

	}
	
	public AndroidLauncher getAndroidLauncher(){
		return this.androidLauncher;
	}
	
}