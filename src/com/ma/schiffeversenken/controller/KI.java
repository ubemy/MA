package com.ma.schiffeversenken.controller;

import java.util.Random;

import com.ma.schiffeversenken.model.*;

public class KI {
	Block blöcke[];
	
	public KI(){
		blöcke = new Block[25];
	}
	
	public void platziereSchiffe(Spielfeld feld, Schiff[] schiffe){
		//Platziert die Schiffe automatisch auf dem Spielfeld
		blöckeErstellen(feld);
		Random random = new Random();
		
		for(Schiff schiff:schiffe){
			int ran = 0;
			int horver = 0;
			int run=0;
			
			
			do{
				ran = random.nextInt(100);
			}while(!checkBlock(ran, schiff));
			
			do{
				horver = random.nextInt(3);
				run++;
			}while(!checkEinheit(horver, schiff, ran, feld) && run<4);
			
		}
		
		
		
	}
	
	private boolean checkEinheit(int horver, Schiff schiff, int id, Spielfeld feld){
		int size = schiff.getSize();
		boolean ret = true;
		
		if(!feld.getElementByID(id).getBelegt()){
			if(horver == 0){
				//Nach rechts gerichtetes Schiff
				checkID(1, size, id, feld);
			}
			else if(horver == 1){
				//Nach oben gerichtetes Schiff
				checkID(-10, size, id, feld);
			}
			else if(horver == 2){
				//Nach links gerichtetes Schiff
				checkID(-1, size, id, feld);
			}
			else if(horver == 3){
				//Nach unten gerichtetes Schiff
				checkID(10, size, id, feld);
			}
		}
		
		return ret;
	}
	
	private boolean checkID(int counter, int size, int id, Spielfeld feld){
		boolean ret = true;
		for(int i=0;i<size;i=i+counter){
			if(i>0 && i<101){
				if(feld.getElementByID(id+i).getBelegt()) ret = false;
			}
			else ret=false;
		}
		return ret;
	}
	
	private Block getBlockById(int id){
		for(Block b : blöcke){
			for(int i:b.getFelder()){
				if(i==id){
					return b;
				}
			}
		}
		return null;
	}
	
	private boolean checkBlock(int random, Schiff schiff){
		boolean ret = false;
		
		if(getBlockById(random).getBelegt()) ret = true;
		
		return ret;
	}
	
	private void blöckeErstellen(Spielfeld feld){
		//Teil das Spielfeld in 25 identische Blöcke auf
		int k=0;
		FeldElement[][] einheiten = feld.getEinheiten();
		for(int i=0; i<10; i=i+2){
			for(int j=0; j<10; j=j+2){
				blöcke[k] = new Block(einheiten[i][j].getID(),
						einheiten[i][j+1].getID(),
						einheiten[i+1][j].getID(),
						einheiten[i+1][j+1].getID());
			}
		}
	}
}

