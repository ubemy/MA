package com.ma.schiffeversenken.android.controller;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.ma.schiffeversenken.android.model.*;

/**
 * Gesamte Spiellogik
 * - Beinhaltet alle Spieldaten
 * - Kommuniziert mit beiden Spielern
 * @author Maik Steinborn
 * @author Klaus Schlender
 */
public class Game extends Thread {
	/**Einzelspielermodus*/
	private final static int GAME_MODE_SINGLE_PLAYER = 0;
	/**Mehrspielermodus*/
	private final static int GAME_MODE_MULTI_PLAYER = 1;
	/**500 millisekunden*/
	private final static int FIVEHUNDRED_MS = 500;
	/**1000 millisekunden*/
	private final static int THOUSAND_MS = 1000;
	/**50 millisekunden*/
	private final static int FIFTY_MS = 50;
	/**Erster Spieler*/
	private final static int FIRST_GAMER = 0;
	/**Zweiter Spieler*/
	private final static int SECOND_GAMER = 1;
	/**Einzelspieler (=0) oder Mehrspielermodus (=1)*/
	private int gameMode;
	/**Spielfeld des Spielerstellers*/
	private Field firstFieldPlayer;
	/**Spielfeld des Gastspielers*/
	private Field secondFieldEnemy;
	/**Wird auf true gesetzt, wenn Spieler 1 eine Eingabe getaetigt hat*/
	private boolean firstGamerAction;
	/**Wird auf true gesetzt, wenn Spieler 2 eine Eingabe getaetigt hat*/
	private boolean secondGamerAction;
	/**Die Feld-ID, die Spieler 1 attackiert*/
	private int firstGamerAttackID;
	/**Die Feld-ID, die Spieler 2 attackiert*/
	private int secondGamerAttackID;
	/**Kuenstlicher Computer Gegner*/
	private KI ki;
	/**BluetoothConnectedThread Instanz zum Senden der Ereignisse an den
	 * Bluetooth Gegner*/
	private BluetoothConnectedThread btConnectedThread;
	/**Gibt an, ob es sich bei diesem Geraet um den Bluetooth Server handelt*/
	private boolean primaryBTGame;
	/**Gibt an, ob es sich bei diesem Geraet um den Bluetooth Client handelt*/
	private boolean secondaryBTGame;
	/**Variable wird auf True gesetzt, wenn ein Angriff bei dem Bluetooth Gegner
	 * ein Schiff getroffen hat*/
	private boolean returnAttackHit;
	/**Variable wird auf True gesetzt, wenn ein Angriff bei dem Bluetooth Gegner
	 * ein Schiff zerstoert hat*/
	private boolean returnShipDestroyed;
	/**Variable wird auf True gesetzt, wenn nach einem Angriff die Ergebnisse
	 * des Angriffs vom Bluetooth Gegner zurueckgegeben wurden*/
	private boolean returnValuesAvailable;
	/**Variable wird auf True gesetzt, wenn das Feldelement, das attackiert werden soll
	 * bereits zuvor attackiert wurde*/
	private boolean feWasAlreadyAttacked;
	/**Gibt an, welcher Spieler am Zug ist. 0=Spieler 1; 1=Spieler 2*/
	int gamersTurn;
	/**Wird auf True gesetzt, wenn das Spiel zuende ist*/
	private boolean end;
		
	/**
	 * Erstellt ein neues Game Objekt
	 * @param gameMode 0=Einzelspieler; 1=Mehrspieler
	 * @param firstFieldPlayer Spielfeld des 1. Spielers
	 * @param secondField Spieldfeld des 2. Spielers
	 * @param primaryBTGame Gibt an, ob es sich bei diesem Geraet um den Bluetooth Server handelt
	 * @param secondaryBTGame Gibt an, ob es sich bei diesem Geraet um den Bluetooth Client handelt
	 * @param loadedGame Boolean ob das Spiel geladen wurde
	 * @param difficultyLevel Schwierigkeitsstufe der KI
	 */
	public Game(int gameMode, Field firstFieldPlayer, Field secondField, boolean primaryBTGame, boolean secondaryBTGame, boolean loadedGame, int difficultyLevel){
		this.gameMode = gameMode;
		this.firstFieldPlayer = firstFieldPlayer;
		this.secondFieldEnemy = secondField;
		this.primaryBTGame = primaryBTGame;
		this.secondaryBTGame = secondaryBTGame;
		this.end = true;
		feWasAlreadyAttacked = false;
		
		resetActionVariables();
		
		if(gameMode == GAME_MODE_SINGLE_PLAYER){
			//Wenn GameMode == 0 == Einzelspieler, dann KI erstellen
			//Field kiField = new Field(1);
			ki = new KI(secondField, firstFieldPlayer, false, loadedGame, difficultyLevel);
		}
		else{
			btConnectedThread = BluetoothConnectedThread.getInstance();
			btConnectedThread.setGame(this);
		}
	}
	
	/**
	 * Zurueckgeben ob es sich bei diesem Geraet um den Bluetooth Server handelt
	 * @return True, wenn dieses Geraet der Bluetooth Server ist
	 */
	public boolean getPrimaryBTGame(){
		return primaryBTGame;
	}
	
	/**
	 * Zurueckgeben ob es sich bei diesem Geraet um den Bluetooth Client handelt
	 * @return True, wenn dieses Geraet der Bluetooth Client ist
	 */
	public boolean getSecondaryBTGame(){
		return secondaryBTGame;
	}
	
	/**
	 * Wird aufgerufen, wenn ein Touch Event auftritt
	 * Holt die ID, des angegriffenen Feldelements und greift dies an
	 * @param xPos x-Position des Touch-Events
	 * @param yPos y-Position des Touch-Events
	 */
	public void touchEvent(float xPos, float yPos){
		FieldUnit fieldUnit = null;
		
		if(gamersTurn==0){//Wenn Spieler am Zug greife Gegnerfeld an
			if(primaryBTGame || (!primaryBTGame && !secondaryBTGame)){
				fieldUnit = secondFieldEnemy.getElementByXPosYPos(xPos, yPos);
				if(fieldUnit != null) firstGamerAttack(fieldUnit.getID());
			}
		}
		else{
			if(secondaryBTGame){
				fieldUnit = secondFieldEnemy.getElementByXPosYPos(xPos, yPos);
				if(fieldUnit != null) secondGamerAttack(fieldUnit.getID());
			}
			else if(!primaryBTGame && !secondaryBTGame){
				fieldUnit = firstFieldPlayer.getElementByXPosYPos(xPos, yPos);
				if(fieldUnit != null) secondGamerAttack(fieldUnit.getID());
			}
		}
	}
	
	
	
	/**
	 * Die Methode wird von der GUI aufgerufen,
	 * sobald Spieler 1 auf ein Feld getippt
	 * hat, das er attackieren moechte
	 * @param id ID des Feldes, das angegriffen wird
	 */
	public void firstGamerAttack(int id){
		this.firstGamerAttackID = id;
		this.firstGamerAction = true;
	}
	
	/**
	 * Die Methode wird von der GUI aufgerufen,
	 * sobald Spieler 2 auf ein Feld getippt
	 * hat, das er attackieren moechte
	 * @param id ID des Feldes, das angegriffen wird 
	 */
	public void secondGamerAttack(int id){
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
		
		if(fe.getPlacedShip() != null){
			for(FieldUnit f : fe.getPlacedShip().getLocation()){
				if(!f.getAttacked()){
					destroyed = false;
				}
			}
			
			fe.getPlacedShip().setDestroyed(destroyed);
		}
	}
	
	/**
	 * Variablen werden durch BluetoothConnectedtThread gesetzt, wenn diese
	 * Variablen vom Bluetooth Gegner gesendet werden
	 * @param returnAttackHit
	 * @param returnShipDestroyed
	 */
	public void setReturnValues(boolean returnAttackHit, boolean returnShipDestroyed){
		this.returnValuesAvailable = true;
		this.returnAttackHit = returnAttackHit;
		this.returnShipDestroyed = returnShipDestroyed;
	}
	
	/**
	 * Behandelt die Ausführung einer Attacke
	 * @param id ID des Feldes, das angegriffen wird
	 * @param gamer 0= Erster Spieler; 1= zweiter Spieler
	 * @return Boolean ob ein gegnerisches Schiff getroffen wurde
	 */
	private boolean gamerAction(int id, int gamer){
		boolean ret = false;
		FieldUnit fe = null;
		boolean attackHit = false;
		boolean shipDestroyed = false;
		boolean done = false;
		
		
		if(primaryBTGame || secondaryBTGame){
			
			if(gamer == 0 && primaryBTGame){
				fe = secondFieldEnemy.getElementByID(id);
			}
			else if(gamer == 0 && secondaryBTGame){
				fe = firstFieldPlayer.getElementByID(id);
			}
			else if(gamer == 1 && primaryBTGame){
				fe = firstFieldPlayer.getElementByID(id);
			}
			else if(gamer == 1 && secondaryBTGame){
				fe = secondFieldEnemy.getElementByID(id);
			}
			
			if(fe.getAttacked()){
				feWasAlreadyAttacked = true;
			}
			else{
				byte[] attackString = (new String("_ATTACK_" + Integer.toString(id))).getBytes();
				
				if(primaryBTGame && gamer == FIRST_GAMER){
					btConnectedThread.write(attackString);	
				}
				else if(secondaryBTGame && gamer == SECOND_GAMER){
					btConnectedThread.write(attackString);
				}
				
				if((gamer == 0 && primaryBTGame) || (gamer == 1 && secondaryBTGame)){
					while(!returnValuesAvailable){
						try {
							Thread.sleep(FIFTY_MS);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					ret = returnAttackHit;
					destroyShip(fe);//Ob Schiff komplett zerstört
					shipDestroyed = returnShipDestroyed;
					attackHit = returnAttackHit;
				}
				else{
					if(fe.getOccupied()){
						//Wenn das Feld belegt war
						ret = true;
						destroyShip(fe);
						shipDestroyed = fe.getPlacedShip().isDestroyed();
						attackHit = true;
					}
				}
				
				byte[] returnString = (new String("_RETURN_" + Boolean.toString(attackHit) + "_" + Boolean.toString(shipDestroyed))).getBytes();
				
				if(secondaryBTGame && gamer == 0){
					btConnectedThread.write(returnString);
				}
				else if(primaryBTGame && gamer == 1){
					btConnectedThread.write(returnString);
				}
				
				done = true;
			}
		}
		else{
			if(gamer == 0){
				fe = secondFieldEnemy.getElementByID(id);
			}
			else{
				fe = firstFieldPlayer.getElementByID(id);
			}
		}
		
		if(fe.getAttacked()){
			feWasAlreadyAttacked = true;
		}
		
		if(!feWasAlreadyAttacked){
			fe.setAttacked(true); //FeldElement als attackiert markieren
			
			if(!done){
				if(fe.getOccupied()){
					//Wenn das Feld belegt war
					ret = true;
					destroyShip(fe);
					System.out.println("NULL?"+((fe.getPlacedShip()!=null)?"No":"Yes"));
					shipDestroyed = fe.getPlacedShip().isDestroyed();
					attackHit = true;
				}
			}
			
			if(gamer == SECOND_GAMER && gameMode == GAME_MODE_SINGLE_PLAYER){
				/*
				 * Wenn die KI attackiert hat werden zwei Variablen gesetzt,
				 * damit die KI weiss ob ein Schiff getroffen und/oder zerstoert wurden
				 */
				ki.updateHistory(id, attackHit, shipDestroyed);
			}	
		}
		
		resetActionVariables();
		
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
		returnAttackHit = false;
		returnShipDestroyed = false;
		returnValuesAvailable = false;
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
		
		for(Ship ship : secondFieldEnemy.getShips()){
			if(!ship.isDestroyed()){
				ret = 2;
			}
		}
		
		if(ret == 2){
			for(Ship ship : firstFieldPlayer.getShips()){
				if(!ship.isDestroyed()){
					ret = 0;
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Gibt zurueck ob der Bluetooth Gegner gewonnen hat
	 * @return True, wenn Bluetooth Gegner gewonnen hat
	 */
	private boolean hasEnemyWon(){
		boolean ret = true;
		
		for(Ship ship : firstFieldPlayer.getShips()){
			if(!ship.isDestroyed()){
				ret = false;
			}
		}
		
		return ret;
	}
	
	/**
	 * Startet das Game. Wird aufgerufen, wenn das Game Thread gestartet wird
	 */
	@Override
	public void run() {
		end = false;
		boolean hitShip = false;
		//gamersTurn = (new Random()).nextInt(2);
		gamersTurn = 0;
		
		do{
			if(gamersTurn==0){
				//Auf Eingabe von Benutzer warten
				do{
					feWasAlreadyAttacked = false;
					
					while(!firstGamerAction){
						try {
							Thread.sleep(FIVEHUNDRED_MS);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					hitShip = gamerAction(firstGamerAttackID, gamersTurn);
				}while(hitShip || feWasAlreadyAttacked);
				gamersTurn++;
			}
			else{
				if(gameMode == GAME_MODE_SINGLE_PLAYER){
					//Wenn GameMode == 0 == Einzelspieler, dann KI attackieren lassen
					do{
						feWasAlreadyAttacked = false;
						/*
						 * Die Schleife wird solange durchlaufen,
						 * bis der Spieler ins Leere trifft
						 */
						try {
							Thread.sleep(THOUSAND_MS);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						hitShip = gamerAction(ki.attack(), gamersTurn);
					}while(hitShip || feWasAlreadyAttacked);
				}
				else{
					//Auf Eingabe von Benutzer warten
					do{
						feWasAlreadyAttacked = false;
						/*
						 * Die Schleife wird solange durchlaufen,
						 * bis der Spieler ins Leere trifft
						 */
						while(!secondGamerAction){
							try {
								Thread.sleep(FIVEHUNDRED_MS);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						hitShip = gamerAction(secondGamerAttackID, gamersTurn);
					}while(hitShip || feWasAlreadyAttacked);
				}
				gamersTurn = 0;
			}
			
			if(primaryBTGame || secondaryBTGame){
				if(hasEnemyWon()){
					end = true;
				}
			}
			else if(hasSomebodyWon() != 0){
				end = true;
			}
		}while(!end);
	}

	/**
	 * Zeichnet die beiden Spielfelder mit den jeweiligen Zuständen
	 * @param batch SpriteBatch worauf gezeichnet wird.
	 */
//	@Deprecated
	public void draw(Batch batch) {
		// TODO Auto-generated method stub
		firstFieldPlayer.draw(batch);
		secondFieldEnemy.draw(batch);
	}

	public Field getFirstFieldPlayer() {
		return firstFieldPlayer;
	}

	public Field getSecondFieldEnemy() {
		return secondFieldEnemy;
	}
	
	public boolean isEnd(){
		return end;
	}
	
	/**
	 * Methode gibt zurück ob Spieler am Zug ist
	 * 
	 * @return int 0 wenn Spieler Am Zug
	 */
	public int getGamersTurn(){
		return gamersTurn;
	}
}
