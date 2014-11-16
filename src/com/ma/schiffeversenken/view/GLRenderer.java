package com.ma.schiffeversenken.view;

//import java.util.Random;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import com.ma.schiffeversenken.view.opengl.Triangle;

public class GLRenderer implements Renderer {

    Triangle triangle;

    @Override //every fps
    public void onDrawFrame(GL10 gl) {
        //Every time rendered we clear BG
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        triangle.draw();
        
    }

    @Override //when surface is changed
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //reset x and y coordinates to the size of screen
//        gl.glViewport(0, 0, width, height);
    }

    @Override //like init() when surface created
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        triangle = new Triangle();

    }

}
