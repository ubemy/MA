package com.ma.schiffeversenken.android.model;

/**
 * Uboot mit einer Groesse von 2 Felder
 * @author Maik Steinborn
 */
public class Submarine extends Ship {
	/**
	 * Erstellt ein Submarine Objekt
	 * @param name Name des Schiffes
	 */
	public Submarine(String name){
		super(name, 2);
	}
}
