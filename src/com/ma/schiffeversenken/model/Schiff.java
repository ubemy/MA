package com.ma.schiffeversenken.model;

public abstract class Schiff {
	/*
	 * Interface für alle Schiffe
	 */
	
	int size; //Länger des Schiffs
	String name; //Name des Schiffs
	FeldElement[] standort; //Felder auf denen das Schiff platziert ist
	
	public Schiff(String name, int size){
		this.name=name;
		this.size=size;
		standort = new FeldElement[size];
	}
	
	public int getSize() {
		return size;
	}
	
	public String getName() {
		return name;
	}

	
}
