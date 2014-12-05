package com.ma.schiffeversenken.android.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Das Spielfeld besteht aus 100 FeldElementen
 * 
 * @author Maik Steinborn
 * @author Klaus Schlender
 */
public class FieldUnit {
	/** Direkte Nachbarn dieses Feldes */
	FieldUnit lNeighbor, rNeighbor, oNeighbor, uNeighbor;
	/** Schiff, das auf diesem FeldElement steht */
	Ship placedShip;
	/** Die eindeutige ID dieses Feldelements */
	int id;
	/** True oder False ob das Feldelement belegt ist */
	boolean occupied;
	/**
	 * Wenn das Feldelement direkt an einer oder zwei Kante\n liegt, gibt dieser
	 * Wert die Richtung der Kante\n an: 1=Obere Kante, 2=Untere Kante, 3=Rechte
	 * Kante, 4=Linke Kante
	 */
	int edge1 = 0, edge2 = 0;
	/** True oder False ob dieses Feldelement bereits attackiert wurde */
	private boolean attacked;
	/** Segment des Schiffs: 0=Vorderteil, 1=Mittelteil, 2=Hinterteil */
	private int shipSegment;

	// OpenGL Elemente
	private TextureRegion drawFeld;
	private Sprite sprite;
	private String textureName;
	private float xpos;
	private float ypos;

	/**
	 * Erstellt ein neues FieldUnit Objekt
	 * 
	 * @param id
	 *            Die eindeutige ID dieses Feldelements
	 * @param xpos
	 *            X-Koordinate
	 * @param ypos
	 *            Y-Koordinate
	 */
	public FieldUnit(int id, float xpos, float ypos) {
		this.id = id;
		this.occupied = false;
		this.attacked = false;
		this.placedShip = null;
		this.xpos = xpos;
		this.ypos = ypos;
	}

	/**
	 * Gibt zurueck ob auf diesem Feldelement ein Vorder-, Mittel- oder
	 * Hinterteil des Schiffs liegt
	 * 
	 * @return Segment des Schiffs: 0=Vorderteil, 1=Mittelteil, 2=Hinterteil
	 */
	public int getShipSegment() {
		return this.shipSegment;
	}

	/**
	 * Setzt True oder False ob das Feldelement attackiert wurde
	 * 
	 * @param attacked
	 *            True oder False ob das Feldelement attackiert wurde
	 */
	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}

	/**
	 * Gibt zurueck ob das Feldelement bereits attackiert wurde
	 * 
	 * @return True oder False ob das Feldelement attackiert wurde
	 */
	public boolean getAttacked() {
		return this.attacked;
	}

	/**
	 * Setzt die Kanten dieses Feldelements
	 * 
	 * @param edge
	 *            1 = 1. Kante oder 2 = 2. Kante des Feldelements
	 * @param value
	 *            Art der Kante: 1 = Obere Kante, 2 = Untere Kante, 3 = Rechte
	 *            Kante, 4 = Linke Kante
	 */
	public void setEdge(int edge, int value) {
		if (edge == 1)
			this.edge1 = value;
		else if (edge == 2)
			this.edge2 = value;
	}

	/**
	 * Gibt den Wert der Kante zurueck
	 * 
	 * @param edge
	 *            1 = 1. Kante oder 2 = 2. Kante des Feldelements
	 * @return Art der Kante: 1 = Obere Kante, 2 = Untere Kante, 3 = Rechte
	 *         Kante, 4 = Linke Kante
	 */
	public int getEdge(int edge) {
		int ret = 0;

		if (edge == 1)
			ret = this.edge1;
		else if (edge == 2)
			ret = this.edge2;

		return ret;
	}

	/**
	 * Weist diesem FeldElement seine direkten Nachbarn zu
	 * 
	 * @param lNeighbor
	 *            Linker Nachbar
	 * @param rNeighbor
	 *            Rechter Nachbar
	 * @param oNeighbor
	 *            Oberer Nachbar
	 * @param uNeighbor
	 *            Unterer Nachbar
	 */
	public void setNeighbors(FieldUnit lNeighbor, FieldUnit rNeighbor,
			FieldUnit oNeighbor, FieldUnit uNeighbor) {
		this.lNeighbor = lNeighbor;
		this.rNeighbor = rNeighbor;
		this.oNeighbor = oNeighbor;
		this.uNeighbor = uNeighbor;
	}

	/**
	 * Gibt die eindeutige ID des Feldelements zurueck
	 * 
	 * @return Die eindeutige ID
	 */
	public int getID() {
		return id;
	}

	/**
	 * Platziert auf diesem Feldelement ein Schiff
	 * 
	 * @param ship
	 *            Das Schiff, das auf diesem Feldelement platziert wird
	 * @param shipSegment
	 *            Segment des Schiffs: 0=Vorderteil, 1=Mittelteil, 2=Hinterteil
	 */
	public void placeShip(Ship ship, int shipSegment) {
		this.placedShip = ship;
		this.shipSegment = shipSegment;
	}

	/**
	 * Gibt das Schiff zurueck, das auf diesem Feldelement platziert wurde
	 * 
	 * @return Das Schiff, das auf diesem Feldelement platziert wurde
	 */
	public Ship getPlacedShip() {
		if (occupied) {
			return this.placedShip;
		} else {
			return null;
		}
	}

	/**
	 * Gibt zurueck ob das Feldelement belegt ist
	 * 
	 * @return True oder False ob das Feldelemenet belegt ist
	 */
	public boolean getOccupied() {
		return occupied;
	}

	/**
	 * Setzt ob das Feldelement belegt ist
	 * 
	 * @param occupied
	 *            True oder False ob das Feldelement belegt ist
	 */
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	
	/**
	 * Für das Zeichnen in Opengl Koordinaten benötigt
	 * @return position
	 */
	public float getXpos() {
		return xpos;
	}

	/**
	 * Für das Zeichnen in Opengl Koordinaten benötigt
	 * @return position
	 */
	public void setXpos(float xpos) {
		this.xpos = xpos;
	}

	/**
	 * Für das Zeichnen in Opengl Koordinaten benötigt
	 * @return position
	 */
	public float getYpos() {
		return ypos;
	}

	/**
	 * Für das Zeichnen in Opengl Koordinaten benötigt
	 * @return position
	 */
	public void setYpos(float ypos) {
		this.ypos = ypos;
	}

	/**
	 * TODO Maik nach den variablen fragen und Abklären wegen Zeichnen.
	 * 
	 * @param batch
	 *            ist die SpriteBach die fürs Zeichnen übergeben wird.
	 * @param atlas
	 *            hält alle benötigten Texturelemente mit namen
	 */
	public void draw(SpriteBatch batch, TextureAtlas atlas, int size) {
		// TODO Auslesen ob das Schiff Horizontal oder Vertikal steht
		if (getAttacked() && getPlacedShip() != null) {
			// TODO wenn Front
			textureName = "shipfrontattacked";
			// TODO wenn Mittelteil
			textureName = "shipmiddleattacked";
			// TODO wenn Back
			textureName = "shipbackattacked";
		} else if (!getAttacked() && getPlacedShip() != null) {
			// TODO wenn Front
			textureName = "shipfront";
			// TODO wenn Mittelteil
			textureName = "shipmiddle";
			// TODO wenn Back
			textureName = "shipback";
		} else if (getAttacked() && getPlacedShip() == null) {
			textureName = "waterattacked";
		} else {
			textureName = "water";
		}
		drawFeld = atlas.findRegion(textureName);
		sprite = new Sprite(drawFeld);
		sprite.setSize(size, size);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(xpos, ypos);
		sprite.draw(batch);
		// TODO Testen was besser ist über batch oder sprite das Zeichen
		// batch.draw(drawFeld, xpos, ypos);
	}
}
