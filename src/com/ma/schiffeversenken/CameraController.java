package com.ma.schiffeversenken;

import java.util.ArrayList;

import android.media.CameraProfile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTile.BlendMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ma.schiffeversenken.android.controller.Game;
import com.ma.schiffeversenken.android.model.FieldUnit;
import com.ma.schiffeversenken.android.model.Player;

class CameraController implements GestureListener {
	float velX, velY;
	boolean flinging = false;
	float initialScale = 1;
	OrthographicCamera camera;
	float layerX, layerY, layerZoom;
	Game game;
	private float faktorX;
	private float faktorZoom;
	private float faktorY;
	private boolean panStart=false;
	 // 0=Intro, 1=FullView 2=FullView smooth, 3=GameFieldZoom, 4=PlayerShips, 5=EnemyShips
	ArrayList<Boolean> state;

	public CameraController(OrthographicCamera c, float mx, float my,
			float mzoom, Player player,ArrayList<Boolean> state) {
		this.state=state;
		this.camera = c;
		this.layerX = mx;
		this.layerY = my;
		this.layerZoom = mzoom;
		this.game = player.getGame();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		flinging = false;
		initialScale = camera.zoom;
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
//		Gdx.app.log("GestureDetectorTest", "tap at " + x + ", " + y
//				+ ", count: " + count);
		
		//Bildschirmkoordinaten transformieren zu Weldkoordinaten
		// touch.x und  touch.y mit count für anzahl der tap
		Vector3 touch = new Vector3(x, y, 0);
	    camera.unproject(touch);
	    
	    if(state.get(3)||state.get(3)&&!game.isEnd()){
//	    	game.touchEvent(touch.x, touch.y);
	    }
	    
	    FieldUnit unit = game.getFirstFieldPlayer().getElementByXPosYPos(touch.x, touch.y);
	    if(unit!=null){
	    	Gdx.app.log("GestureDetectorTest", "tap at " + touch.x + ", " + touch.y
	    			+ ", count: " + count);
	    	Gdx.app.log("tap", "unit Gefunden!");
	    	// Hinzufügen von Schiffsteil
			EntityShip tmpShip = new EntityShip(
					game.getFirstFieldPlayer().getShipTextures().get("rhk"),game.getFirstFieldPlayer().getShipTextures().get("rhka"), new Vector2(
							unit.getXpos(), unit.getYpos()),
					new Vector2(GameFieldScreen.size, GameFieldScreen.size), game.getFirstFieldPlayer().getMapTileLayer());
			unit.setEntityShipDrawUnit(tmpShip);
			unit.setOccupied(true);

	    	
	    }else{
	    	Gdx.app.log("GestureDetectorTest", "tap at " + touch.x + ", " + touch.y
	    			+ ", count: " + count);
	    	Gdx.app.log("tap", "unit nicht Gefunden!");   	
	    }
	    
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		Gdx.app.log("GestureDetectorTest", "long press at " + x + ", " + y);
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		flinging = true;
		velX = camera.zoom * velocityX * 0.5f;
		velY = camera.zoom * velocityY * 0.5f;
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
//		Gdx.app.log("GestureDetectorTest", "pan at " + x + ", " + y);
		if(!panStart){
			panStart=true;
			panStart(x, y, deltaX, deltaY);
		}
//		camera.position.add(-deltaX * camera.zoom, deltaY * camera.zoom, 0);
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
//		Gdx.app.log("GestureDetectorTest", "pan stop at " + x + ", " + y);
		panStart=false;
		
		//Bildschirmkoordinaten transformieren zu Weldkoordinaten
		Vector3 touch = new Vector3(x, y, 0);
	    camera.unproject(touch);
	    System.out.println("Stop Bildschirm zu Weltkoordinaten: "
	        + "X: " + touch.x + " Y: " + touch.y);
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean panStart(float x, float y, float deltaX, float deltaY) {
//		Gdx.app.log("GestureDetectorTest", "pan Start at " + x + ", " + y);
		if(state.get(3)){
			
		//TODO Auf Spielfeld Übersetzen die kamerakoordinaten.
//		game.getFirstFieldPlayer().getMapTileLayer().getCell((int) x,(int) y).setTile(game.getFirstFieldPlayer().getShipTiles().get("rhk"));
			
			//Bildschirmkoordinaten transformieren zu Weldkoordinaten
			Vector3 touch = new Vector3(x, y, 0);
		    camera.unproject(touch);
		    System.out.println("Start Bildschirm zu Weltkoordinaten: "
		        + "X: " + touch.x + " Y: " + touch.y);

			
			
		// Hinzufügen von Schiffsteil
		FieldUnit unit = game.getFirstFieldPlayer().getElementByXPosYPos(x, y);
		if(unit!=null){
			
		
			
		EntityShip tmpShip = new EntityShip(
				game.getFirstFieldPlayer().getShipTextures().get("rhk"),game.getFirstFieldPlayer().getShipTextures().get("rhk"+"a"), new Vector2(
						unit.getXpos(), unit.getYpos()),
				new Vector2(GameFieldScreen.size, GameFieldScreen.size), game.getFirstFieldPlayer().getMapTileLayer());
		unit.setEntityShipDrawUnit(tmpShip);
		}else{
			Gdx.app.log("GameField", "Kein GamefieldGefunden bei pan Start");
			Gdx.app.log("ID 1 Unit", "Koordinaten: x"+game.getFirstFieldPlayer().getElementByID(1).getXpos()+" y"+game.getFirstFieldPlayer().getElementByID(1).getYpos());
		}	
		}
		return false;
	}

	@Override
	public boolean zoom(float originalDistance, float currentDistance) {
		if (camera.zoom <= layerZoom) {
			float ratio = originalDistance / currentDistance;
			camera.zoom = initialScale * ratio;
			System.out.println(camera.zoom);
		} else if (camera.zoom >= layerZoom * 2) {
			camera.zoom -= layerZoom * 0.5f;
		}
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	public void update() {
		if (flinging) {
			velX *= 0.98f;
			velY *= 0.98f;
			camera.position.add(-velX * Gdx.graphics.getDeltaTime(), velY
					* Gdx.graphics.getDeltaTime(), 0);
			if (Math.abs(velX) < 0.01f)
				velX = 0;
			if (Math.abs(velY) < 0.01f)
				velY = 0;
		}
		setNewCameraStatePosition();

	}

	/**
	 * Methode setzt die Kamera auf gewünschte position je nach Status. 
	 * 0=Intro, 1=FullView 2=FullView smooth, 3=GameFieldZoom, 4=PlayerShips,
	 * 5=EnemyShips
	 * 
	 * @param layerY
	 * @param layerX
	 * @param layerZoom
	 */
	private void setNewCameraStatePosition() {
		if(!state.get(0)){
//			game.
		}
		
		
		
		// Intro
		if (state.get(0) && (camera.position.x < layerX)) {
			camera.position.y = layerY;
			camera.zoom = layerZoom;
			camera.position.x += 5;
			// Gdx.app.log("Schiffeversenken 1.0: ","Intro Ausführen");
		} else if (state.get(0)) {
			camera.position.x = layerX;
			// State Wechsel
			changeStateTo(state, 1, false);
			Gdx.app.log("Schiffeversenken 1.0: ",
					"Wechsel zu FullView Ausführen");
		}
		// 1=FullView
		if (state.get(1)) {
			if (camera.position.x > layerX) {
				camera.position.x -= 20f;
				if (camera.position.x < layerX)
					camera.position.x = layerX;
			}

			if (camera.position.x < layerX) {
				camera.position.x += 20f;
				if (camera.position.x > layerX)
					camera.position.x = layerX;
			}

			if (camera.position.y > layerY) {
				camera.position.y -= 20f;
				if (camera.position.y < layerY)
					camera.position.y = layerY;
			}

			if (camera.position.y < layerY) {
				camera.position.y += 20f;
				if (camera.position.y > layerY)
					camera.position.y = layerY;
			}

			if (camera.zoom > layerZoom) {
				camera.zoom -= 0.03f;
				if (camera.zoom < layerZoom)
					camera.zoom = layerZoom;
			}

			if (camera.zoom < layerZoom) {
				camera.zoom += 0.03f;
				if (camera.zoom > layerZoom)
					camera.zoom = layerZoom;
			}

		}

		// 2=GameField Zoom
		if (state.get(2)) {
			faktorX = 0.8f;
			faktorY = 1.0f;
			faktorZoom = 0.72f;
			if (camera.position.x > layerX * faktorX) {
				camera.position.x -= 20f;
				if (camera.position.x < layerX * faktorX)
					camera.position.x = layerX * faktorX;
			}

			if (camera.position.x < layerX * faktorX) {
				camera.position.x += 20f;
				if (camera.position.x > layerX * faktorX)
					camera.position.x = layerX * faktorX;
			}

			if (camera.position.y > layerY * faktorY) {
				camera.position.y -= 20f;
				if (camera.position.y < layerY * faktorY)
					camera.position.y = layerY * faktorY;
			}

			if (camera.position.y < layerY * faktorY) {
				camera.position.y += 20f;
				if (camera.position.y > layerY * faktorY)
					camera.position.y = layerY * faktorY;
			}

			if (camera.zoom > layerZoom * faktorZoom) {
				camera.zoom -= 0.03f;
				if (camera.zoom < layerZoom * faktorZoom)
					camera.zoom = layerZoom * faktorZoom;
			}

			if (camera.zoom < layerZoom * faktorZoom) {
				camera.zoom += 0.03f;
				if (camera.zoom > layerZoom * faktorZoom)
					camera.zoom = layerZoom * faktorZoom;
			}

		}

		//PlayerGrid
		if (state.get(3)) {
			faktorX = 0.75f;
			faktorY = 0.7f;
			faktorZoom = 0.65f;
			if (camera.position.x > layerX * faktorX) {
				camera.position.x -= 20f;
				if (camera.position.x < layerX * faktorX)
					camera.position.x = layerX * faktorX;
			}

			if (camera.position.x < layerX * faktorX) {
				camera.position.x += 20f;
				if (camera.position.x > layerX * faktorX)
					camera.position.x = layerX * faktorX;
			}

			if (camera.position.y > layerY * faktorY) {
				camera.position.y -= 20f;
				if (camera.position.y < layerY * faktorY)
					camera.position.y = layerY * faktorY;
			}

			if (camera.position.y < layerY * faktorY) {
				camera.position.y += 20f;
				if (camera.position.y > layerY * faktorY)
					camera.position.y = layerY * faktorY;
			}

			if (camera.zoom > layerZoom * faktorZoom) {
				camera.zoom -= 0.03f;
				if (camera.zoom < layerZoom * faktorZoom)
					camera.zoom = layerZoom * faktorZoom;
			}

			if (camera.zoom < layerZoom * faktorZoom) {
				camera.zoom += 0.03f;
				if (camera.zoom > layerZoom * faktorZoom)
					camera.zoom = layerZoom * faktorZoom;
			}
		}
		
		//EnemyGrid
		if (state.get(4)) {
			faktorX = 0.75f;
			faktorY = 1.3f;
			faktorZoom = 0.65f;
			if (camera.position.x > layerX * faktorX) {
				camera.position.x -= 20f;
				if (camera.position.x < layerX * faktorX)
					camera.position.x = layerX * faktorX;
			}

			if (camera.position.x < layerX * faktorX) {
				camera.position.x += 20f;
				if (camera.position.x > layerX * faktorX)
					camera.position.x = layerX * faktorX;
			}

			if (camera.position.y > layerY * faktorY) {
				camera.position.y -= 20f;
				if (camera.position.y < layerY * faktorY)
					camera.position.y = layerY * faktorY;
			}

			if (camera.position.y < layerY * faktorY) {
				camera.position.y += 20f;
				if (camera.position.y > layerY * faktorY)
					camera.position.y = layerY * faktorY;
			}

			if (camera.zoom > layerZoom * faktorZoom) {
				camera.zoom -= 0.03f;
				if (camera.zoom < layerZoom * faktorZoom)
					camera.zoom = layerZoom * faktorZoom;
			}

			if (camera.zoom < layerZoom * faktorZoom) {
				camera.zoom += 0.03f;
				if (camera.zoom > layerZoom * faktorZoom)
					camera.zoom = layerZoom * faktorZoom;
			}
		}
	}

	/**
	 * Hilfsmethode, dient dem setzen eines Kamerazustandes.
	 * 
	 * 0=Intro, 1=FullView, 2=GameFieldZoom, 3=PlayerShips, 4=EnemyShips,
	 * 5=GameFieldGrid 6=PlayerGrid, 7=EnemyGrid
	 * 
	 * @param state ArrayList<Boolean> mit den jeweiligen Zuständen
	 * @param toStateNumber Der ausgewählte Zustand
	 * @param grid Boolean der im jeweiligen Zustand den Grid Zustand mit aktiviert.
	 */
	public static void changeStateTo(ArrayList<Boolean> state,
			int toStateNumber, boolean grid) {
		for (int i = 0; i < state.size(); i++) {
			if (i == toStateNumber) {
				state.set(i, true);
				if (grid) {
					if (toStateNumber == 2) {
						state.set(5, true);
						state.set(6, false);
						state.set(7, false);
					}
					if (toStateNumber == 3) {
						state.set(5, false);
						state.set(6, true);
						state.set(7, false);
					}
					if (toStateNumber == 4) {
						state.set(5, false);
						state.set(6, false);
						state.set(7, true);
					}
				}
			} else {
				if(grid&&(i==5||i==6||i==7))
					continue;
				state.set(i, false);
			}
		}

	}

}