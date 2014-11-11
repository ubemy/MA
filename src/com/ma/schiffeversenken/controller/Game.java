package com.ma.schiffeversenken.controller;

import com.ma.schiffeversenken.model.FeldElement;
import com.ma.schiffeversenken.model.Spielfeld;

public class Game {
	/*
	 * Gesamte Spiellogik
	 * - Beinhaltet alle Spieldaten
	 * - Kommuniziert mit beiden Spielern
	 */
	private int gameMode; //Einzelspieler (=0) oder Mehrspielermodus (=1)
	Spielfeld firstField; //Spielfeld des Spielerstellers
	Spielfeld secondField; //Spielfeld des Gastspielers
	KI ki; //Künstlicher Computer Gegner
	
	public Game(int gameMode, Spielfeld firstField, Spielfeld secondField){
		this.gameMode = gameMode;
		this.firstField = firstField;
		this.secondField = secondField;
		
		if(gameMode == 0){
			//Wenn GameMode == 0 == Einzelspieler, dann KI erstellen
			Spielfeld kiField = new Spielfeld(1);
			ki = new KI(kiField);
		}
	}
	
	private void destroyShip(FeldElement fe){
		/*
		 * Wenn alle FeldElemente eines Schiffes zerstoert wurden,
		 * wird dieses Schiff als zerstoert markiert
		 */
		boolean destroyed = true;
		
		for(FeldElement f : fe.getPlatziertesSchiff().getStandort()){
			if(!f.getAttacked()){
				destroyed = false;
			}
		}
		
		fe.getPlatziertesSchiff().setDestroyed(destroyed);
	}
	
	public void start(){
		boolean end = false;
		int gamersTurn = 0;
		
		do{
			if(gamersTurn == 0){
				//Auf Eingabe von Benutzer warten
				gamersTurn++;
			}
			else{
				if(gameMode == 0){
					//Wenn GameMode == 0 == Einzelspieler, dann KI attackieren lassen
					FeldElement fe = firstField.getElementByID(ki.attack());
					fe.setAttacked(true); //FeldElement als attackiert markieren
					destroyShip(fe);
					if(fe.getBelegt()){
						ki.setShipHitByLastAttack(true);
						if(fe.getPlatziertesSchiff().getDestroyed()){
							ki.setShipDestroyedByLastAttack(true);
						}
					}
				}
				else{
					//Auf Eingabe von Benutzer warten
				}
				gamersTurn = 0;
			}
			
			if(hasSomebodyWon()){
				end = true;
			}
		}while(!end);
	}
	
	private boolean hasSomebodyWon(){
		//Ueberpruefen ob ein Spieler gewonnen hat
		boolean ret = false;
		
		
		return ret;
	}
}
