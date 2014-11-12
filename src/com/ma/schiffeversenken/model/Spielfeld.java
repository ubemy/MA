package com.ma.schiffeversenken.model;

public class Spielfeld {
	/*
	 * einheiten = Das Spielfeld besteht aus 10x10 Einheiten
	 * einheiten[y-Achse (Zeile)][x-Achse (Spalte)]
	 */
	FeldElement[][] einheiten = new FeldElement[10][10];
	Schiff[] placedShips;
	/*
	 * typ = Gibt den Typ des Spielfelds an.
	 * Eigenes Spielfeld = 0;
	 * Gegnerisches Spielfeld = 1; 
	 */
	int typ;
	
	public Spielfeld(int typ){
		try{
			this.typ = typ;
			create();
			createNeighbors();
			createKante();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void setShips(Schiff[] ships){
		this.placedShips = ships;
	}
	
	public Schiff[] getShips(){
		return this.placedShips;
	}
	
	public FeldElement[][] getEinheiten(){
		return einheiten;
	}
	
	public FeldElement getElementByID(int id){
		for (int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				if(einheiten[i][j].getID() == id) return einheiten[i][j];
			}
		}
		return null;
	}
	
	private void create(){
		//Erstellt das Spielfeld, das aus 10x10 Feldelementen besteht
		int id=0;
		
		for (int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				id++;
				einheiten[i][j] = new FeldElement(id);
			}
		}
	}
	
	private void createKante(){
		//Markiert, dass dieses FeldElement an einer Kante platziert ist
		for (int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				FeldElement e = einheiten[i][j];
				int id = e.getID();
				if(id < 11){
					//Obere Kante
					if(e.getKante(1) == 0) e.setKante(1, 1);
					else e.setKante(2, 1);
				}
				if(id > 89){
					//Untere Kante
					if(e.getKante(1) == 0) e.setKante(1, 2);
					else e.setKante(2, 2);
				}
				if((id-(10*(i+1))) == 0){
					//Rechte Kante
					if(e.getKante(1) == 0) e.setKante(1, 3);
					else e.setKante(2, 3);
				}
				if((id-(10*i)) == 1){
					//Linke Kante
					if(e.getKante(1) == 0) e.setKante(1, 4);
					else e.setKante(2, 4);
				}
			}
		}
	}
	
	private void createNeighbors(){
		//Weist jedem FeldElement seine direkten Nachbarn zu
		for (int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				FeldElement lNachbar = null, rNachbar = null, oNachbar = null, uNachbar = null;
				
				if(i>1 && i<10){
					oNachbar = einheiten[i-1][j];
				}	
				if(i>0 && i<9){
					uNachbar = einheiten[i+1][j];
				}
				
				
				if(j>1 && j<10){
					lNachbar = einheiten[i][j-1];
				}
				
				if(j>0 && j<9){
					rNachbar = einheiten[i][j+1];
				}
				
				
				einheiten[i][j].setNeighbors(lNachbar, rNachbar, oNachbar, uNachbar);
			}
		}
	}
}
