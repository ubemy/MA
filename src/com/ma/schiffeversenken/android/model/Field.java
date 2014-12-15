package com.ma.schiffeversenken.android.model;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ma.schiffeversenken.EntityShip;
import com.ma.schiffeversenken.GameFieldScreen;
import com.ma.schiffeversenken.MyGdxGameField;
import com.ma.schiffeversenken.Tile;

/**
 * Das Spielfeld
 * 
 * @author Maik Steinborn
 * @author Klaus Schlender
 */
public class Field {
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
	int size;
	// graphics High and width
	private int h = Gdx.graphics.getHeight();
	private int w = Gdx.graphics.getWidth();
	private TextureAtlas atlas;
	private TiledMapTileLayer mapTileLayer;
	// TODO EntityShip or Tile
	private ArrayList<EntityShip> drawShips;
	private Iterator<EntityShip> tileIterator;

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

	public Field(int typ, TextureAtlas a,TiledMapTileLayer mtl) {
		try {
			this.typ = typ;
			this.atlas = a;
			this.mapTileLayer = mtl;
			this.size=mtl.getCell(0, 0).getTile().getTextureRegion().getRegionWidth();
			drawShips = new ArrayList<EntityShip>();
			create();
			createNeighbors();
			createKante();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Setzt die platzierten Schiffe nach Erstellung.
	 * 
	 * @param ships
	 *            Schiffe, die zu diesem Spielfeld zugeordnet werden sollen
	 */
	public void setShips(Ship[] ships) {
		//Vorher das setzen der Schiffe
		this.placedShips = ships;

		// Für das Zeichnen bekommt jedes Schiff sein EntityShip danach die Koordinaten.
		for (Ship ship : ships) {
			// Finden der geeigneten Textur
			String textureName;
			switch (ship.getShipSegment()) {
			case 0:// 0=Vorderteil
				if (ship.getOrientation() == 0 || ship.getOrientation() == 2) {// Horizontal
					if (!ship.isDestroyed()) {
						textureName = "hzshipfront";
					} else {
						textureName = "hzshipfrontattacked";
					}
				} else {// Vertikal
					if (!ship.isDestroyed()) {
						textureName = "shipfront";
					} else {
						textureName = "shipfrontattacked";
					}
				}
				break;
			case 2:// 2=Hinterteil
				if (ship.getOrientation() == 0 || ship.getOrientation() == 2) {// Horizontal
					if (!ship.isDestroyed()) {
						textureName = "hzshipback";
					} else {
						textureName = "hzshipbackattacked";
					}
				} else {// Vertikal
					if (!ship.isDestroyed()) {
						textureName = "shipback";
					} else {
						textureName = "shipbackattacked";
					}
				}
				break;
			default:// 1=Mittelteil
				if (ship.getOrientation() == 0 || ship.getOrientation() == 2) {// Horizontal
					if (!ship.isDestroyed()) {
						textureName = "hzshipmiddle";
					} else {
						textureName = "hzshipmiddleattacked";
					}
				} else {// Vertikal
					if (!ship.isDestroyed()) {
						textureName = "shipmiddle";
					} else {
						textureName = "shipmiddleattacked";
					}
				}
				break;
			}
			// Setzen der EntityShip für das Schiff	
			EntityShip tmpEntity;
			tmpEntity = new EntityShip(0f, 0f, size, size, atlas.findRegion(textureName).getTexture(), mapTileLayer);

			drawShips.add(tmpEntity);
			ship.setEntityShipDrawUnit(tmpEntity);
		}

		// Hier wird für jedes EntityShip festgelegt wo es gezeichnet wird.
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (units[i][j].getOccupied()) {
					units[i][j]
							.getPlacedShip()
							.getEntityShipDrawUnit()
							.setPosition(units[i][j].getXpos(),
									units[i][j].getYpos());
				}
			}
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
		int cellPosY = 1;
		if (this.typ == 1) {
			cellPosY += 10;
		}
		float layerW = mapTileLayer.getWidth();
		float layerH = mapTileLayer.getHeight();
		;
		int id = 0;

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				id++;

				// TODO Testen auf funktion beim Zeichen
				units[i][j] = new FieldUnit(id, (j + cellPosX) * layerW,
						(i + cellPosY) * layerH);
			}
		}

		// Debugging ausgabe der positionen auf konsole
		System.out.println("Neues Feld: ");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// Debugging
				System.out.println("FieldUnit: x:" + units[i][j].getXpos()
						+ " y:" + units[i][j].getYpos());
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
						e.setEdge(1, 1);
					else
						e.setEdge(2, 1);
				}
				if (id > 89) {
					// Untere Kante
					if (e.getEdge(1) == 0)
						e.setEdge(1, 2);
					else
						e.setEdge(2, 2);
				}
				if ((id - (10 * (i + 1))) == 0) {
					// Rechte Kante
					if (e.getEdge(1) == 0)
						e.setEdge(1, 3);
					else
						e.setEdge(2, 3);
				}
				if ((id - (10 * i)) == 1) {
					// Linke Kante
					if (e.getEdge(1) == 0)
						e.setEdge(1, 4);
					else
						e.setEdge(2, 4);
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
	public void draw(Batch batch) {

		//Rendern aller Schiffe
		//TODO Player Ships
		tileIterator = drawShips.iterator();
		EntityShip curShip;
		while(tileIterator.hasNext()){
			curShip=tileIterator.next();
			curShip.render(batch);
		}
		
		// for (int i = 0; i < 10; i++) {
		// for (int j = 0; j < 10; j++) {
		// // TODO Drawing
		// units[i][j].draw(batch, atlas, size);
		// }
		// }
	}

	public ArrayList<EntityShip> getTiledShips() {
		;

		return drawShips;
	}
}
