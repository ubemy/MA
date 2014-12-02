package com.ma.schiffeversenken.android.controller;

import java.util.Random;
import com.ma.schiffeversenken.android.model.*;

/**
 * Spiellogik des Computer Gegners
 * @author Maik Steinborn
 */
public class KI {
	/**Das Spielfeld der KI*/
	private Field myField;
	/**Das gegnerische Spielfeld*/
	private Field enemiesField;
	/**Die ID's der letzten 4 Angriffe*/
	private int[] idHistory = new int[4];
	/**True oder False ob bei den letzten 4 Angriffen ein Schiff getroffen wurde*/
	private boolean[] hitHistory = new boolean[4];
	/**True oder False ob bei den letzten 4 Angriffen ein Schiff zerstoert wurde*/
	private boolean[] shipDestroyedHistory = new boolean[4];
	
	/**
	 * Erstellt ein neues KI Objekt
	 * @param myField Spielfeld der KI
	 * @param enemiesField Spielfeld des Gegners
	 */
	public KI(Field myField, Field enemiesField){
		this.myField = myField;
		this.enemiesField = enemiesField;
		setShips(createShips());
		initHistory();
	}
	
	/**
	 * History updaten
	 * Die History speichert die letzten 4 Angriffe
	 * @param id Die id der letzten Angriffe
	 * @param hit True oder False ob bei den letzten Angriffen ein Schiff getroffen wurde
	 * @param shipDestroyed True oder False ob bei den letzten Angriffen ein Schiff zerstoert wurde
	 */
	public void updateHistory(int id, boolean hit, boolean shipDestroyed){
		for(int i=3; i>0; i--){
			idHistory[i] = idHistory[i-1];
			hitHistory[i] = hitHistory[i-1];
			shipDestroyedHistory[i] = shipDestroyedHistory[i-1];
		}
		
		idHistory[0] = id;
		hitHistory[0] = hit;
		shipDestroyedHistory[0] = shipDestroyed;
	}
	
	/**
	 * History initialisieren
	 */
	private void initHistory(){
		for(int i = 0; i<4; i++){
			idHistory[i] = 0;
			hitHistory[i] = false;
			shipDestroyedHistory[i] = false;
		}
	}
	
	/**
	 * Benoetigte Schiffe erstellen
	 * @return Erstellt Schiffe
	 */
	private Ship[] createShips(){
		Ship[] ships = new Ship[]{new Submarine("Uboot"),
				new Submarine("Uboot"),
				new Submarine("Uboot"),
				new Cruiser("Kreuzer"),
				new Cruiser("Kreuzer"),
				new Cruiser("Kreuzer"),
				new Cruiser("Kreuzer"),
				new Destroyer("Zerstoerer"),
				new Destroyer("Zerstoerer"),
				new Battleship("Schlachtschiff")
				};
		
		return ships;
	}
	
	public ShipPlacement sp = new ShipPlacement(); //Nur zu Testzwecken als globale Variable
	
	/**
	 * Schiffe auf dem Spielfeld der KI platzieren
	 * @param ships Die Schiffe, die platziert werden
	 */
	private void setShips(Ship[] ships){
		sp = new ShipPlacement();
		sp.placeShips(myField, ships);
	}
	
	/**
	 * Den Gegner attackieren
	 * @return ID des Feldes, das attackiert werden soll
	 */
	public int attack(){
		Random random = new Random();
		int nextAttackID = 0;
		int idForContinueLastAttack = getIDForContinueLastAttack();
		
		if(idForContinueLastAttack != 0){
			//Den letzten Angriff fortfuehren
			nextAttackID = idForContinueLastAttack;
		}
		else{
			//Angriff auf eine neue zufaellige FeldID starten
			do{
				//Zufaellige Zahl erstellen
				nextAttackID = random.nextInt(99) + 1;
			}while(enemiesField.getElementByID(nextAttackID).getAttacked());
		}
		
		//Ausgewaehltes FeldElement attackieren
		return nextAttackID;
	}
	
	/**
	 * Berechnet die ID, die von der KI angegriffen werden soll,
	 * wenn die KI in der vorherigen Spielrunde ein gegnerisches Schiff getroffen hat
	 * @return Die ID des Feldelements, das angegriffen werden soll
	 */
	private int getIDForContinueLastAttack(){
		int ret = 0;
		boolean jump = false;
		
		for(int i=0; i<4; i++){
			if(!jump && ret == 0){
				if(hitHistory[i]){
					if(shipDestroyedHistory[i]){
						jump = true;
					}
					else{
						if((idHistory[i] + 1) <= 100){
							if(enemiesField.getElementByID(idHistory[i] + 1).getAttacked()){
								if(enemiesField.getElementByID(idHistory[i]).getEdge(1) != 4 && enemiesField.getElementByID(idHistory[i]).getEdge(2) != 4 && !enemiesField.getElementByID(idHistory[i] - 1).getAttacked()){
									ret = idHistory[i] - 1;
								}
							}
						}
						if((idHistory[i] - 1) > 0){
							if(enemiesField.getElementByID(idHistory[i] - 1).getAttacked()){
								if(enemiesField.getElementByID(idHistory[i]).getEdge(1) != 3 && enemiesField.getElementByID(idHistory[i]).getEdge(2) != 3 && !enemiesField.getElementByID(idHistory[i] + 1).getAttacked()){
									ret = idHistory[i] + 1;
								}
							}
						}
						if((idHistory[i] + 10) <= 100){
							if(enemiesField.getElementByID(idHistory[i] + 10).getAttacked()){
								if(enemiesField.getElementByID(idHistory[i]).getEdge(1) != 1 && enemiesField.getElementByID(idHistory[i]).getEdge(2) != 1 && !enemiesField.getElementByID(idHistory[i] - 10).getAttacked()){
									ret = idHistory[i] - 10;
								}
							}
						}
						if((idHistory[i] - 10) > 0){
							if(enemiesField.getElementByID(idHistory[i] - 10).getAttacked()){
								if(enemiesField.getElementByID(idHistory[i]).getEdge(1) != 2 && enemiesField.getElementByID(idHistory[i]).getEdge(2) != 2 && !enemiesField.getElementByID(idHistory[i] + 10).getAttacked()){
									ret = idHistory[i] + 10;
								}
							}
						}
					}
				}
			}
		}
		
		ret = getIDAfterOneAttackToShip(ret);
		
		return ret;
	}

	/**
	 * Wenn das gegnerische Schiff getroffen und erst einmal angegriffen wurde,
	 * wird durch Zufall bestimmt in welche Richtung der nächste Angriff geht
	 * @param ret Die aktuelle ID, die angegriffen wird
	 * @return Die neue ID, die angegriffen werden soll
	 */
	private int getIDAfterOneAttackToShip(int ret) {
		if((ret == 0) && hitHistory[0]){
			Random random = new Random();
			int counter = 0;
			do{
				int randomInt = random.nextInt(3) + 1;
				
				if(enemiesField.getElementByID(idHistory[0]).getEdge(1) != randomInt && enemiesField.getElementByID(idHistory[0]).getEdge(2) != randomInt){
					if(randomInt == 1){
						if(!enemiesField.getElementByID(idHistory[0] - 10).getAttacked()){
							ret = idHistory[0] - 10;
						}
					}
					if(randomInt == 2){
						if(!enemiesField.getElementByID(idHistory[0] + 10).getAttacked()){
							ret = idHistory[0] + 10;
						}
					}
					if(randomInt == 3){
						if(!enemiesField.getElementByID(idHistory[0] + 1).getAttacked()){
							ret = idHistory[0] + 1;
						}
					}
					if(randomInt == 4){
						if(!enemiesField.getElementByID(idHistory[0] - 1).getAttacked()){
							ret = idHistory[0] - 1;
						}
					}
				}
				
				counter++;
			}while(ret == 0 && counter < 4);
		}
		return ret;
	}
}

