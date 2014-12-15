package com.ma.schiffeversenken.android.controller;

import java.util.Random;

import com.ma.schiffeversenken.android.model.*;

/**
 * Steuert das automatische Platzieren der Schiffe
 * fuer beide Spiele
 * @author Maik Steinborn
 */
public class ShipPlacement {
	/**Das Spielfeld wird in 25 Bloecke aufgeteilt. Die Bloecke werden in dieser Variable gespeichert*/
	Block blocks[];
	/**Das Spielfeld*/
	Field t;
	
	/**
	 * Erstellt das ShipPlacement Objekt
	 */
	public ShipPlacement() {
		blocks = new Block[25];
	}
	
	/**
	 * Platziert die Schiffe automatisch auf dem Spielfeld
	 * Erst das Erstellen von ships, danach das Platzieren in field
	 * 
	 * @param field
	 * @param ships
	 */
	public void placeShips(Field field, Ship[] ships){
		this.t = field;

		createBlocks(field);
		Random random = new Random();
		//Erstellen der Schiffe danach erst im field setzen.
		for(Ship ship:ships){
			int randomID = 0;
			int orientation = 0;
			int run=0;
			boolean ok = false;
			
			do{
				do{
					//Pruefen ob der Block schon belegt ist.
					do{
						//Zufaellige Zahl erstellen
						randomID = random.nextInt(100);
					}while(randomID == 0);
				}while(!checkBlock(randomID, ship));
				
				do{
					//Pruefen ob das FeldElement schon belegt ist
					orientation = random.nextInt(3);
					run++;
					ok = checkEinheit(orientation, ship, randomID, field);
				}while(!ok && run<4);
			}while(!ok);
			
			//Wenn die gewuenschten Felder frei sind, kann hier jetzt das 
			//Schiff platziert werden..
			field.getElementByID(randomID).setOccupied(true);
			FieldUnit tempElement = field.getElementByID(randomID);
			tempElement.setOccupied(true);
			//TODO: X\Y Koordinate an Schiff uebergeben, wenn belegt
			tempElement.placeShip(ship, 2);
			ship.setStandort(tempElement, 0, orientation);
			int schiffSize=ship.getSize();
			
			if(schiffSize>1){
				if(orientation == 0){
					markElements(1, schiffSize, randomID, field, ship, orientation);
				}
				else if(orientation == 1){
					markElements(-10, schiffSize, randomID, field, ship, orientation);
				}
				else if(orientation == 2){
					markElements(-1, schiffSize, randomID, field, ship, orientation);
				}
				else if(orientation == 3){
					markElements(10, schiffSize, randomID, field, ship, orientation);
				}
			}
		}
		//Setzen von ships
		field.setShips(ships);
	}

	/**
	 * FeldElement als markiert markieren und das Schiff setzen
	 * @param counter Gibt an in welche Richtung das Schiff platziert wurde (oben, unten, rechts, links)
	 * @param size Größe des Schiffs
	 * @param id ID des Startfeldes
	 * @param field Spielfeld
	 * @param ship Das zu platzierende Schiff
	 * @param orientation Die Orientierung des Schiffs (nach oben/unten/rechts/links gerichtet)
	 */
	private void markElements(int counter, int size, int id, Field field, Ship ship, int orientation){
		int temp=0;
		int shipSegment = 1;
		int finalCounter = 0;
		for(int i=1;i<size;i++){
			finalCounter = counter*i;
			temp=id+finalCounter;
			if(temp>0 && temp<101){
				FieldUnit tempElement = field.getElementByID(temp);
				if(i==(size-1)) shipSegment = 0;
				tempElement.setOccupied(true);
				tempElement.placeShip(ship, shipSegment);
				ship.setStandort(tempElement, i, orientation);
			}
		}
	}
	
	public String print(){
		//Spielfeld mit platziertes Schiffen wird ausgegeben
		//Dient nur zum testen
		String ret="#############\r\n";
		for(int i=0;i<10;i++){
			ret += "#";
			for(int j=0;j<10;j++){
				FieldUnit temp = t.getFieldUnits()[i][j];
				if(temp.getOccupied()){
					try{
						if(temp.getPlacedShip().getName()=="Uboot")ret += "U";
						if(temp.getPlacedShip().getName()=="Schlachtschiff")ret += "S";
						if(temp.getPlacedShip().getName()=="Kreuzer")ret += "K";
						if(temp.getPlacedShip().getName()=="Zerstoerer")ret += "Z";
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
				}
				else{
					ret += "O";
				}
			}
			ret += "#\r\n";
		}
		ret += "#############\r\n";
		
		return ret;
	}
	
	/**
	 * Prüft ob die benötigten Felder frei oder belegt sind
	 * @param horver Gibt an in welcher Richtung das Schiff platziert werden soll (oben, unten, rechts, links)
	 * @param ship Das zu platzierende Schiff
	 * @param id Die ID des Startfeldes
	 * @param feld Spielfeld
	 * @return Gibt zurueck ob die Felder frei oder belegt sind
	 */
	private boolean checkEinheit(int horver, Ship ship, int id, Field feld){
		int size = ship.getSize();
		boolean ret = true;
		
		if(feld.getElementByID(id).getOccupied()){
			ret = false;
		}
		else {
			if(horver == 0){
				//Nach rechts gerichtetes Schiff
				ret = checkID(1, size, id, feld);
			}
			else if(horver == 1){
				//Nach oben gerichtetes Schiff
				ret = checkID(-10, size, id, feld);
			}
			else if(horver == 2){
				//Nach links gerichtetes Schiff
				ret = checkID(-1, size, id, feld);
			}
			else if(horver == 3){
				//Nach unten gerichtetes Schiff
				ret = checkID(10, size, id, feld);
			}
		}
		
		return ret;
	}
	
	/**
	 * Prüft eine Reihe von Feldern ob diese frei oder belegt sind
	 * @param counter Die ID wird mit dem counter multipliziert, um nach oben, unten, rechts oder links zu gehen
	 * @param size Groesse des Schiffs
	 * @param id ID des Startfeldes
	 * @param feld Spielfeld
	 * @return Gibt zurueck ob die Felder frei oder belegt sind
	 */
	private boolean checkID(int counter, int size, int id, Field feld){
		boolean ret = true;
		int temp=0;
		int finalCounter = 0;
		for(int i=1;i<=size;i++){
			finalCounter = counter*i;
			temp=id+finalCounter;
			if(temp>0 && temp<101){
				FieldUnit tempElement = feld.getElementByID(temp); 
				if(tempElement.getOccupied()) ret = false;
				if((size-i)>0){
					if(checkEdge(counter, tempElement)) ret = false;
				}
			}
			else ret=false;
		}
		return ret;
	}
	
	/**
	 * Prueft ob das Feld an einer Kante liegt
	 * @param counter Die ID wird mit dem counter multipliziert, um nach oben, unten, rechts oder links zu gehen
	 * @param element Das FeldElement, das uberprueft werden soll
	 * @return Gibt true zurueck, wenn das Element an einer Kante liegt
	 */
	private boolean checkEdge(int counter, FieldUnit element){
		int kRichtung = 0;
		boolean ret = false;
		int id = element.getID();
		if((id>0 && id<11) ||
				(id>89 && id<101) ||
				id==11 || id==21 || id==31 || id==41 || id==51 || id==61 ||
				id==71 || id==81 || id==20 || id==30 || id==40 || id==50 ||
				id==60 || id==70 || id==80){
		}
		
		if(counter == 1) kRichtung = 3;
		else if(counter == -1) kRichtung = 4;
		else if(counter == 10) kRichtung = 2;
		else if(counter == -10) kRichtung = 1;
		
		if(element.getEdge(1) == kRichtung || element.getEdge(2) == kRichtung) ret = true;
		
		return ret;
	}
	
	/**
	 * Sucht den passenden Block zu einer Feld-ID
	 * @param id Die ID, die gesucht wird
	 * @return Der Block, zu der gesuchten ID
	 */
	private Block getBlockById(int id){
		for(Block b : blocks){
			for(int i:b.getFieldUnits()){
				if(i==id){
					return b;
				}
			}
		}
		return null;
	}
	
	/**
	 * Prueft ob der Block belegt ist
	 * @param random Die ID des Blocks
	 * @param ship Das zu platzierende Schiff
	 * @return Gibt zurueck ob der Block frei oder belegt ist
	 */
	private boolean checkBlock(int random, Ship ship){
		boolean ret = true;
		
		try{
			if(getBlockById(random).getOccupied()) ret = false;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Teil das Spielfeld in 25 identische Bloecke auf
	 * @param feld Spielfeld
	 */
	private void createBlocks(Field feld){
		int k=0;
		FieldUnit[][] einheiten = feld.getFieldUnits();
		for(int i=0; i<10; i=i+2){
			for(int j=0; j<10; j=j+2){
				blocks[k] = new Block(einheiten[i][j].getID(),
						einheiten[i][j+1].getID(),
						einheiten[i+1][j].getID(),
						einheiten[i+1][j+1].getID());
				k++;
			}
		}
	}
}
