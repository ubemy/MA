package com.ma.schiffeversenken;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ma.schiffeversenken.android.model.Player;

public class GameFieldScreen implements Screen {

	private TiledMap map;
	private TiledMapTileLayer mapTileLayer;
	// Renderer hält einen SpriteBatch fürs zeichnen bereit
	private OrthogonalTiledMapRenderer renderer;
	private Batch batch;
	private OrthographicCamera camera;

	// höhe und breite
	private int h;
	private int w;
	private int hMap = 704;
	private int wMap = 512;

	// Einheitsgröße der Texturen
	public static final int size = 64;

	// Environment für Lichtefekkte
	private Environment environment;

	// Spiellogic
	private Player player;
	private ArrayList<EntityShip> playerShips;
	private ArrayList<EntityShip> enemyShips;

	// TEstzwecke
	private EntityShip ship;

	// Texturen
	private TextureAtlas atlas;
	private int[] background = {0},water={1},ships={2},attack={3};

	@Override
	public void show() {

		// graphics High and width
		h = Gdx.graphics.getHeight();
		w = Gdx.graphics.getWidth();
		// Wegen resize Aufruf nach erstellen ist die übergabe von w/h unnötig
		camera = new OrthographicCamera();
		//camera.zoom 1.4	camera.position.x 510.0 camera.position.y 710.0
		 camera.position.set((h/2)-(size/2), w, 0);
		 System.out.println("w"+w+" h"+h);
		 camera.zoom=1.33f;
		// camera.lookAt(0, 0, 0);
		// camera.translate(h/2, h/2+size, h*4);
		// camera.near = 0.1f;
		// camera.far = h*4;

		// Ambiente
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f,
				0.8f, 0.8f, 1.0f));

		// Tiled Maps laden um diese zu nutzen
		map = new TmxMapLoader().load("maps/map.tmx"); 
		
		mapTileLayer = (TiledMapTileLayer) map.getLayers().get("0");

		// Get Texture Pack
		atlas = new TextureAtlas(Gdx.files.internal("graphics//textures.atlas"));
		

		// renderer kann man noch ein skalierungsfaktor mitgeben.
		renderer = new OrthogonalTiledMapRenderer(map);

		// SpriteBatch vom Renderer
		batch = renderer.getSpriteBatch();

		// Creating Ships for Player and Game Fields
		// ...TODO
		// init ships
		playerShips = new ArrayList<EntityShip>();
		enemyShips = new ArrayList<EntityShip>();
		player = new Player(playerShips, enemyShips, atlas, map);

		// Shiff Zeichnen TODO löschen den auskommentierten code
		 Sprite sprite2 = new Sprite(atlas.findRegion("shipmiddle"));
		 sprite2.setSize(size, size);
		 sprite2.setOrigin(sprite2.getWidth(), sprite2.getHeight());
		 ship = new EntityShip(sprite2, mapTileLayer);
		 ship.setPosition(1 * mapTileLayer.getTileHeight(),
		 1 * mapTileLayer.getTileWidth());

	}

	@Override
	public void render(float delta) {
		player.update(camera);
		camera.update();
		// Render the things after show()
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		// dem Renderer die camera übergeben.
		renderer.setView(camera);
		renderer.render();

		//Animation bg
		renderer.render(ships);
		
		// Draw Stuff
		batch.begin();
		player.draw(batch, atlas);
		ship.draw(batch);
		batch.end();
		
		//Animation bg
		renderer.render(attack);
		
		//TODO Animate Fireing some canons and ships getting into position.
		player.animatedTiles();
		
	}



	@Override
	public void resize(int width, int height) {
		// TODO globale variable oder referenz auf map größe
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		dispose();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		atlas.dispose();
		player.dispose();// TODO Rekursiv alle texturen
		batch.dispose();
		map.dispose();
		ship.getTexture().dispose();// Wichtig texturen dispose()

	}

}
