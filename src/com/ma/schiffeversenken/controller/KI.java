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
	
	public KI(Field feld){
		this.myField = feld;
		this.shipDestroyedByLastAttack = false;
		this.shipHitByLastAttack = false;
		this.lastAttackedID = 0;
		setShips(createShips());
	}
	
	private Ship[] createShips(){
		//Benoetigte Schiffe erstellen
		Ship[] schiffe = new Ship[]{new Submarine("Uboot"),
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
		
		return schiffe;
	}
	
	public void setShipDestroyedByLastAttack(boolean destroyed){
		this.shipDestroyedByLastAttack = destroyed;
	}
	
	public void setShipHitByLastAttack(boolean hit){
		this.shipHitByLastAttack = hit;
	}
	
	private void setShips(Ship[] ships){
		//Schiffe platzieren
		ShipPlacement sp = new ShipPlacement();
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
				nextAttackID = random.nextInt(100);
			}while((nextAttackID == 0) && enemiesField.getElementByID(nextAttackID).getAttacked());
		}
		
		//Werte reinigen
		shipDestroyedByLastAttack = false;
		shipHitByLastAttack = false;
		
		//Ausgewaehltes FeldElement attackieren
		return nextAttackID;
	}
	
	private int getIDForContinueLastAttack(){
		int ret = 0;
		
		if(shipHitByLastAttack){
			if(!shipDestroyedByLastAttack){				
				if(enemiesField.getElementByID(lastAttackedID + 1).getPlacedShip().getDestroyed()){
					if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 4 && myField.getElementByID(lastAttackedID).getEdge(2) != 4 && myField.getElementByID(lastAttackedID - 1).getAttacked()){
						ret = lastAttackedID - 1;
					}
					if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 3 && myField.getElementByID(lastAttackedID).getEdge(2) != 3 && myField.getElementByID(lastAttackedID + 1).getAttacked()){
						ret = lastAttackedID + 1;
					}
				}
				else if(enemiesField.getElementByID(lastAttackedID - 1).getPlacedShip().getDestroyed()){
					if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 3 && myField.getElementByID(lastAttackedID).getEdge(2) != 3 && myField.getElementByID(lastAttackedID + 1).getAttacked()){
						ret = lastAttackedID + 1;
					}
					if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 4 && myField.getElementByID(lastAttackedID).getEdge(2) != 4 && myField.getElementByID(lastAttackedID - 1).getAttacked()){
						ret = lastAttackedID - 1;
					}
				}
				else if(enemiesField.getElementByID(lastAttackedID + 10).getPlacedShip().getDestroyed()){
					if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 1 && myField.getElementByID(lastAttackedID).getEdge(2) != 1 && myField.getElementByID(lastAttackedID - 10).getAttacked()){
						ret = lastAttackedID - 10;
					}
					if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 2 && myField.getElementByID(lastAttackedID).getEdge(2) != 2 && myField.getElementByID(lastAttackedID + 10).getAttacked()){
						ret = lastAttackedID + 10;
					}
				}
				else if(enemiesField.getElementByID(lastAttackedID - 10).getPlacedShip().getDestroyed()){
					if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 2 && myField.getElementByID(lastAttackedID).getEdge(2) != 2 && myField.getElementByID(lastAttackedID + 10).getAttacked()){
						ret = lastAttackedID + 10;
					}
					if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != 1 && myField.getElementByID(lastAttackedID).getEdge(2) != 1 && myField.getElementByID(lastAttackedID - 10).getAttacked()){
						ret = lastAttackedID - 10;
					}
				}
				else{
					Random random = new Random();
					int randomInt = random.nextInt(3) + 1;
					
					if(enemiesField.getElementByID(lastAttackedID).getEdge(1) != randomInt && myField.getElementByID(lastAttackedID).getEdge(2) != randomInt){
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
				}
			}
		}
		
		return ret;
	}
}

