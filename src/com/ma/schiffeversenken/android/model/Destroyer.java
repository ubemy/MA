package com.ma.schiffeversenken.android.model;

/**
 * Zerstoerer mit einer Groesse von 3 FeldElementen
 * @author Maik Steinborn
 */
public class Destroyer extends Ship {
	/**
	 * Erstellt ein Destroyer Objekt
	 * @param name Name des Schiffes
	 */
	public Destroyer(String name){
		super(name, 3);
	}
}
