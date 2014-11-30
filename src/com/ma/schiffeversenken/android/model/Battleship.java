package com.ma.schiffeversenken.android.model;

/**
 * Schlachtschiff mit einer Groesse von 4 Felder
 * @author Maik Steinborn
 */
public class Battleship extends Ship {
	/**
	 * Erstellt ein Battelship Objekt
	 * @param name Name des Schiffes
	 */
	public Battleship(String name) {
		super(name, 4);
	}
}
