package com.ma.schiffeversenken;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.ma.schiffeversenken.android.model.Player;

public class GameFieldScreen implements Screen {

	public static final String TITLE = "Schiffeversenken 1.0: ";

	private TiledMap map;
	private TiledMapTileLayer mapTileLayer;
	private TiledMapTileSet tileSetShips;

	// Renderer hält einen SpriteBatch fürs zeichnen bereit
	private OrthogonalTiledMapRenderer renderer;
	private Batch batch;
	public static OrthographicCamera camera;

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

	private float layerX;
	private float layerY;
	private float layerZoom;
	private ArrayList <Boolean> state;// 0=Intro, 1=FullView 2=GameFieldZoom, 3=PlayerShips,4=EnemyShips,5=GameGrid

	// Intro Textur
	private Texture introTexture;
	private TextureRegion introTextureRegion;

	private CameraController controller;

	private GestureDetector gestureDetector;

	private Texture randTexture;

	private TextureRegion randTextureRegion;

	private TextureRegion randTextureRegionUp;

	private TextureRegion randTextureRegionUpRight;

	private boolean gameGridMode=true;

	@Override
	public void show() {
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
		layerX = mapTileLayer.getWidth() * mapTileLayer.getTileWidth() / 2;
		layerY = mapTileLayer.getHeight() * mapTileLayer.getTileHeight() / 2;
		camera.position.set(-layerX, layerY, 0);
		// zoomarichmetik um jede Auflösung zu unterstützen
		float zoomfaktor = ((0.95f * 1920 / h));
		camera.zoom = zoomfaktor;
		layerZoom = camera.zoom;
		camera.update();

		// Controls
		controller = new CameraController(camera, layerX, layerY, layerZoom);
		gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, controller);
		Gdx.input.setInputProcessor(gestureDetector);

		//State initialisieren
		state = new ArrayList<Boolean>();
		for (int i=0;i<6;i++) {
			state.add(new Boolean(false));
		}
		//Intro
		state.set(0,new Boolean(true));
		
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

		// Intro Textur setzen
		introTexture = new Texture(Gdx.files.internal("graphics//Intro.png"));
		introTextureRegion = new TextureRegion(introTexture);
		
		randTexture = new Texture(Gdx.files.internal("graphics//Rand.png"));
		randTextureRegion = new TextureRegion(randTexture);
		randTextureRegion.flip(true, false);
		randTextureRegionUp = new TextureRegion(randTexture);
		randTextureRegionUp.flip(false, true);
		
		randTextureRegionUpRight = new TextureRegion(randTexture);
		randTextureRegionUpRight.flip(true, true);

	}

	@Override
	public void render(float delta) {
		// Gdx.app.log(TITLE, "render(...)");
		player.update(camera);
		controller.update(state);
		camera.update();
		// Render the things after show()
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);

		//Dem Renderer die camera übergeben.
		renderer.setView(camera);
		renderer.render();

		//Animation bg
		renderer.render(ships);
		
		// Draw Stuff
		//Randgebiete
		batch.begin();
		batch.draw(introTextureRegion, -layerX * 2, layerY*2);
		batch.draw(introTextureRegion, -layerX * 2, -layerY*2);
		batch.draw(randTextureRegionUp, 0, layerY*2);
		batch.draw(randTextureRegionUpRight, layerX * 2, layerY*2);
		batch.draw(introTextureRegion, 0, -layerY*2);
		batch.draw(introTextureRegion, layerX * 2, -layerY*2);
		batch.draw(randTextureRegion, layerX * 2, 0);
		batch.draw(introTextureRegion, -layerX * 2, 0);
		//Weiter Außerhalb
		//links
		batch.draw(introTextureRegion, -layerX * 2*2, layerY*2);
		batch.draw(introTextureRegion, -layerX * 2*2, 0);
		batch.draw(introTextureRegion, -layerX * 2*2, -layerY*2);
		//rechts
		batch.draw(introTextureRegion, layerX * 2*2, layerY*2);
		batch.draw(introTextureRegion, layerX * 2*2, 0);
		batch.draw(introTextureRegion, layerX * 2*2, -layerY*2);
		//unten
		batch.draw(introTextureRegion, -layerX * 2*2, -layerY*2*2);
		batch.draw(introTextureRegion, -layerX * 2, -layerY*2*2);
		batch.draw(introTextureRegion, 0, -layerY*2*2);
		batch.draw(introTextureRegion, layerX * 2, -layerY*2*2);
		batch.draw(introTextureRegion, layerX * 2*2, -layerY*2*2);
		//oben
		batch.draw(introTextureRegion, -layerX * 2*2, layerY*2*2);
		batch.draw(introTextureRegion, -layerX * 2, layerY*2*2);
		batch.draw(introTextureRegion, 0, layerY*2*2);
		batch.draw(introTextureRegion, layerX * 2, layerY*2*2);
		batch.draw(introTextureRegion, layerX * 2*2, layerY*2*2);
		batch.end();
		
		// Spielfelder
		batch.begin();
		player.draw(batch);
		batch.end();

		// Animation bg
		renderer.render(attack);

		// TODO Animate Fireing some canons and ships getting into position.
		player.animatedTiles();

		if (state.get(5)) {
			// render Objects
			// Wie renderer.setView(camera.combined) Transformieren der Shapes
			// auf
			// die cam position/koordinaten.
			sr.setProjectionMatrix(camera.combined);
			sr.setColor(Color.GRAY);
			// RectangleMapObject, CircleMapObject,
			// PolylineMapObject, EllipseMapObject, PolygonMapObject.
			for (MapObject object : map.getLayers().get("GameField")
					.getObjects()) {
				if (object instanceof RectangleMapObject) {
					Rectangle rt = ((RectangleMapObject) object).getRectangle();
					sr.begin(ShapeType.Line);
					sr.rect(rt.x, rt.y, rt.width, rt.height);
					sr.end();
				}
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
		introTexture.dispose();
		// ship.getTexture().dispose();// Wichtig texturen dispose()

	}

}
