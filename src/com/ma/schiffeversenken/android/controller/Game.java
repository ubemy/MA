package com.ma.schiffeversenken.android.controller;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ma.schiffeversenken.android.model.*;

/**
 * Gesamte Spiellogik
 * - Beinhaltet alle Spieldaten
 * - Kommuniziert mit beiden Spielern
 * @author Maik Steinborn
 * @author Klaus Schlender
 */
public class Game implements Runnable {
	/**Einzelspieler (=0) oder Mehrspielermodus (=1)*/
	private int gameMode;
	/**Spielfeld des Spielerstellers*/
	private Field firstField;
	/**Spielfeld des Gastspielers*/
	private Field secondField;
	/**Wird auf true gesetzt, wenn Spieler 1 eine Eingabe getaetigt hat*/
	private boolean firstGamerAction;
	/**Wird auf true gesetzt, wenn Spieler 2 eine Eingabe getaetigt hat*/
	private boolean secondGamerAction;
	/**Die Feld-ID, die Spieler 1 attackiert*/
	private int firstGamerAttackID;
	/**Die Feld-ID, die Spieler 2 attackiert*/
	private int secondGamerAttackID;
	/**Kuenstlicher Computer Gegner*/
	KI ki;
	
	//Nur zu Testzwecken - Maik
	public boolean getroffen = false;
	
	/**
	 * Erstellt ein neues Game Objekt
	 * @param gameMode 0=Einzelspieler; 1=Mehrspieler
	 * @param firstField Spielfeld des 1. Spielers
	 * @param secondField Spieldfeld des 2. Spielers
	 */
	public Game(int gameMode, Field firstField, Field secondField){
		this.gameMode = gameMode;
		this.firstField = firstField;
		this.secondField = secondField;
		this.firstGamerAction = false;
		this.secondGamerAction = false;
		
		if(gameMode == 0){
			//Wenn GameMode == 0 == Einzelspieler, dann KI erstellen
			//Field kiField = new Field(1);
			ki = new KI(secondField, firstField);
		}
	}
	
	/**
	 * Die Methode wird von der GUI aufgerufen,
	 * sobald Spieler 1 auf ein Feld getippt
	 * hat, das er attackieren moechte
	 * @param id ID des Feldes, das angegriffen wird
	 */
	public void firstGamerAngriff(int id){
		this.firstGamerAttackID = id;
		this.firstGamerAction = true;
	}
	
	/**
	 * Die Methode wird von der GUI aufgerufen,
	 * sobald Spieler 2 auf ein Feld getippt
	 * hat, das er attackieren moechte
	 * @param id ID des Feldes, das angegriffen wird 
	 */
	public void secondGamerAngriff(int id){
		this.secondGamerAttackID = id;
		this.secondGamerAction = true;
	}
	
	/**
	 * Wenn alle FeldElemente eines Schiffes zerstoert wurden,
	 * wird dieses Schiff als zerstoert markiert
	 * @param fe Feld, auf dem das Schiff steht, das geprueft wird
	 */
	private void destroyShip(FieldUnit fe){
		boolean destroyed = true;
		
		for(FieldUnit f : fe.getPlacedShip().getLocation()){
			if(!f.getAttacked()){
				destroyed = false;
			}
		}
		
		fe.getPlacedShip().setDestroyed(destroyed);
	}
	
	/**
	 * Startet das Game. Wird aufgerufen, wenn das Game Thread gestartet wird
	 * @throws InterruptedException
	 */
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
	
	/**
	 * 
	 * @param id ID des Feldes, das angegriffen wird
	 * @param gamer 0= Erster Spieler; 1= zweiter Spieler
	 * @return Boolean ob ein gegnerisches Schiff getroffen wurde
	 */
	private boolean gamerAction(int id, int gamer){
		boolean ret = false;
		FieldUnit fe;
		boolean attackHit = false;
		boolean shipDestroyed = false;
		
		if(gamer == 0){
			fe = secondField.getElementByID(id);
		}
		else{
			fe = firstField.getElementByID(id);
		}
		
		fe.setAttacked(true); //FeldElement als attackiert markieren
		
		if(fe.getOccupied()){
			//Wenn das Feld belegt war
			ret = true;
			destroyShip(fe);
			shipDestroyed = fe.getPlacedShip().getDestroyed();
			attackHit = true;
		}
		
		if(gamer == 1 && gameMode == 0){
			/*
			 * Wenn die KI attackiert hat werden zwei Variablen gesetzt,
			 * damit die KI weiss ob ein Schiff getroffen und/oder zerstoert wurden
			 */
			ki.updateHistory(id, attackHit, shipDestroyed);
		}
		
		resetActionVariables();
		
		getroffen = ret; //Nur zu Testzwecken - Maik
		
		return ret;
	}
	
	/**
	 * Variablen nach Angriff zuruecksetzen
	 */
	private void resetActionVariables(){
		firstGamerAction = false;
		firstGamerAttackID = 0;
		secondGamerAction = false;
		secondGamerAttackID = 0;
	}
	
	/**
	 * Ueberpruefen ob ein Spieler gewonnen hat
	 * @return
	 * - Gibt 1 zurueck, wenn Spieler 1 gewonnen hat
	 * - Gibt 2 zurueck, wenn Spieler 2 gewonnen hat
	 * - Gibt 0 zurueck, wenn niemand gewonnen hat 
	 */
	private int hasSomebodyWon(){
		int ret = 1;
		
		for(Ship ship : secondField.getShips()){
			if(!ship.getDestroyed()){
				ret = 2;
			}
		}
		
		if(ret == 2){
			for(Ship ship : firstField.getShips()){
				if(!ship.getDestroyed()){
					ret = 0;
				}
			}
		}
		
		return ret;
	}

	/**
	 * Startet das Game Thread
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Zeichnet die beiden Spielfelder mit den jeweiligen Zuständen
	 * @param batch SpriteBatch worauf gezeichnet wird.
	 * @param atlas Textures
	 */
	public void draw(SpriteBatch batch, TextureAtlas atlas) {
		// TODO Auto-generated method stub
		firstField.draw(batch,atlas);
		secondField.draw(batch,atlas);
	}
	
	
}
