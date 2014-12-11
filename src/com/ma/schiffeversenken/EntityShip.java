package com.ma.schiffeversenken;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class EntityShip extends Sprite {

	/** Bewegung */
	private Vector2 velocity = new Vector2();

	private float speed = 60 * 2, gravity = 60 * 1.8f;

	TiledMapTileLayer collisionLayer;

	public EntityShip(Sprite sprite, TiledMapTileLayer c) {
		// Übergabe des Sprite, wie das Schiff aussehen soll.
		super(sprite);
		// Übergabe des ColissionLayer
		collisionLayer = c;
	}

	@Override
	public void draw(Batch batch) {
		// Beim Zeichnen wird update vorher aufgerufen mit xpos und ypos
		// aktualisiert
		update(Gdx.graphics.getDeltaTime());

		super.draw(batch);
	}

	private void update(float deltaTime) {
//		// Bewegung erzeugen, Gravity nach unten mal die zeit
//		velocity.y -= gravity * deltaTime;
//		// Gravity abfangen um nicht ins extreme zu gehen.
//		// Wenn zu schnell dann auf speedlimit setzen
//		if (velocity.y > speed) {
//			velocity.y = speed;
//		} else if (velocity.y < speed) {
//			velocity.y = -speed;
//		}

		// IDEA of colision detection
		/*
		 * alte koordinaten und neue koordinaten die neuen koordinaten werden
		 * geprüft auf kolisition wenn die neuen koordinaten ungültig sind dann
		 * sollen die alten koordinaten genommen werden andernfalls werden die
		 * neuen überprüften koordinaten übergeben als aktuelle position.
		 * 
		 * Für die performance geht man hin und betrachtet nur die Bereiche von
		 * der Map die jeweils in der Richtung in der gegangen wird liegen.
		 * undzwar die 3 direkt angrenzenden tiles
		 */

//		// alte pos speichern
//		float oldX = getX(), oldY = getY(), tileWidth = collisionLayer
//				.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
//		boolean collisionX = false, collisionY = false;

		// zu neuer Position x wandern.
		setX(getX() + velocity.x * deltaTime);

//		// prüfen ob wir nach rechts oder links gehen
//		if (velocity.x < 0) {
//			// richtung links
//			// top left
//			// true/false |= true/false, getCell at position of
//			// Entity,getTile,getpropertys,contains(blocked)
//			collisionX = collisionLayer
//					.getCell((int) (getX() / tileWidth),
//							(int) (getY() + getHeight() / tileHeight))
//					.getTile().getProperties().containsKey("blocked");
//
//			// middle left
//			if (!collisionX)
//				collisionX = collisionLayer
//						.getCell((int) (getX() / tileWidth),
//								(int) ((getY() + getHeight() / 2) / tileHeight))
//						.getTile().getProperties().containsKey("blocked");
//
//			// bottom left
//			if (!collisionX)
//				collisionX = collisionLayer
//						.getCell((int) (getX() / tileWidth),
//								(int) (getY() / tileHeight)).getTile()
//						.getProperties().containsKey("blocked");
//		} else if (velocity.x > 0) {
//			// richtung rechts
//			// top rigth
//			collisionX = collisionLayer
//					.getCell((int) ((getX() + getWidth()) / tileWidth),
//							(int) ((getY() + getHeight()) / tileHeight))
//					.getTile().getProperties().containsKey("blocked");
//			// middle right
//			if (!collisionX)
//				collisionX = collisionLayer
//						.getCell((int) ((getX() + getWidth()) / tileWidth),
//								(int) ((getY() + getHeight() / 2) / tileHeight))
//						.getTile().getProperties().containsKey("blocked");
//
//			// bottom right
//			if (!collisionX)
//				collisionX = collisionLayer
//						.getCell((int) ((getX() + getWidth()) / tileWidth),
//								(int) ((getY()) / tileHeight)).getTile()
//						.getProperties().containsKey("blocked");
//		}
//
//		//Reaktion auf collisionX. Übergeben wir bei Kollision die alte xpos
//		if(collisionX){
//			setX(oldX);
//			velocity.x=0;//um nicht gegen die Wand zu fahren
//		}
		
		
		// zu neuer Position > wandern.
		setY(getY() + velocity.y * deltaTime);

//		// prüfen ob wir nach oben oder unten gehen
//		if (velocity.y < 0) {
//			// richtung unten
//			// bottom left
//			collisionY = collisionLayer
//					.getCell((int) (getX() / tileWidth),
//							(int) (getY() / tileHeight)).getTile()
//					.getProperties().containsKey("blocked");
//			// bottom middle
//			if (!collisionY)
//				collisionY = collisionLayer
//						.getCell((int) ((getX() + getWidth() / 2) / tileWidth),
//								(int) (getY() / tileHeight)).getTile()
//						.getProperties().containsKey("blocked");
//			// bottom right
//			if (!collisionY)
//				collisionY = collisionLayer
//						.getCell((int) ((getX() + getWidth()) / tileWidth),
//								(int) (getY() / tileHeight)).getTile()
//						.getProperties().containsKey("blocked");
//		} else if (velocity.y > 0) {
//			// richtung oben
//			// top left
//			collisionY = collisionLayer
//					.getCell((int) (getX() / tileWidth),
//							(int) ((getY() + getHeight()) / tileHeight))
//					.getTile().getProperties().containsKey("blocked");
//			// top middle
//			if (!collisionY)
//				collisionY = collisionLayer
//						.getCell((int) ((getX() + getWidth() / 2) / tileWidth),
//								(int) ((getY() + getHeight()) / tileHeight))
//						.getTile().getProperties().containsKey("blocked");
//			// top right
//			if (!collisionY)
//				collisionY = collisionLayer
//						.getCell((int) ((getX() + getWidth()) / tileWidth),
//								(int) ((getY() + getHeight()) / tileHeight))
//						.getTile().getProperties().containsKey("blocked");
//
//		}
//		
//
//		//Reaktion auf collisionY. Übergeben wir bei Kollision die alte xpos
//		if(collisionY){
//			setY(oldY);
//			velocity.x=0;//um nicht gegen die Wand zu fahren
//		}
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}
	
	
}
