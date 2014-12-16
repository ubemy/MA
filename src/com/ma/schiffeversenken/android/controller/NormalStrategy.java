package com.ma.schiffeversenken.android.controller;

import java.util.Random;

public class NormalStrategy implements KIStrategy{
	/**Das initialisierte KI Objekt*/
	KI ki;
	
	/**
	 * Erstellt ein NormalStrategy Objekt
	 * @param ki Das initialisierte KI Objekt
	 */
	public NormalStrategy(KI ki){
		this.ki = ki;
	}
	
	/**
	 * Den Gegner attackieren
	 * @return ID des Feldes, das attackiert werden soll
	 */
	@Override
	public int attack() {
		Random random = new Random();
		int nextAttackID = 0;
		int idForContinueLastAttack = ki.getIDForContinueLastAttack();
		
		if(idForContinueLastAttack != 0){
			//Den letzten Angriff fortfuehren
			nextAttackID = idForContinueLastAttack;
		}
		else{
			//Angriff auf eine neue zufaellige FeldID starten
			do{
				//Zufaellige Zahl erstellen
				nextAttackID = random.nextInt(99) + 1;
			}while(ki.getEnemiesField().getElementByID(nextAttackID).getAttacked());
		}
		
		//Ausgewaehltes FeldElement attackieren
		return nextAttackID;
	}
}
