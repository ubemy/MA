package com.ma.schiffeversenken;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ma.schiffeversenken.android.model.Player;

public class GameFieldScreen implements Screen {

	public static final String TITLE = "Schiffeversenken 1.0: ";

	private TiledMap map;
	private TiledMapTileLayer mapTileLayer;
	private TiledMapTileSet tileSetShips;
	
	// Renderer hält einen SpriteBatch fürs zeichnen bereit
	private OrthogonalTiledMapRenderer renderer;
	private Batch batch;
	private OrthographicCamera camera;

	// höhe und breite
	private int h;
	private int w;

	// Einheitsgröße der Texturen
	public static final int size = 64;

	// Environment für Lichtefekkte
	private Environment environment;

	// Spiellogic
	private Player player;
	private ArrayList<EntityShip> tilesPlayerShips;
	private ArrayList<EntityShip> tilesEnemyShips;
	private Iterator<EntityShip> tileIterator;

	// TEstzwecke
	private EntityShip ship;

	// Texturen
	private TextureAtlas atlas;
	private int[] background = { 0 }, water = { 1 }, ships = { 2 },
			attack = { 3 };

	// ShapeRenderer für GridObjekte
	private ShapeRenderer sr;




	@Override
	public void show() {
		// Gdx.app.log(TITLE, "show()");
		// Tiled Maps,Layer und tileSet laden um diese zu nutzen
		map = new TmxMapLoader().load("maps/map.tmx");
		mapTileLayer = (TiledMapTileLayer) map.getLayers().get("0");
		tileSetShips = map.getTileSets().getTileSet("ships");

		// Get Texture Pack TODO Texturen aus TiledMap holen.
		atlas = new TextureAtlas(Gdx.files.internal("graphics//textures.atlas"));

		// graphics High and width
		h = Gdx.graphics.getHeight();
		w = Gdx.graphics.getWidth();
		// Wegen resize Aufruf nach erstellen ist die übergabe von w/h unnötig
		camera = new OrthographicCamera();
		// camera.zoom 1.4 camera.position.x 510.0 camera.position.y 710.0
		camera.viewportWidth = w;
		camera.viewportHeight = h;
		float layerX = mapTileLayer.getWidth() * mapTileLayer.getTileWidth()
				/ 2;
		float layerY = mapTileLayer.getHeight() * mapTileLayer.getTileHeight()
				/ 2;
		camera.position.set(layerX, layerY, 0);
		// zoomarichmetik um jede Auflösung zu unterstützen
		float zoomfaktor = ((0.95f * 1920 / h));
		System.out.println(zoomfaktor);

		camera.zoom = zoomfaktor;
		camera.update();

		// Ambiente
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f,
				0.8f, 0.8f, 1.0f));

		// renderer kann man noch ein skalierungsfaktor mitgeben.
		renderer = new OrthogonalTiledMapRenderer(map);

		// SpriteBatch vom Renderer
		batch = renderer.getSpriteBatch();

		player = new Player(tileSetShips, map);

		// Neuer ShapeRenderer um Objektlayer zu zeichnen fürs GameGrid
		sr = new ShapeRenderer();

		// Shiff Zeichnen TODO löschen den Test code
	
//		Sprite sprite2 = new Sprite(atlas.findRegion("shipmiddle"));
//		sprite2.setSize(size, size);
//		sprite2.setOrigin(sprite2.getWidth(), sprite2.getHeight());
//		ship = new EntityShip(tileSetShips.iterator().next().getTextureRegion().getTexture(), 
//				new Vector2(1*mapTileLayer.getTileWidth(),5*mapTileLayer.getTileHeight()), 
//				new Vector2(size,size), mapTileLayer);
		// ENDE Shiff Zeichnen TODO löschen den Test code

		// Tiles Array um Shiffe auf Ebene zu projezieren.
		//tilesPlayerShips TODO
		tilesEnemyShips = player.getSecondField().getTiledShips();
	
	}

	@Override
	public void render(float delta) {
		// Gdx.app.log(TITLE, "render(...)");
		player.update(camera);
		camera.update();
		// Render the things after show()
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		// dem Renderer die camera übergeben.
		renderer.setView(camera);
		renderer.render();

		// Animation bg
		renderer.render(ships);

		// Draw Stuff
		batch.begin();
		player.draw(batch, atlas);
//		ship.render(batch);

		
		batch.end();

		// Animation bg
		renderer.render(attack);

		// TODO Animate Fireing some canons and ships getting into position.
		player.animatedTiles();

		// render Objects
		// Wie renderer.setView(camera.combined) Transformieren der Shapes auf
		// die cam position.
		sr.setProjectionMatrix(camera.combined);
		sr.setColor(Color.GRAY);

		// RectangleMapObject, CircleMapObject,
		// PolylineMapObject, EllipseMapObject, PolygonMapObject.
		for (MapObject object : map.getLayers().get("GameField").getObjects()) {
			if (object instanceof RectangleMapObject) {
				Rectangle rt = ((RectangleMapObject) object).getRectangle();
				sr.begin(ShapeType.Line);
				sr.rect(rt.x, rt.y, rt.width, rt.height);
				sr.end();
			}
		}

	}

	@Override
	public void resize(int width, int height) {
		// Gdx.app.log(TITLE, "resize(...)");
		// TODO globale variable oder referenz auf map größe
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		// camera.position.set(width/2f, height/2f, 0);
		// camera.zoom=0.5f;
		camera.update();
	}

	@Override
	public void hide() {
		// Gdx.app.log(TITLE, "hide()");
		// TODO Auto-generated method stub
		dispose();

	}

	@Override
	public void pause() {
		// Gdx.app.log(TITLE, "pause()");
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// Gdx.app.log(TITLE, "resume()");
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// Gdx.app.log(TITLE, "dispose()");
		atlas.dispose();
		player.dispose();// TODO Rekursiv alle texturen
		batch.dispose();
		map.dispose();
//		ship.getTexture().dispose();// Wichtig texturen dispose()

	}

}
