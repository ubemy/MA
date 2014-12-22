package com.ma.schiffeversenken.android.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.ma.schiffeversenken.EntityShip;
import com.ma.schiffeversenken.android.controller.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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
	TiledMap map;
	GamePreferences pref;

	public Player(TiledMapTileSet tileSet, TiledMap m) {
		super();
		map = m;
		firstField = new Field(0, tileSet, (TiledMapTileLayer) map.getLayers()
				.get("0"));
		secondField = new Field(1, tileSet, (TiledMapTileLayer) map.getLayers()
				.get("0"));
		// TODO Support moere gameModes...
		this.game = new Game(0, firstField, secondField, false, false);
		
		if (Gdx.files.local("preferences.dat").exists()) {
			System.out.println("GamePreferences Exists. Reading File ...");
			try {
				pref = GamePreferences.readGamePreferences();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out
					.println("GamePreferences does not exist. Creating new Standard GamePreferences ...");
			pref = new GamePreferences();
		}
	}

	public void update(OrthographicCamera camera) {

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			// camera.translate(0, 10);
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			// camera.translate(-10, 0);
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			// camera.translate(0, -10);
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			// camera.translate(+10, 0);
		} else if (Gdx.input.isKeyPressed(Keys.PLUS)) {
			if (camera.zoom > 0.1f)
				camera.zoom = camera.zoom - 0.1f;
		} else if (Gdx.input.isKeyPressed(Keys.MINUS)) {
			if (camera.zoom < 2.0f)
				camera.zoom = camera.zoom + 0.1f;
		}

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
	// @Deprecated
	public void draw(Batch batch) {
		game.draw(batch);
	}

	public void animatedTiles() {
		// Animatad Tiles
		// FrameArray für zwei verschiedene Bilder
		Array<StaticTiledMapTile> frameTiles = new Array<StaticTiledMapTile>(3);

		// Iterieren und holen der Animierten tiles für das FrameArray
		Iterator<TiledMapTile> tiles = map.getTileSets().getTileSet("ships")
				.iterator();
		while (tiles.hasNext()) {
			TiledMapTile tile = tiles.next();
			if (tile.getProperties().containsKey("gunattack")
					&& tile.getProperties().get("gunattack", String.class)
							.equals("1"))
				frameTiles.add((StaticTiledMapTile) tile);
		}

		// Erstellen der Animierten Tile
		AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(1 / 3f,
				frameTiles);

		// 3=attack,2=ships,1=water, 0=land
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("3");
		//
		//
		//

		// iteration über das TileGrid
		for (int x = 0; x < layer.getHeight(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					// Wenn das die gesuchte zelle ist.
					if (cell.getTile().getProperties().containsKey("gunattack")
							&& cell.getTile().getProperties()
									.get("gunattack", String.class).equals("1")) {
						// Animierte Tile der cell zuordnen.
						cell.setTile(animatedTile);
					}
				}
			}
		}

	}

	public Game getGame() {
		return game;
	}

	public static void savePlayer(Player player) throws IOException {
		if(Gdx.files.isLocalStorageAvailable()){
		FileHandle file = Gdx.files.local("data/player.bin");
		try {
			file.writeBytes(serialize(player), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	}

	public static Player readPlayer() throws IOException,
			ClassNotFoundException {
		Player player = null;
		if(Gdx.files.isLocalStorageAvailable()){
		FileHandle file = Gdx.files.local("data/player.bin");
		player = (Player) deserialize(file.readBytes());
		}
		return player;
	}

	/**
	 * Methode dient der serialisierung eines Objectes zu einem ByteArray.
	 * 
	 * @param obj
	 *            Das zu serialisierende Object.
	 * @return ByteArray der das serialiserte Object hält.
	 * @throws IOException
	 */
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

	@Override
	public void write(Json json) {
		// TODO Auto-generated method stub
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		// TODO Auto-generated method stub
	}
}
