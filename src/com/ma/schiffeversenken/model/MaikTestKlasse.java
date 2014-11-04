package com.ma.schiffeversenken.model;



public class MaikTestKlasse {
	//Eine Klasse zum testen
	
	public MaikTestKlasse(){
		Schiff[] fleet =
		    {
		      new Kreuzer("E"),
		      new Uboot("f")
		    };
		
		Zerstörer z = new Zerstörer("Zerstörer");
		z.getName();
	}
	
}
