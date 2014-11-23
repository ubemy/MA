package com.ma.schiffeversenken.android.model;

public class FieldUnit {
	/*
	 * Das Spielfeld besteht aus 100 FeldElementen
	 */
	FieldUnit lNeighbor, rNeighbor, oNeighbor, uNeighbor; //Direkte Nachbar dieses Feldes
	Ship placedShip; //Schiff, das auf diesem FeldElement steht
	int id;
	boolean occupied;
	int edge1=0;
	int edge2=0;
	private boolean attacked;
	
	public FieldUnit(int id){
		this.id=id;
		this.occupied = false;
		this.attacked = false;
	}
	
	public void setAttacked(boolean attacked){
		this.attacked = attacked;
	}
	
	public boolean getAttacked(){
		return this.attacked;
	}
	
	public void setEdge(int edge, int value){
		if(edge == 1) this.edge1 = value;
		else if(edge == 2) this.edge2 = value;
	}
	
	public int getEdge(int edge){
		int ret = 0;
		
		if(edge == 1) ret = this.edge1;
		else if(edge == 2) ret = this.edge2;
		
		return ret;
	}

	public void setNeighbors(FieldUnit lNeighbor, FieldUnit rNeighbor, FieldUnit oNeighbor, FieldUnit uNeighbor){
		//Weist diesem FeldElement seine direkten Nachbarn zu
		this.lNeighbor = lNeighbor;
		this.rNeighbor = rNeighbor;
		this.oNeighbor = oNeighbor;
		this.uNeighbor = uNeighbor;
	}
	
	public int getID(){
		return id;
	}
	
	public void placeShip(Ship ship){
		this.placedShip = ship;
	}
	
	public Ship getPlacedShip(){
		if(occupied){
			return this.placedShip;
		}
		else{
			return null;
		}
	}
	
	public boolean getOccupied(){
		return occupied;
	}
	
	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}
}
