package com.ma.schiffeversenken.android.model;

public class Block {
	int fieldUnits[];
	boolean occupied;
	
	public Block(int field1, int field2, int field3, int field4){
		fieldUnits = new int[]{field1, field2, field3, field4};
		occupied = false;
	}
	
	public int[] getFieldUnits(){
		return fieldUnits;
	}
	
	public boolean getOccupied(){
		return occupied;
	}
}
