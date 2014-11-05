package com.ma.schiffeversenken.model;

public class FeldElement {
	//Direkte Nachbar dieses Feldes
	FeldElement lNachbar, rNachbar, oNachbar, uNachbar;
	//Schiff, das auf diesem FeldElement steht
	Schiff platziertesSchiff;
	int id;
	boolean belegt;
	
	public FeldElement(int id){
		this.id=id;
		this.belegt = false;
	}

	public void setNeighbors(FeldElement lNachbar, FeldElement rNachbar, FeldElement oNachbar, FeldElement uNachbar){
		//Weiﬂt diesem FeldElement seine direkten Nachbarn zu
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
	
	public boolean getBelegt(){
		return belegt;
	}
}
