package com.ma.schiffeversenken;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.ma.schiffeversenken.android.model.Player;

public class MyGdxGameField extends Game {

	private boolean primaryBTGame, secondaryBTGame;
	public MyGdxGameField(boolean primaryBTGame, boolean secondaryBTGame){
		this.primaryBTGame = primaryBTGame;
		this.secondaryBTGame = secondaryBTGame;
	}
	
	/**
	 * create initialisiert das Grundgerüst für das Zeichnen mit OpenGL ES 2.0
	 * es wird nur einmal ausgeführt
	 */
	@Override
	public void create() {
		//Wenn spiel erstellt wird wollen wir den Screen setzen
		setScreen(new GameFieldScreen(primaryBTGame, secondaryBTGame));
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

}