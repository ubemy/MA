package com.ma.schiffeversenken.controller;

import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter; 
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.ma.schiffeversenken.R;
import com.ma.schiffeversenken.model.*;
import com.ma.schiffeversenken.view.Startseite;

public class KI extends Activity {
	//Spiellogik des Computer Gegners
	private Field myField;
	private Field enemiesField;
	private boolean shipDestroyedByLastAttack;
	private boolean shipHitByLastAttack;
	private int lastAttackedID;
	private int[] idHistory = new int[4];
	private boolean[] hitHistory = new boolean[4];
	private boolean[] shipDestroyedHistory = new boolean[4];
	
	public KI(Field myField, Field enemiesField){
		this.myField = myField;
		this.enemiesField = enemiesField;
		this.shipDestroyedByLastAttack = false;
		this.shipHitByLastAttack = false;
		this.lastAttackedID = 0;
		setShips(createShips());
		initHistory();
	}
	
	public void updateHistory(int id, boolean hit, boolean shipDestroyed){
		/*
		 * History updaten
		 * Die History speichert die letzten 4 Angriffe
		 * - idHistory = Die id der letzten Angriffe.
		 * - hitHistory = True oder false ob bei den letzten Angriffen ein Schiff getroffen wurde.
		 * - shipDestroyedHistory = True oder false ob bei den letzten Angriffen ein Schiff zerstoert wurde.
		 */
		for(int i=3; i>0; i--){
			idHistory[i] = idHistory[i-1];
			hitHistory[i] = hitHistory[i-1];
			shipDestroyedHistory[i] = shipDestroyedHistory[i-1];
		}
		
		idHistory[0] = id;
		hitHistory[0] = hit;
		shipDestroyedHistory[0] = shipDestroyed;
	}
	
	private void initHistory(){
		//History initialisieren
		for(int i = 0; i<4; i++){
			idHistory[i] = 0;
			hitHistory[i] = false;
			shipDestroyedHistory[i] = false;
		}
	}
	
	private Ship[] createShips(){
		//Benoetigte Schiffe erstellen
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
	
	public void setShipDestroyedByLastAttack(boolean destroyed){
		this.shipDestroyedByLastAttack = destroyed;
	}
	
	public void setShipHitByLastAttack(boolean hit){
		this.shipHitByLastAttack = hit;
	}
	
	public ShipPlacement sp = new ShipPlacement(); //Nur zu Testzwecken als globale Variable
	private void setShips(Ship[] ships){
		//Schiffe platzieren
		sp = new ShipPlacement();
		sp.placeShips(myField, ships);
	}
	
	public int attack(){
		//Den Gegner attackieren
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
		
		//Werte reinigen
		shipDestroyedByLastAttack = false;
		shipHitByLastAttack = false;
		
		lastAttackedID = nextAttackID;
		
		//Ausgewaehltes FeldElement attackieren
		return nextAttackID;
	}
	
	private int getIDForContinueLastAttack2(){
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
	
	private int getIDForContinueLastAttack(){
		int ret = 0;
		//LastAttackedHistory implementieren
		if(shipHitByLastAttack){
			if(!shipDestroyedByLastAttack){
				if((lastAttackedID + 1) <= 100){
					if(enemiesField.getElementByID(lastAttackedID + 1).getAttacked()){
						if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 4 && enemiesField.getElementByID(lastAttackedID).getEdge(2) != 4 && myField.getElementByID(lastAttackedID - 1).getAttacked()){
							ret = lastAttackedID - 1;
						}
						if(enemiesField.getElementByID(lastAttackedID + 1).getEdge(1) != 3 && enemiesField.getElementByID(lastAttackedID).getEdge(2) != 3 && myField.getElementByID(lastAttackedID + 1).getAttacked()){
							ret = lastAttackedID + 2;
						}
					}
				}
				if((lastAttackedID - 1) > 0){
					if(enemiesField.getElementByID(lastAttackedID - 1).getAttacked()){
						if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 3 && enemiesField.getElementByID(lastAttackedID).getEdge(2) != 3 && myField.getElementByID(lastAttackedID + 1).getAttacked()){
							ret = lastAttackedID + 1;
						}
						if(enemiesField.getElementByID(lastAttackedID - 1).getEdge(1) != 4 && enemiesField.getElementByID(lastAttackedID).getEdge(2) != 4 && myField.getElementByID(lastAttackedID - 1).getAttacked()){
							ret = lastAttackedID - 2;
						}
					}
				}
				if((lastAttackedID + 10) <= 100){
					if(enemiesField.getElementByID(lastAttackedID + 10).getAttacked()){
						if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 1 && enemiesField.getElementByID(lastAttackedID).getEdge(2) != 1 && myField.getElementByID(lastAttackedID - 10).getAttacked()){
							ret = lastAttackedID - 10;
						}
						if(enemiesField.getElementByID(lastAttackedID + 10).getEdge(1) != 2 && enemiesField.getElementByID(lastAttackedID).getEdge(2) != 2 && myField.getElementByID(lastAttackedID + 10).getAttacked()){
							ret = lastAttackedID + 20;
						}
					}
				}
				if((lastAttackedID - 10) > 0){
					if(enemiesField.getElementByID(lastAttackedID - 10).getAttacked()){
						if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 2 && enemiesField.getElementByID(lastAttackedID).getEdge(2) != 2 && myField.getElementByID(lastAttackedID + 10).getAttacked()){
							ret = lastAttackedID + 10;
						}
						if(enemiesField.getElementByID(lastAttackedID - 10).getEdge(1) != 1 && enemiesField.getElementByID(lastAttackedID).getEdge(2) != 1 && myField.getElementByID(lastAttackedID - 10).getAttacked()){
							ret = lastAttackedID - 20;
						}
					}
				}
				
				if(ret == 0){
					Random random = new Random();
					int counter = 0;
					do{
						int randomInt = random.nextInt(3) + 1;
						
						if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != randomInt && enemiesField.getElementByID(lastAttackedID).getEdge(2) != randomInt){
							if(randomInt == 1){
								if(!enemiesField.getElementByID(lastAttackedID - 10).getAttacked()){
									ret = lastAttackedID - 10;
								}
							}
							if(randomInt == 2){
								if(!enemiesField.getElementByID(lastAttackedID + 10).getAttacked()){
									ret = lastAttackedID + 10;
								}
							}
							if(randomInt == 3){
								if(!enemiesField.getElementByID(lastAttackedID + 1).getAttacked()){
									ret = lastAttackedID + 1;
								}
							}
							if(randomInt == 4){
								if(!enemiesField.getElementByID(lastAttackedID - 1).getAttacked()){
									ret = lastAttackedID - 1;
								}
							}
						}
						
						counter++;
					}while(ret == 0 && counter < 4);
				}
			}
		}
		
		return ret;
	}
}

