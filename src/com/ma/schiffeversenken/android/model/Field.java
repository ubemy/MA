package com.ma.schiffeversenken.android.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.ma.schiffeversenken.EntityShip;
import com.ma.schiffeversenken.GameFieldScreen;
import com.ma.schiffeversenken.MyGdxGameField;

/**
 * Das Spielfeld
 * 
 * @author Maik Steinborn
 * @author Klaus Schlender
 */
public class Field {
	/** Rechte Kante */
	public static final int EDGE_RIGHT = 0;
	/** Obere Kante */
	public static final int EDGE_ABOVE = 1;
	/** Linke Kante */
	public static final int EDGE_LEFT = 2;
	/** Untere Kante */
	public static final int EDGE_BELOW = 3;
	/**
	 * einheiten = Das Spielfeld besteht aus 10x10 Einheiten einheiten[y-Achse
	 * (Zeile)][x-Achse (Spalte)]
	 */
	FieldUnit[][] units = new FieldUnit[10][10];
	/**
	 * Menge von platzierten Schiffen auf diesem Spielfeld
	 */
	Ship[] placedShips;
	/**
	 * typ = Gibt den Typ des Spielfelds an. Eigenes Spielfeld = 0; Gegnerisches
	 * Spielfeld = 1;
	 */
	int typ;

	// Einheitsgröße der Texturen
	float size;
	// graphics High and width
	private int h = Gdx.graphics.getHeight();
	private int w = Gdx.graphics.getWidth();
	private TiledMapTileLayer mapTileLayer;
	// TODO EntityShip or Tile
	private ArrayList<EntityShip> drawShips;
	private Iterator<EntityShip> tileIterator;
	private TreeMap<String, TextureRegion> shipTextures;

	/**
	 * Erstellt ein Field Objekt
	 * 
	 * @param typ
	 *            Typ des Spielfelds (0=Eigenes Spielfeld, 1=Gegnerisches
	 *            Spielfeld)
	 */
	public Field(int typ) {
		try {
			this.typ = typ;
			create();
			createNeighbors();
			createKante();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Überladener Konstruktor erstellt ein Field Objekt
	 * 
	 * @param typ
	 *            Typ des Spielfelds (0=Eigenes Spielfeld, 1=Gegnerisches
	 *            Spielfeld)
	 * @param shipTextures
	 *            SchiffsTexturen aus TiledMapTileSet
	 * @param mtl
	 *            Tilemap Layer aus TiledMapTileLayer
	 */
	public Field(int typ, TiledMapTileSet shipTextures, TiledMapTileLayer mtl) {
		try {
			this.typ = typ;
			this.mapTileLayer = mtl;
			this.size = mtl.getTileHeight();
			drawShips = new ArrayList<EntityShip>();
			getShipTileSetTextures(shipTextures);
			create();
			createNeighbors();
			createKante();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Diese Methode speichert alle Schiffstexturen in einer Treemap
	 * 
	 * @param st
	 *            TiledMapTileSet wo die Texturen gespeichert sind
	 */
	private void getShipTileSetTextures(TiledMapTileSet st) {

		// DEPRECATED START
		// int colTexture = 5;
		// int rowTexture = 6;
		//
		// //Holen der einzelenn Schifftexturen aus der Gesamten Textur in ein
		// 2D Array.
		// Texture tmpST = st.iterator().next().getTextureRegion().getTexture();
		// TextureRegion[][] tmp = TextureRegion.split(tmpST,
		// tmpST.getWidth()/colTexture, tmpST.getHeight()/rowTexture);
		//
		// //Holen der Texturen in ein 1D Array
		// TextureRegion[] shipFrames = new
		// TextureRegion[colTexture*rowTexture];
		// int index = 0;
		// for (int i = 0; i < rowTexture; i++) {
		// for (int j = 0; j < colTexture; j++) {
		// shipFrames[index++] = tmp[i][j];
		// }
		// }
		// DEPRECATED END

		// Setzen der Einzelnen Texturen in eine TreeMap<key,value>.
		this.shipTextures = new TreeMap<String, TextureRegion>();
		Iterator<TiledMapTile> iter = st.iterator();
		TiledMapTile tile;
		while (iter.hasNext()) {
			tile = iter.next();
			// Wenn wir ein Tile mit propertie name haben
			if (tile.getProperties().get("name") != null) {
				shipTextures.put(tile.getProperties().get("name").toString(),
						tile.getTextureRegion());
			}
		}
	}

	/**
	 * Setzt die platzierten Schiffe nach Erstellung.
	 * 
	 * @param ships
	 *            Schiffe, die zu diesem Spielfeld zugeordnet werden sollen
	 */
	public void setShips(Ship[] ships) {
		// Vorher das setzen der Schiffe
		this.placedShips = ships;
		// Für das Zeichnen bekommt jedes Schiff sein EntityShip danach die
		// Koordinaten.
		for (Ship ship : ships) {
			// Finden der geeigneten Textur
			String textureName = "";
			int index = 0;
			if (ship.getSize() > 1) {
				for (FieldUnit unit : ship.location) {
					switch (unit.getShipSegment()) {
					case 0:// 0=Vorderteil
						if (unit.getPlacedShip().getOrientation() == 0
								|| unit.getPlacedShip().getOrientation() == 2) {// Horizontal
							if (!unit.getPlacedShip().isDestroyed()) {
								textureName = "rhb";
							} else {
								textureName = "rhba";
							}
						} else {// Vertikal
							if (!unit.getPlacedShip().isDestroyed()) {
								// textureName = "dvb";//TODO Add upstairs
								// texture
								textureName = "uvb";
							} else {
								// textureName = "dvba";
								textureName = "uvba";
							}
						}
						break;
					case 1:// 1=Mittelteil
						if (unit.getPlacedShip().getOrientation() == 0
								|| unit.getPlacedShip().getOrientation() == 2) {// Horizontal
							if (!unit.getPlacedShip().isDestroyed()) {
								textureName = "rhm";
							} else {
								textureName = "rhma";
							}
						} else {// Vertikal
							if (!unit.getPlacedShip().isDestroyed()) {
								// textureName = "dvm";//TODO Add upstairs
								// texture
								textureName = "uvm";
							} else {
								// textureName = "dvma";
								textureName = "uvma";
							}
						}
						break;
					case 2:// 2=Hinterteil
						if (unit.getPlacedShip().getOrientation() == 0
								|| unit.getPlacedShip().getOrientation() == 2) {// Horizontal
							if (!unit.getPlacedShip().isDestroyed()) {
								textureName = "rhf";
							} else {
								textureName = "rhfa";
							}
						} else {// Vertikal
							if (!unit.getPlacedShip().isDestroyed()) {
								// textureName = "dvf";// TODO Add downstairs
								// texture
								textureName = "uvf";
							} else {
								// textureName = "dvfa";
								textureName = "uvfa";
							}
						}
						break;
					}
					// Hinzufügen von Schiffsteil
					EntityShip tmpShip = new EntityShip(
							shipTextures.get(textureName), new Vector2(
									unit.getXpos(), unit.getYpos()),
							new Vector2(size, size), mapTileLayer);
					ship.setEntityShipDrawUnit(tmpShip);
					drawShips.add(tmpShip);
				}
			} else {// Kleines Schiff
				FieldUnit unit = ship.location[0];

				if (unit.getPlacedShip().getOrientation() == 0) {// Rechts
					if (!ship.isDestroyed()) {
						textureName = "rhk";
					} else {
						textureName = "rhka";
					}
				} else if (unit.getPlacedShip().getOrientation() == 1) {// Oben
					if (!unit.getPlacedShip().isDestroyed()) {
						textureName = "uvk";
					} else {
						textureName = "uvka";
					}
				} else if (unit.getPlacedShip().getOrientation() == 2) {// links
					if (!unit.getPlacedShip().isDestroyed()) {
						// textureName = "lvk";//TODO add left texture
						textureName = "rhk";
					} else {
						// textureName = "lvka";
						textureName = "rhka";
					}
				} else if (unit.getPlacedShip().getOrientation() == 3) {// unten
					if (!unit.getPlacedShip().isDestroyed()) {
						textureName = "dvk";
					} else {
						textureName = "dvka";
					}
				}
				// Hinzufügen von Schiffsteil
				EntityShip tmpShip = new EntityShip(
						shipTextures.get(textureName), new Vector2(
								unit.getXpos(), unit.getYpos()), new Vector2(
								size, size), mapTileLayer);
				ship.setEntityShipDrawUnit(tmpShip);
				drawShips.add(tmpShip);
			}
			

			// DEPRECATED START
			// Setzen der EntityShip für das Schiff
			// EntityShip tmpEntity;
			// System.out.println(textureName);
			// tmpEntity = new EntityShip(shipTextures.get(textureName),
			// new Vector2(0f, 0f), new Vector2(size, size), mapTileLayer);
			//
			// drawShips.add(tmpEntity);
			// ship.setEntityShipDrawUnit(tmpEntity);
			// DEPRECATED START
		

		}
	}
	/**
	 * Gibt die platzierten Schiffe zurueck
	 * 
	 * @return Die platzierten Schiffe
	 */
	public Ship[] getShips() {
		return this.placedShips;
	}

	/**
	 * Gibt die FeldEinheiten zurueck
	 * 
	 * @return Die FeldEinheiten
	 */
	public FieldUnit[][] getFieldUnits() {
		return units;
	}

	/**
	 * Gibt das FeldElement passend zu der ID zurueck
	 * 
	 * @param id
	 *            Die gesuchte ID
	 * @return Das FeldElement passend zu der ID
	 */
	public FieldUnit getElementByID(int id) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (units[i][j].getID() == id)
					return units[i][j];
			}
		}
		return null;
	}

	/**
	 * Erstellt das Spielfeld, das aus 10x10 Feldelementen besteht Hierbei wird
	 * auch die Position je Feld in der Scene übergeben.
	 */
	private void create() {
		int cellPosX = 1;
		int cellPosY = 5;
		if (this.typ == 1) {
			cellPosY += 10;
		}
		int id = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				id++;

				// TODO Testen auf funktion beim Zeichen
				units[i][j] = new FieldUnit(id, (j + cellPosX)
						* mapTileLayer.getTileWidth(), (i + cellPosY)
						* mapTileLayer.getTileHeight());
			}
		}
	}

	/**
	 * Markiert, dass dieses FeldElement an einer Kante platziert ist und in
	 * welcher Richtung die Kante liegt
	 */
	private void createKante() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				FieldUnit e = units[i][j];
				int id = e.getID();
				if (id < 11) {
					// Obere Kante
					if (e.getEdge(1) == 0)
						// e.setEdge(1, 1);
						e.setEdge(1, EDGE_ABOVE);
					else
						// e.setEdge(2, 1);
						e.setEdge(2, EDGE_ABOVE);
				}
				if (id > 89) {
					// Untere Kante
					if (e.getEdge(1) == 0)
						// e.setEdge(1, 2);
						e.setEdge(1, EDGE_BELOW);
					else
						// e.setEdge(2, 2);
						e.setEdge(2, EDGE_BELOW);
				}
				if ((id - (10 * (i + 1))) == 0) {
					// Rechte Kante
					if (e.getEdge(1) == 0)
						// e.setEdge(1, 3);
						e.setEdge(1, EDGE_RIGHT);
					else
						// e.setEdge(2, 3);
						e.setEdge(2, EDGE_RIGHT);
				}
				if ((id - (10 * i)) == 1) {
					// Linke Kante
					if (e.getEdge(1) == 0)
						// e.setEdge(1, 4);
						e.setEdge(1, EDGE_LEFT);
					else
						// e.setEdge(2, 4);
						e.setEdge(2, EDGE_LEFT);
				}
			}
		}
	}

	/**
	 * Weist jedem Feldelement seine direkten Nachbarn zu
	 */
	private void createNeighbors() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				FieldUnit lNeighbor = null, rNeighbor = null, oNeighbor = null, uNeighbor = null;

				if (i > 1 && i < 10) {
					oNeighbor = units[i - 1][j];
				}
				if (i > 0 && i < 9) {
					uNeighbor = units[i + 1][j];
				}

				if (j > 1 && j < 10) {
					lNeighbor = units[i][j - 1];
				}

				if (j > 0 && j < 9) {
					rNeighbor = units[i][j + 1];
				}

				units[i][j].setNeighbors(lNeighbor, rNeighbor, oNeighbor,
						uNeighbor);
			}
		}
	}

	/**
	 * Iteriert über alle Feldelemente die gezeichnet werden.
	 * 
	 * @param batch
	 *            SpriteBatch fürs Zeichnen
	 * @param atlas
	 */
	// @Deprecated
	public void draw(Batch batch) {
		// Rendern aller Schiffe
		tileIterator = drawShips.iterator();
		EntityShip curShip;
		while (tileIterator.hasNext()) {
			curShip = tileIterator.next();
			curShip.render(batch);

		}

	}

	/**
	 * Methode gibt ein Array von EntityShip zurück die gezeichnet werden
	 * können.
	 * 
	 * @return out rrayList<EntityShip> fürs Zeichnen
	 */
	public ArrayList<EntityShip> getTiledShips() {
		// ArrayList<EntityShip> out = new ArrayList<EntityShip>();
		//
		// if(placedShips==null)
		// System.out.println("LOL!");
		// for (Ship ship : placedShips) {
		// for (FieldUnit unit : ship.location) {
		// out.add(unit.getPlacedShip().getEntityShipDrawUnit());
		// System.out.println("Schiffsteilkoord: x"
		// + unit.getPlacedShip().getEntityShipDrawUnit().getX() + " y"
		// + unit.getPlacedShip().getEntityShipDrawUnit().getY());
		// }
		// }
		return drawShips;
	}
}
