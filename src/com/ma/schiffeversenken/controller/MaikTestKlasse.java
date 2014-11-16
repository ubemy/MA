package com.ma.schiffeversenken.controller;

import com.ma.schiffeversenken.model.*;



public class MaikTestKlasse {
	public MaikTestKlasse(){
		//Eine Klasse zum testen
		
		Destroyer z = new Destroyer("Zerstoerer");
		Submarine u = new Submarine("Uboot");
		
		Ship[] schiffe = new Ship[]{z, u};
		Field feld = new Field(0);
		
		ShipPlacement sp = new ShipPlacement();
		sp.placeShips(feld, schiffe);
	}
}
