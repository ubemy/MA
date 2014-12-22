package com.ma.schiffeversenken.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ma.schiffeversenken.EntityShip;

/**
 * Fuer alle Schiffe
 * 
 * @author Maik, Klaus
 */
public class Ship {
	/** Laenge des Schiffs */
	int size;
	/** Boolean ob das Schiff zerstoert ist */
	boolean destroyed;
	/** Name des Schiffs */
	String name;
	/** Felder auf denen das Schiff platziert ist */
	FieldUnit[] location;
	/** Ausrichtung des Schiffs: 0=rechts, 1=oben, 2=links, 3=unten */
	private int orientation;
	/** Segment des Schiffs: 0=Vorderteil, 1=Mittelteil, 2=Hinterteil */
	private int shipSegment;
	private EntityShip entityShip;

	/**
	 * Erstellt ein Ship Objekt
	 * 
	 * @param name
	 *            Name des Schiffs
	 * @param size
	 *            Groesse des Schiffs
	 */
	public Ship(String name, int size) {
		this.name = name;
		this.size = size;
		this.destroyed = false;
		this.location = new FieldUnit[size];

	}
	
	/**
	 * Gibt die Orientierung des Schiffs zurueck Ausrichtung des Schiffs:
	 * 0=rechts, 1=oben, 2=links, 3=unten
	 * 
	 * @return Die Orientierung des Schiffs
	 */
	public int getOrientation() {
		return this.orientation;
	}

	/**
	 * Setzt einen Boolean ob das Schiff zerstoert ist
	 * 
	 * @param destroyed
	 *            true oder false ob das Schiff zerstoert ist
	 */
	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	/**
	 * Gibt zurueck ob das Schiff zerstoert ist
	 * 
	 * @return true oder false ob das Schiff zerstoert ist
	 */
	public boolean isDestroyed() {
		return this.destroyed;
	}

	/**
	 * Gibt die Groesse des Schiffs zurueck
	 * 
	 * @return Die Groesse des Schiffs
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gibt den Namen des Schiffs zurueck
	 * 
	 * @return Der Name des Schiffs
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Standort des Schiffs
	 * 
	 * @param element
	 *            Das FeldElement
	 * @param i
	 *            Indizes der FeldElemente, auf denen das Schiff steht
	 * @param orientation
	 *            Die Orientierung des Schiffs (nach oben/unten/rechts/links
	 *            gerichtet)
	 */
	public void setStandort(FieldUnit element, int i, int orientation) {
		this.location[i] = element;
		this.orientation = orientation;
	}

	/**
	 * Gibt den Standort des Schiffs zurueck
	 * 
	 * @return Der Standort des Schiffs
	 */
	public FieldUnit[] getLocation() {
		return this.location;
	}

	public void setShipSegment(int s) {
		shipSegment = s;

	}

	/**
	 * Gibt das Segment des Schiffs zurück: 0=Vorderteil, 1=Mittelteil,
	 * 2=Hinterteil
	 * 
	 * @return Das Segment des Schiffs
	 */
	public int getShipSegment() {
		return shipSegment;
	}

	/**
	 * Das Schiff bekommt eine EntityShip Unit übergeben für das Zeichnen
	 * 
	 * @param e
	 *            EntityShip für das Zeichnen des Schiffteils
	 */
	public void setEntityShipDrawUnit(EntityShip e) {
		this.entityShip = e;

	}

	/**
	 * Gibt das EntityShip von dem Schiff für das Zeichnen zurück
	 */
	public EntityShip getEntityShipDrawUnit() {
		return entityShip;

	}

}
