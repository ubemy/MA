package com.ma.schiffeversenken.android.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.ma.schiffeversenken.EntityShip;
import com.ma.schiffeversenken.android.controller.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Der Player hält alles zusammen OpenGl/Controller
 * 
 * @author Klaus
 */
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	Game game;
	private Field firstField;
	private Field secondField;

	public Player(ArrayList<EntityShip> playerShips, ArrayList<EntityShip> enemyShips,TextureAtlas a, TiledMapTileLayer mtl) {
		super();
		firstField = new Field(0,playerShips,a,mtl);
		secondField = new Field(1,enemyShips,a,mtl);
		// TODO Support moere gameModes...
		this.game = new Game(0, firstField, secondField, false, false);
	}

	public void update(OrthographicCamera camera) {
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			camera.translate(0, 10);
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			camera.translate(-10, 0);
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			camera.translate(0, -10);
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			camera.translate(+10, 0);
		} else if (Gdx.input.isKeyPressed(Keys.PLUS)) {
			if(camera.zoom>0.1f)
			camera.zoom=camera.zoom-0.1f;
		} else if (Gdx.input.isKeyPressed(Keys.MINUS)) {
			if(camera.zoom<2.0f)
			camera.zoom=camera.zoom+0.1f;
		}

	}

	@Override
	public void write(Json json) {
		// TODO Auto-generated method stub
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		// TODO Auto-generated method stub
	}

	/**
	 * Methode dient der serialisierung eines Objectes zu einem ByteArray.
	 * 
	 * @param obj
	 *            Das zu serialisierende Object.
	 * @return ByteArray der das serialiserte Object hält.
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
	}

	/**
	 * Methode dient der deserialisierung eines Objectes aus einem ByteArray.
	 * 
	 * @param bytes
	 *            ByteArray eines Objectes.
	 * @return deserialisiertes Objekt wird zurückgeliefert.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserialize(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}

	/**
	 * Callback Methode für das Object Player
	 */
	public void dispose() {
		// TODO Auto-generated method stub
		// Maybe serialice object ;)

	}

	public Field getFirstField() {
		return firstField;
	}

	public void setFirstField(Field firstField) {
		this.firstField = firstField;
	}

	public Field getSecondField() {
		return secondField;
	}

	public void setSecondField(Field secondField) {
		this.secondField = secondField;
	}

	/**
	 * Methode zum Zeichnen der Szene
	 * 
	 * @param batch
	 *            SpriteBatch wird fürs Zeichnen übergeben.
	 * @param atlas
	 */
	public void draw(Batch batch, TextureAtlas a) {
		game.draw(batch, a);
	}

}
