package com.ma.schiffeversenken.controller;

import java.util.Random;

import com.ma.schiffeversenken.model.Block;
import com.ma.schiffeversenken.model.*;
import com.ma.schiffeversenken.model.Field;

public class ShipPlacement {
	/*
	 * Steuer das automatische Platzieren der Schiffe
	 * für beide Spieler
	 */
	Block blocks[];
	Field t;
	
	public ShipPlacement() {
		blocks = new Block[25];
	}
	
	public void placeShips(Field field, Ship[] ships){
		this.t = field;
		//Platziert die Schiffe automatisch auf dem Spielfeld
		createBlocks(field);
		Random random = new Random();
		field.setShips(ships);
		for(Ship ship:ships){
			int randomID = 0;
			int horver = 0;
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
					horver = random.nextInt(3);
					run++;
					ok = checkEinheit(horver, ship, randomID, field);
				}while(!ok && run<4);
			}while(!ok);
			
			//Wenn die gewuenschten Felder frei sind, kann hier jetzt das 
			//Schiff platziert werden..
			field.getElementByID(randomID).setOccupied(true);
			FieldUnit tempElement = field.getElementByID(randomID);
			tempElement.setOccupied(true);
			//TODO: X\Y Koordinate an Schiff uebergeben, wenn belegt
			tempElement.placeShip(ship);
			ship.setStandort(tempElement, 0);
			int schiffSize=ship.getSize();
			
			if(schiffSize>1){
				if(horver == 0){
					markElements(1, schiffSize, randomID, field, ship);
				}
				else if(horver == 1){
					markElements(-10, schiffSize, randomID, field, ship);
				}
				else if(horver == 2){
					markElements(-1, schiffSize, randomID, field, ship);
				}
				else if(horver == 3){
					markElements(10, schiffSize, randomID, field, ship);
				}
			}
		}
	}
	
	private void markElements(int counter, int size, int id, Field field, Ship ship){
		int temp=0;
		int finalCounter = 0;
		for(int i=1;i<size;i++){
			finalCounter = counter*i;
			temp=id+finalCounter;
			if(temp>0 && temp<101){
				FieldUnit tempElement = field.getElementByID(temp);
				tempElement.setOccupied(true);
				tempElement.placeShip(ship);
				ship.setStandort(tempElement, i);
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
	
	private boolean checkEdge(int counter, FieldUnit element){
		int kRichtung = 0;
		boolean ret = false;
		int id = element.getID();
		if((id>0 && id<11) ||
				(id>89 && id<101) ||
				id==11 || id==21 || id==31 || id==41 || id==51 || id==61 ||
				id==71 || id==81 || id==20 || id==30 || id==40 || id==50 ||
				id==60 || id==70 || id==80){
			String test="Hallo";
		}
		
		if(counter == 1) kRichtung = 3;
		else if(counter == -1) kRichtung = 4;
		else if(counter == 10) kRichtung = 2;
		else if(counter == -10) kRichtung = 1;
		
		if(element.getEdge(1) == kRichtung || element.getEdge(2) == kRichtung) ret = true;
		
		return ret;
	}
	
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
	
	private void createBlocks(Field feld){
		//Teil das Spielfeld in 25 identische Blï¿½cke auf
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
