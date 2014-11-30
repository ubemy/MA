package com.ma.schiffeversenken.android.model;

/**
 * Interface fuer alle Schiffe
 * @author Maik
 */
public abstract class Ship {
	/**Laenge des Schiffs*/
	int size; 
	/**Boolean ob das Schiff zerstoert ist*/
	boolean destroyed;
	/**Name des Schiffs*/
	String name;
	/**Felder auf denen das Schiff platziert ist*/
	FieldUnit[] location;
	
	/**
	 * Erstellt ein Ship Objekt
	 * @param name Name des Schiffs
	 * @param size Groesse des Schiffs
	 */
	public Ship(String name, int size){
		this.name=name;
		this.size=size;
		this.destroyed = false;
		this.location = new FieldUnit[size];
	}
	
	/**
	 * Setzt einen Boolean ob das Schiff zerstoert ist
	 * @param destroyed true oder false ob das Schiff zerstoert ist
	 */
	public void setDestroyed(boolean destroyed){
		this.destroyed = destroyed;
	}
	
	/**
	 * Gibt zurueck ob das Schiff zerstoert ist
	 * @return true oder false ob das Schiff zerstoert ist
	 */
	public boolean getDestroyed(){
		return this.destroyed;
	}
	
	/**
	 * Gibt die Groesse des Schiffs zurueck
	 * @return Die Groesse des Schiffs
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Gibt den Namen des Schiffs zurueck
	 * @return Der Name des Schiffs
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Standort des Schiffs
	 * @param element Das FeldElement
	 * @param i Indizes der FeldElemente, auf denen das Schiff steht
	 */
	public void setStandort(FieldUnit element, int i){
		this.location[i] = element;
	}
	
	/**
	 * Gibt den Standort des Schiffs zurueck
	 * @return Der Standort des Schiffs
	 */
	public FieldUnit[] getLocation(){
		return this.location;
	}
}
