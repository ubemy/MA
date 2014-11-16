package com.ma.schiffeversenken.model;

public class Field {
	/*
	 * einheiten = Das Spielfeld besteht aus 10x10 Einheiten
	 * einheiten[y-Achse (Zeile)][x-Achse (Spalte)]
	 */
	FieldUnit[][] units = new FieldUnit[10][10];
	Ship[] placedShips;
	/*
	 * typ = Gibt den Typ des Spielfelds an.
	 * Eigenes Spielfeld = 0;
	 * Gegnerisches Spielfeld = 1; 
	 */
	int typ;
	
	public Field(int typ){
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
	
	public void setShips(Ship[] ships){
		this.placedShips = ships;
	}
	
	public Ship[] getShips(){
		return this.placedShips;
	}
	
	public FieldUnit[][] getFieldUnits(){
		return units;
	}
	
	public FieldUnit getElementByID(int id){
		for (int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				if(units[i][j].getID() == id) return units[i][j];
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
				units[i][j] = new FieldUnit(id);
			}
		}
	}
	
	private void createKante(){
		//Markiert, dass dieses FeldElement an einer Kante platziert ist
		for (int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				FieldUnit e = units[i][j];
				int id = e.getID();
				if(id < 11){
					//Obere Kante
					if(e.getEdge(1) == 0) e.setEdge(1, 1);
					else e.setEdge(2, 1);
				}
				if(id > 89){
					//Untere Kante
					if(e.getEdge(1) == 0) e.setEdge(1, 2);
					else e.setEdge(2, 2);
				}
				if((id-(10*(i+1))) == 0){
					//Rechte Kante
					if(e.getEdge(1) == 0) e.setEdge(1, 3);
					else e.setEdge(2, 3);
				}
				if((id-(10*i)) == 1){
					//Linke Kante
					if(e.getEdge(1) == 0) e.setEdge(1, 4);
					else e.setEdge(2, 4);
				}
			}
		}
	}
	
	private void createNeighbors(){
		//Weist jedem FeldElement seine direkten Nachbarn zu
		for (int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				FieldUnit lNeighbor = null, rNeighbor = null, oNeighbor = null, uNeighbor = null;
				
				if(i>1 && i<10){
					oNeighbor = units[i-1][j];
				}	
				if(i>0 && i<9){
					uNeighbor = units[i+1][j];
				}
				
				
				if(j>1 && j<10){
					lNeighbor = units[i][j-1];
				}
				
				if(j>0 && j<9){
					rNeighbor = units[i][j+1];
				}
				
				
				units[i][j].setNeighbors(lNeighbor, rNeighbor, oNeighbor, uNeighbor);
			}
		}
	}
}
