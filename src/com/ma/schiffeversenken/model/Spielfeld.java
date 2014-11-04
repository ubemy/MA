package com.ma.schiffeversenken.model;

public class Spielfeld {
	/*
	 * einheiten = Das Spielfeld besteht aus 10x10 Einheiten
	 * einheiten[y-Achse (Zeile)][x-Achse (Spalte)]
	 */
	FeldElement[][] einheiten = new FeldElement[10][10];
	
	/*
	 * typ = Gibt den Typ des Spielfelds an.
	 * Eigenes Spielfeld = 0;
	 * Gegnerisches Spielfeld = 1; 
	 */
	int typ;
	
	public Spielfeld(int typ){
		this.typ = typ;
		create();
		createNeighbors();
	}
	
	private void create(){
		//Erstellt das Spielfeld, das aus 10x10 Feldelementen besteht
		for (int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				einheiten[i][j] = new FeldElement();
			}
		}
	}
	
	private void createNeighbors(){
		//Weist jedem FeldElement seine direkten Nachbarn zu
		for (int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				FeldElement lNachbar = null, rNachbar = null, oNachbar = null, uNachbar = null;
				
				if(i>0){
					oNachbar = einheiten[i-1][j];
					
					if(i<10){
						uNachbar = einheiten[i+1][j];
					}
				}
				
				if(j>0){
					lNachbar = einheiten[i][j-1];
					
					if(j<10){
						rNachbar = einheiten[i][j+1];
					}
				}
				
				einheiten[i][j].setNeighbors(lNachbar, rNachbar, oNachbar, uNachbar);
			}
		}
	}
}
