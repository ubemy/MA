package com.ma.schiffeversenken.controller;

import com.ma.schiffeversenken.model.*;

public class Game {
	/*
	 * Gesamte Spiellogik
	 * - Beinhaltet alle Spieldaten
	 * - Kommuniziert mit beiden Spielern
	 */
	private int gameMode; //Einzelspieler (=0) oder Mehrspielermodus (=1)
	private Field firstField; //Spielfeld des Spielerstellers
	private Field secondField; //Spielfeld des Gastspielers
	private boolean firstGamerAction; //Wird auf true gesetzt, wenn Spieler 1 eine Eingabe getätigt hat
	private boolean secondGamerAction; //Wird auf true gesetzt, wenn Spieler 2 eine Eingabe getätigt hat
	private int firstGamerAttackID; //Die Feld-ID, die Spieler 1 attackiert
	private int secondGamerAttackID; //Die Feld-ID, die Spieler 2 attackiert
	KI ki; //Kuenstlicher Computer Gegner
	
	public Game(int gameMode, Field firstField, Field secondField){
		this.gameMode = gameMode;
		this.firstField = firstField;
		this.secondField = secondField;
		this.firstGamerAction = false;
		this.secondGamerAction = false;
		
		if(gameMode == 0){
			//Wenn GameMode == 0 == Einzelspieler, dann KI erstellen
			Field kiField = new Field(1);
			ki = new KI(kiField);
		}
	}
	
	public void firstGamerAngriff(int id){
		/*
		 * Die Methode wird von der GUI aufgerufen,
		 * sobald Spieler 1 auf ein Feld getippt
		 * hat, das er attackieren möchte
		 */
		this.firstGamerAttackID = id;
		this.firstGamerAction = true;
	}
	
	public void secondGamerAngriff(int id){
		/*
		 * Die Methode wird von der GUI aufgerufen,
		 * sobald Spieler 2 auf ein Feld getippt
		 * hat, das er attackieren möchte
		 */
		this.firstGamerAttackID = id;
		this.firstGamerAction = true;
	}
	
	private void destroyShip(FieldUnit fe){
		/*
		 * Wenn alle FeldElemente eines Schiffes zerstoert wurden,
		 * wird dieses Schiff als zerstoert markiert
		 */
		boolean destroyed = true;
		
		for(FieldUnit f : fe.getPlacedShip().getLocation()){
			if(!f.getAttacked()){
				destroyed = false;
			}
		}
		
		fe.getPlacedShip().setDestroyed(destroyed);
	}
	
	public void start() throws InterruptedException{
		boolean end = false;
		boolean hitShip = false;
		int gamersTurn = 0;
		
		do{
			if(gamersTurn == 0){
				//Auf Eingabe von Benutzer warten
				do{
					while(!firstGamerAction){
						Thread.sleep(500);
					}
					hitShip = gamerAction(firstGamerAttackID, gamersTurn);
				}while(hitShip);
				gamersTurn++;
			}
			else{
				if(gameMode == 0){
					//Wenn GameMode == 0 == Einzelspieler, dann KI attackieren lassen
					do{
						/*
						 * Die Schleife wird solange durchlaufen,
						 * bis der Spieler ins Leere trifft
						 */
						Thread.sleep(1000);
						hitShip = gamerAction(ki.attack(), gamersTurn);
					}while(hitShip);
				}
				else{
					//Auf Eingabe von Benutzer warten
					do{
						/*
						 * Die Schleife wird solange durchlaufen,
						 * bis der Spieler ins Leere trifft
						 */
						while(!secondGamerAction){
							Thread.sleep(500);
						}
						hitShip = gamerAction(secondGamerAttackID, gamersTurn);
					}while(hitShip);
				}
				gamersTurn = 0;
			}
			
			if(hasSomebodyWon() != 0){
				end = true;
			}
		}while(!end);
	}
	
	private boolean gamerAction(int id, int gamer){
		//Gibt zurueck ob ein gegnerisches Schiff getroffen wurde
		boolean ret = false;
		FieldUnit fe;
		
		if(gamer == 0){
			fe = secondField.getElementByID(id);
		}
		else{
			fe = firstField.getElementByID(id);
		}
		
		fe.setAttacked(true); //FeldElement als attackiert markieren
		
		if(fe.getOccupied()){
			ret = true;
			destroyShip(fe);
			if(gamer == 1 && gameMode == 0){
				/*
				 * Wenn die KI attackiert hat werden zwei Variablen gesetzt,
				 * damit die KI weiß ob ein Schiff getroffen und/oder zerstoert wurden
				 */
				ki.setShipHitByLastAttack(true);
				if(fe.getPlacedShip().getDestroyed()){
					ki.setShipDestroyedByLastAttack(true);
				}
			}
		}
		
		return ret;
	}
	
	private int hasSomebodyWon(){
		/*
		 * Ueberpruefen ob ein Spieler gewonnen hat
		 * - Gibt 1 zurueck, wenn Spieler 1 gewonnen hat
		 * - Gibt 2 zurueck, wenn Spieler 2 gewonnen hat
		 * - Gibt 0 zurueck, wenn niemand gewonnen hat
		 */
		int ret = 1;
		
		for(Ship ship : firstField.getShips()){
			if(ship.getDestroyed()){
				ret = 2;
			}
		}
		
		for(Ship ship : secondField.getShips()){
			if(ship.getDestroyed()){
				ret = 0;
			}
		}
		
		return ret;
	}
}
