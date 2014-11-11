package com.ma.schiffeversenken.model;

public class FeldElement {
	/*
	 * Das Spielfeld besteht aus 100 FeldElementen
	 */
	FeldElement lNachbar, rNachbar, oNachbar, uNachbar; //Direkte Nachbar dieses Feldes
	Schiff platziertesSchiff; //Schiff, das auf diesem FeldElement steht
	int id;
	boolean belegt;
	int kante1=0;
	int kante2=0;
	private boolean attackedByFirstPlayer;
	private boolean attackedBySecondPlayer;
	private boolean shipDestroyedByFirstPlayer;
	private boolean shipDestroyedBySecondPlayer;
	
	public FeldElement(int id){
		this.id=id;
		this.belegt = false;
		this.attackedByFirstPlayer = false;
		this.attackedBySecondPlayer = false;
		this.shipDestroyedByFirstPlayer = false;
		this.shipDestroyedBySecondPlayer = false;
	}
	
	public void attack(){
		//Dieses FeldElement attackieren
	}
	
	public boolean getAttackedByFirstPlayer(){
		return this.attackedByFirstPlayer;
	}
	
	public void setAttackedByFirstPlayer(boolean attacked){
		this.attackedByFirstPlayer = attacked;
	}
	
	public boolean getAttackedBySecondPlayer(){
		return this.attackedBySecondPlayer;
	}
	
	public void setAttackedBySecondPlayer(boolean attacked){
		this.attackedBySecondPlayer = attacked;
	}
	//
	public boolean getShipDestroyedByFirstPlayer(){
		return this.shipDestroyedByFirstPlayer;
	}
	
	public void setShipDestroyedByFirstPlayer(boolean attacked){
		this.shipDestroyedByFirstPlayer = attacked;
	}
	
	public boolean getShipDestroyedBySecondPlayer(){
		return this.shipDestroyedBySecondPlayer;
	}
	
	public void setShipDestroyedBySecondPlayer(boolean attacked){
		this.shipDestroyedBySecondPlayer = attacked;
	}
	//
	public void setKante(int kante, int wert){
		if(kante == 1) this.kante1 = wert;
		else if(kante == 2) this.kante2 = wert;
	}
	
	public int getKante(int kante){
		int ret = 0;
		if(kante == 1) ret = this.kante1;
		else if(kante == 2) ret = this.kante2;
		
		return ret;
	}

	public void setNeighbors(FeldElement lNachbar, FeldElement rNachbar, FeldElement oNachbar, FeldElement uNachbar){
		//Weist diesem FeldElement seine direkten Nachbarn zu
		this.lNachbar = lNachbar;
		this.rNachbar = rNachbar;
		this.oNachbar = oNachbar;
		this.uNachbar = uNachbar;
	}
	
	public int getID(){
		return id;
	}
	
	public void platziereSchiff(Schiff schiff){
		this.platziertesSchiff = schiff;
	}
	
	public Schiff getPlatziertesSchiff(){
		return this.platziertesSchiff;
	}
	
	public boolean getBelegt(){
		return belegt;
	}
	
	public void setBelegt(boolean belegt){
		this.belegt = belegt;
	}
}
