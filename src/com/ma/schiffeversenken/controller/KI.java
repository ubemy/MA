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
	private Spielfeld feld;
	private boolean shipDestroyedByLastAttack;
	private boolean shipHitByLastAttack;
	private int lastAttackedID;
	
	public KI(Spielfeld feld){
		this.feld = feld;
		this.shipDestroyedByLastAttack = false;
		this.shipHitByLastAttack = false;
		this.lastAttackedID = 0;
		setShips(createShips());
	}
	
	private Schiff[] createShips(){
		//Benoetigte Schiffe erstellen
		Schiff[] schiffe = new Schiff[]{new Uboot("Uboot"),
				new Uboot("Uboot"),
				new Uboot("Uboot"),
				new Kreuzer("Kreuzer"),
				new Kreuzer("Kreuzer"),
				new Kreuzer("Kreuzer"),
				new Kreuzer("Kreuzer"),
				new Zerstoerer("Zerstoerer"),
				new Zerstoerer("Zerstoerer"),
				new Schlachtschiff("Schlachtschiff")
				};
		
		return schiffe;
	}
	
	public void setShipDestroyedByLastAttack(boolean destroyed){
		this.shipDestroyedByLastAttack = destroyed;
	}
	
	public void setShipHitByLastAttack(boolean hit){
		this.shipHitByLastAttack = hit;
	}
	
	private void setShips(Schiff[] schiffe){
		//Schiffe platzieren
		ShipPlacement sp = new ShipPlacement();
		sp.platziereSchiffe(feld, schiffe);
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
			}while((nextAttackID == 0) && feld.getElementByID(nextAttackID).getAttackedBySecondPlayer());
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
				if(feld.getElementByID(lastAttackedID + 1).getShipDestroyedBySecondPlayer()){
					if(feld.getElementByID(lastAttackedID).getKante(1) != 4 && feld.getElementByID(lastAttackedID).getKante(2) != 4 && feld.getElementByID(lastAttackedID - 1).getAttackedBySecondPlayer()){
						ret = lastAttackedID - 1;
					}
					if(feld.getElementByID(lastAttackedID).getKante(1) != 3 && feld.getElementByID(lastAttackedID).getKante(2) != 3 && feld.getElementByID(lastAttackedID + 1).getAttackedBySecondPlayer()){
						ret = lastAttackedID + 1;
					}
				}
				else if(feld.getElementByID(lastAttackedID - 1).getShipDestroyedBySecondPlayer()){
					if(feld.getElementByID(lastAttackedID).getKante(1) != 3 && feld.getElementByID(lastAttackedID).getKante(2) != 3 && feld.getElementByID(lastAttackedID + 1).getAttackedBySecondPlayer()){
						ret = lastAttackedID + 1;
					}
					if(feld.getElementByID(lastAttackedID).getKante(1) != 4 && feld.getElementByID(lastAttackedID).getKante(2) != 4 && feld.getElementByID(lastAttackedID - 1).getAttackedBySecondPlayer()){
						ret = lastAttackedID - 1;
					}
				}
				else if(feld.getElementByID(lastAttackedID + 10).getShipDestroyedBySecondPlayer()){
					if(feld.getElementByID(lastAttackedID).getKante(1) != 1 && feld.getElementByID(lastAttackedID).getKante(2) != 1 && feld.getElementByID(lastAttackedID - 10).getAttackedBySecondPlayer()){
						ret = lastAttackedID - 10;
					}
					if(feld.getElementByID(lastAttackedID).getKante(1) != 2 && feld.getElementByID(lastAttackedID).getKante(2) != 2 && feld.getElementByID(lastAttackedID + 10).getAttackedBySecondPlayer()){
						ret = lastAttackedID + 10;
					}
				}
				else if(feld.getElementByID(lastAttackedID - 10).getShipDestroyedBySecondPlayer()){
					if(feld.getElementByID(lastAttackedID).getKante(1) != 2 && feld.getElementByID(lastAttackedID).getKante(2) != 2 && feld.getElementByID(lastAttackedID + 10).getAttackedBySecondPlayer()){
						ret = lastAttackedID + 10;
					}
					if(feld.getElementByID(lastAttackedID).getKante(1) != 1 && feld.getElementByID(lastAttackedID).getKante(2) != 1 && feld.getElementByID(lastAttackedID - 10).getAttackedBySecondPlayer()){
						ret = lastAttackedID - 10;
					}
				}
				else{
					Random random = new Random();
					int randomInt = random.nextInt(3) + 1;
					
					if(feld.getElementByID(lastAttackedID).getKante(1) != randomInt && feld.getElementByID(lastAttackedID).getKante(2) != randomInt){
						if(randomInt == 1){
							if(!feld.getElementByID(lastAttackedID - 10).getAttackedBySecondPlayer()){
								ret = lastAttackedID - 10;
							}
						}
						if(randomInt == 2){
							if(!feld.getElementByID(lastAttackedID + 10).getAttackedBySecondPlayer()){
								ret = lastAttackedID + 10;
							}
						}
						if(randomInt == 3){
							if(!feld.getElementByID(lastAttackedID + 1).getAttackedBySecondPlayer()){
								ret = lastAttackedID + 1;
							}
						}
						if(randomInt == 4){
							if(!feld.getElementByID(lastAttackedID - 1).getAttackedBySecondPlayer()){
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

