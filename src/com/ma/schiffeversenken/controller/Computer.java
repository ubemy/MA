package com.ma.schiffeversenken.controller;

import java.util.Random;

import com.ma.schiffeversenken.model.*;

public class Computer {
	//Spiellogik des Computer Gegners
	private Spielfeld feld;
	private boolean shipDestroyedByLastAttack;
	private boolean shipHitByLastAttack;
	private int lastAttackedID;
	
	public Computer(Spielfeld feld){
		this.feld = feld;
		this.shipDestroyedByLastAttack = false;
		this.shipHitByLastAttack = false;
		this.lastAttackedID = 0;
		setShips();
	}
	
	private void setShips(){
		//Schiffe platzieren
	}
	
	private void attack(){
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
			}while((nextAttackID == 0) && fieldAlreadyAttacked(nextAttackID));
		}
		
		//Ausgewaehltes FeldElement attackieren
		FeldElement fe = feld.getElementByID(nextAttackID);
		fe.attack();
	}
	
	private int getIDForContinueLastAttack(){
		int ret = 0;
		
		if(shipHitByLastAttack){
			if(!shipDestroyedByLastAttack){				
				if(feld.getElementByID(lastAttackedID + 1).getShipDestroyedBySecondPlayer()){
					ret = lastAttackedID - 1;
				}
				if(feld.getElementByID(lastAttackedID - 1).getShipDestroyedBySecondPlayer()){
					ret = lastAttackedID + 1;
				}
				if(feld.getElementByID(lastAttackedID + 10).getShipDestroyedBySecondPlayer()){
					ret = lastAttackedID - 10;
				}
				if(feld.getElementByID(lastAttackedID - 10).getShipDestroyedBySecondPlayer()){
					ret = lastAttackedID + 10;
				}
			}
		}
		
		return ret;
	}
	
	private boolean fieldAlreadyAttacked(int id){
		/*
		 * Ueberprueft ob das Feld mit der angegebenen ID bereits vom Computer attackiert wurde
		 */
		boolean ret = false;
		FeldElement f = feld.getElementByID(id);
		
		if(f.getAttackedBySecondPlayer()) ret = true;
		
		return ret;
	}
}
