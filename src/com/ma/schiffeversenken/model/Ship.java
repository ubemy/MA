package com.ma.schiffeversenken.model;

public abstract class Ship {
	/*
	 * Interface für alle Schiffe
	 */
	
	int size; //Laenge des Schiffs
	boolean destroyed;
	String name; //Name des Schiffs
	FieldUnit[] location; //Felder auf denen das Schiff platziert ist
	
	public Ship(String name, int size){
		this.name=name;
		this.size=size;
		this.destroyed = false;
		this.location = new FieldUnit[size];
	}
	
	public void setDestroyed(boolean destroyed){
		this.destroyed = destroyed;
	}
	
	public boolean getDestroyed(){
		return this.destroyed;
	}
	
	public int getSize() {
		return size;
	}
	
	public String getName() {
		return name;
	}

	public void setStandort(FieldUnit element, int i){
		this.location[i] = element;
	}
	
	public FieldUnit[] getLocation(){
		return this.location;
	}
}
