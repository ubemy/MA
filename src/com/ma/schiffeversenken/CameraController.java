package com.ma.schiffeversenken;

import java.util.ArrayList;

import android.media.CameraProfile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.ma.schiffeversenken.android.controller.Game;
import com.ma.schiffeversenken.android.model.Player;

class CameraController implements GestureListener {
	float velX, velY;
	boolean flinging = false;
	float initialScale = 1;
	OrthographicCamera camera;
	float layerX, layerY, layerZoom;
	Game game;

	public CameraController(OrthographicCamera c, float mx, float my,
			float mzoom, Player player) {
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
		game.touchEvent(x, y);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		Gdx.app.log("GestureDetectorTest", "tap at " + x + ", " + y
				+ ", count: " + count);
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
		camera.position.add(-deltaX * camera.zoom, deltaY * camera.zoom, 0);
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
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

	public void update(ArrayList<Boolean> state) {
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
		setNewCameraStatePosition(state);

	}

	/**
	 * Methode setzt die Kamera auf gewünschte position je nach Status. 0=Intro,
	 * 1=FullView 2=FullView smooth, 3=GameFieldZoom, 4=PlayerShips,
	 * 5=EnemyShips
	 * 
	 * @param layerY
	 * @param layerX
	 * @param layerZoom 
	 */
	private void setNewCameraStatePosition(ArrayList<Boolean>  state) {
		// Intro
		if (state.get(0) && (camera.position.x < layerX)) {
			camera.position.y=layerY;
			camera.zoom=layerZoom;
			camera.position.x += 5;
			System.out.println("AA");
		} else if (state.get(0)) {
			camera.position.x = layerX;
			state.set(0,new Boolean(false));
			state.set(1,new Boolean(true));
			state.set(5,new Boolean(true));
			System.out.println("BB");
		}
		// 1=FullView
		if (state.get(1)) {
			System.out.println("CC");
			if (camera.position.x > layerX) {
				camera.position.x -= 10f;
				if (camera.position.x < layerX)
					camera.position.x = layerX;
			}

			if (camera.position.x < layerX) {
				camera.position.x += 10f;
				if (camera.position.x > layerX)
					camera.position.x = layerX;
			}

			if (camera.position.y > layerY) {
				camera.position.y -= 10f;
				if (camera.position.y < layerY)
					camera.position.y = layerY;
			}

			if (camera.position.y < layerY) {
				camera.position.y += 10f;
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

		// 1=FullView smooth
		if (state.get(2)) {
			System.out.println("DD");
			if (camera.position.x > layerX) {
				camera.position.x -= 200f;
			}
			if (camera.position.x < layerX) {
				camera.position.x += 200f;
			}
			if (camera.position.y > layerY) {
				camera.position.y -= 200f;
			}
			if (camera.position.y < layerY) {
				camera.position.y += 200f;
			}
			if (camera.zoom > layerZoom) {
				camera.zoom -= 0.2f;
			}
			if (camera.zoom < layerZoom) {
				camera.zoom += 0.2f;
			}
		}

		if (state.get(3)) {
			if (camera.position.x > layerX * 0.80f) {
				camera.position.x -= 2f;
			}
			if (camera.position.x < layerX * 0.80f) {
				camera.position.x += 2f;
			}
			if (camera.position.y > layerY) {
				camera.position.y -= 2f;
			}
			if (camera.position.y < layerY) {
				camera.position.y += 2f;
			}
			if (camera.zoom > layerZoom * 0.7f) {
				camera.zoom -= 0.01f;
			}
			if (camera.zoom < layerZoom * 0.7f) {
				camera.zoom += 0.01f;
			}
		}

	}

}