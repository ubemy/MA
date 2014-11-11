package com.ma.schiffeversenken.model;

public abstract class Schiff {
	/*
	 * Interface für alle Schiffe
	 */
	
	int size; //Laenge des Schiffs
	boolean destroyed;
	String name; //Name des Schiffs
	FeldElement[] standort; //Felder auf denen das Schiff platziert ist
	
	public Schiff(String name, int size){
		this.name=name;
		this.size=size;
		this.destroyed = false;
		this.standort = new FeldElement[size];
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

	public void setStandort(FeldElement element, int i){
		this.standort[i] = element;
	}
	
	public FeldElement[] getStandort(){
		return this.standort;
	}
}
