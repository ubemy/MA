package com.ma.schiffeversenken;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	private EntityShip ship;

	// Texturen
	private TextureAtlas atlas;

	@Override
	public void show() {

		// graphics High and width
		h = Gdx.graphics.getHeight();
		w = Gdx.graphics.getWidth();
		// Wegen resize Aufruf nach erstellen ist die übergabe von w/h unnötig
		camera = new OrthographicCamera();
		// camera.position.set(0, 0, 0);
		// camera.lookAt(0, 0, 0);
		// camera.translate(h/2, h/2+size, h*4);
		// camera.near = 0.1f;
		// camera.far = h*4;

		// Ambiente
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f,
				0.8f, 0.8f, 1.0f));

		// Tiled Maps laden um diese zu nutzen
		map = new TmxMapLoader().load("maps/schiffeversenken.tmx");

		// renderer kann man noch ein skalierungsfaktor mitgeben.
		renderer = new OrthogonalTiledMapRenderer(map);

		// SpriteBatch vom Renderer
		batch = renderer.getSpriteBatch();
		// Creating Player and Game Fields
		// ...TODO
		player = new Player(atlas);

		// Get Texture Pack
		atlas = new TextureAtlas(Gdx.files.internal("graphics//textures.atlas"));
		// Shiff Zeichnen
		ship = new EntityShip(new Sprite(atlas.findRegion("shipmiddle")),
				(TiledMapTileLayer) map.getLayers().get("0"));
//		ship.setPosition(11 * ship.getCollisionLayer().getTileWidth(),
//				38 * ship.getCollisionLayer().getTileHeight());
		ship.setPosition(11 * ship.getCollisionLayer().getTileWidth(),
				(ship.getCollisionLayer().getHeight()-38) * ship.getCollisionLayer().getTileHeight());

	}

	@Override
	public void render(float delta) {
		// Render the things after show()
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// dem Renderer die camera übergeben.
		renderer.setView(camera);
		renderer.render();

		// Draw Stuff
		batch.begin();
		ship.draw(batch);
		batch.end();
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
		player.dispose();// TODO Rekursiv alle texturen
		batch.dispose();
		map.dispose();
		ship.getTexture().dispose();// Wichtig texturen dispose()

	}

}
