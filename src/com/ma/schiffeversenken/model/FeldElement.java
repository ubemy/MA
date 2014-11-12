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
	private boolean attacked;
	
	public FeldElement(int id){
		this.id=id;
		this.belegt = false;
		this.attacked = false;
	}
	
	public void setAttacked(boolean attacked){
		this.attacked = attacked;
	}
	
	public boolean getAttacked(){
		return this.attacked;
	}
	
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
		if(belegt){
			return this.platziertesSchiff;
		}
		else{
			return null;
		}
	}
	
	public boolean getBelegt(){
		return belegt;
	}
	
	public void setBelegt(boolean belegt){
		this.belegt = belegt;
	}
}
