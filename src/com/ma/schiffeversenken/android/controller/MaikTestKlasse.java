package com.ma.schiffeversenken.android.controller;

import com.ma.schiffeversenken.android.model.*;



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
