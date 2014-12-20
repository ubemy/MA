package com.ma.schiffeversenken;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

class CameraController implements GestureListener {
		float velX, velY;
		boolean flinging = false;
		float initialScale = 1;
		OrthographicCamera camera;
		float CameraMaxX, CameraMaxY, CameraMaxZoom;
		
		public CameraController(OrthographicCamera c,float mx,float my,float mzoom) {
			this.camera=c;
			this.CameraMaxX=mx;
			this.CameraMaxY=my;
			this.CameraMaxZoom=mzoom;
		}
		
		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			flinging = false;
			initialScale = camera.zoom;
			return false;
		}
		
		@Override
		public boolean tap(float x, float y, int count, int button) {
			Gdx.app.log("GestureDetectorTest", "tap at " + x + ", " + y + ", count: " + count);
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
		public boolean zoom (float originalDistance, float currentDistance) {
			float ratio = originalDistance / currentDistance;
			camera.zoom = initialScale * ratio;
			System.out.println(camera.zoom);
			return false;
		}
		
		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
				Vector2 pointer1, Vector2 pointer2) {
			// TODO Auto-generated method stub
			return false;
		}
		
		public void update () {
			if(camera.position.x<CameraMaxX && camera.position.y<CameraMaxY&&camera.zoom<CameraMaxZoom)
			if (flinging) {
				velX *= 0.98f;
				velY *= 0.98f;
				camera.position.add(-velX * Gdx.graphics.getDeltaTime(), velY * Gdx.graphics.getDeltaTime(), 0);
				if (Math.abs(velX) < 0.01f) velX = 0;
				if (Math.abs(velY) < 0.01f) velY = 0;
			}
		}

	}