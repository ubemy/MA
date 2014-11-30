package com.ma.schiffeversenken.android.model;

/**
 * Kreuzer mit einer Groesse von einem Feld
 * @author Maik Steinborn
 */
public class Cruiser extends Ship {
	/**
	 * Erstellt ein Cruiser Objekt
	 * @param name Name des Schiffes
	 */
	public Cruiser(String name){
		super(name, 1);
	}
}
