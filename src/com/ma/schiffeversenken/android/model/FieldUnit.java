package com.ma.schiffeversenken.android.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Das Spielfeld besteht aus 100 FeldElementen
 * @author Maik
 * @author Klaus
 */
public class FieldUnit {
	FieldUnit lNeighbor, rNeighbor, oNeighbor, uNeighbor; //Direkte Nachbar dieses Feldes
	Ship placedShip; //Schiff, das auf diesem FeldElement steht
	int id;
	boolean occupied;
	int edge1=0;
	int edge2=0;
	private boolean attacked;
	/**Segment des Schiffs: 0=Vorderteil, 1=Mittelteil, 2=Hinterteil*/
	private int shipSegment;
	
	//OpenGL Elemente
	private TextureRegion drawFeld;
	private Sprite sprite;
	private String textureName;
	private float xpos;
	private float ypos;
	
	public FieldUnit(int id, float xpos, float ypos){
		this.id=id;
		this.occupied = false;
		this.attacked = false;
		this.placedShip=null;
		this.xpos=xpos;
		this.ypos=ypos;
	}
	
	public int getShipSegment(){
		return this.shipSegment;
	}
	
	public void setAttacked(boolean attacked){
		this.attacked = attacked;
	}
	
	public boolean getAttacked(){
		return this.attacked;
	}
	
	public void setEdge(int edge, int value){
		if(edge == 1) this.edge1 = value;
		else if(edge == 2) this.edge2 = value;
	}
	
	public int getEdge(int edge){
		int ret = 0;
		
		if(edge == 1) ret = this.edge1;
		else if(edge == 2) ret = this.edge2;
		
		return ret;
	}

	public void setNeighbors(FieldUnit lNeighbor, FieldUnit rNeighbor, FieldUnit oNeighbor, FieldUnit uNeighbor){
		//Weist diesem FeldElement seine direkten Nachbarn zu
		this.lNeighbor = lNeighbor;
		this.rNeighbor = rNeighbor;
		this.oNeighbor = oNeighbor;
		this.uNeighbor = uNeighbor;
	}
	
	public int getID(){
		return id;
	}
	
	public void placeShip(Ship ship, int shipSegment){
		this.placedShip = ship;
		this.shipSegment = shipSegment;
	}
	
	public Ship getPlacedShip(){
		if(occupied){
			return this.placedShip;
		}
		else{
			return null;
		}
	}
	
	public boolean getOccupied(){
		return occupied;
	}
	
	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}
	
/**
 * TODO Maik nach den variablen fragen und Abklären wegen Zeichnen.
 * @param batch ist die SpriteBach die fürs Zeichnen übergeben wird.
 * @param atlas hält alle benötigten Texturelemente mit namen
 */
	public void draw (SpriteBatch batch, TextureAtlas atlas) {
		//TODO Auslesen ob das Schiff Horizontal oder Vertikal steht
		if(getAttacked()&&getPlacedShip()!=null){
			//TODO wenn Front
			textureName="shipfrontattacked";
			//TODO wenn Mittelteil
			textureName="shipmiddleattacked";
			//TODO wenn Rumpf
			textureName="shipbackattacked";
		} else if(!getAttacked()&&getPlacedShip()!=null) {
			//TODO wenn Front
			textureName="shipfront";
			//TODO wenn Mittelteil
			textureName="shipmiddle";
			//TODO wenn Rumpf
			textureName="shipback";
		}else if(getAttacked()&&getPlacedShip()==null){
			textureName="waterattacked";
		}else{
			textureName="water";
		}
		drawFeld = atlas.findRegion(textureName);
		sprite = new Sprite(drawFeld);
		sprite.setSize(1, 1 * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(xpos,ypos);
		sprite.draw(batch);
		//TODO Testen was besser ist über batch oder sprite das Zeichen
//		batch.draw(drawFeld, xpos, ypos);
	}
}
