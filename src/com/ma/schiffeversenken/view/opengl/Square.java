package com.ma.schiffeversenken.view.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Square {
	private final String vertexShaderCode =
	// This matrix member variable provides a hook to manipulate
	// the coordinates of the objects that use this vertex shader
	"uniform mat4 uMVPMatrix;" + "attribute vec4 vPosition;" + "void main() {" +
	// The matrix must be included as a modifier of gl_Position.
	// Note that the uMVPMatrix factor *must be first* in order
	// for the matrix multiplication product to be correct.
			" gl_Position = uMVPMatrix * vPosition;" + "}";
	private final String fragmentShaderCode = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main() {"
			+ " gl_FragColor = vColor;" + "}";
	private final FloatBuffer vertexBuffer;
	private final ShortBuffer drawListBuffer;
	private final int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;
	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	static float squareCoords[] = { -0.5f, 0.5f, 0.0f, // top left
			-0.5f, -0.5f, 0.0f, // bottom left
			0.5f, -0.5f, 0.0f, // bottom right
			0.5f, 0.5f, 0.0f }; // top right
	private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw
															// vertices
	private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per
															// vertex
	float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

	/**
	 * Sets up the drawing object data for use in an OpenGL ES context.
	 */
	public Square() {
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
		// (# of coordinate values * 4 bytes per float)
				squareCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(squareCoords);
		vertexBuffer.position(0);
		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(
		// (# of coordinate values * 2 bytes per short)
				drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);
		// prepare shaders and OpenGL program
		int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);
		int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);
		mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader
														// to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
															// shader to program
		GLES20.glLinkProgram(mProgram); // create OpenGL program executables
	}

	/**
	 * Encapsulates the OpenGL ES instructions for drawing this shape.
	 *
	 * @param mvpMatrix
	 *            - The Model View Project matrix in which to draw this shape.
	 */
	public void draw(float[] mvpMatrix) {
		// Add program to OpenGL environment
		GLES20.glUseProgram(mProgram);
		// get handle to vertex shader's vPosition member
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		// Enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
		// get handle to fragment shader's vColor member
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		// Set color for drawing the triangle
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		// get handle to shape's transformation matrix
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		MyGLRenderer.checkGlError("glGetUniformLocation");
		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		MyGLRenderer.checkGlError("glUniformMatrix4fv");
		// Draw the square
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}

//
// import java.nio.ByteBuffer;
// import java.nio.ByteOrder;
// import java.nio.FloatBuffer;
// import java.nio.ShortBuffer;
//
// import android.opengl.GLES20;
//
// /**
// * Created by xps on 16.11.2014.
// */
// public class Square {
// private FloatBuffer vertexBuffer;
// private ShortBuffer drawListBuffer;
// private int mProgram;
// private int mPositionHandle;
// private int vertexStride;
// private int mColorHandle;
// private float[] color;
// private int vertexCount;
//
// // number of coordinates per vertex in this array
// static final int COORDS_PER_VERTEX = 3;
// static float squareCoords[] = {
// -0.5f, 0.5f, 0.0f, // top left
// -0.5f, -0.5f, 0.0f, // bottom left
// 0.5f, -0.5f, 0.0f, // bottom right
// 0.5f, 0.5f, 0.0f }; // top right
//
// private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
//
// /*
// You need at least one vertex shader to draw a shape and one fragment shader
// to color that shape.
// These shaders must be complied and then added to an OpenGL ES program, which
// is then used to draw the shape.
// */
// private final String vertexShaderCode =
// "attribute vec4 vPosition;" +
// "void main() {" +
// "  gl_Position = vPosition;" +
// "}";
//
// private final String fragmentShaderCode =
// "precision mediump float;" +
// "uniform vec4 vColor;" +
// "void main() {" +
// "  gl_FragColor = vColor;" +
// "}";
//
//
//
//
// /*
// In order to draw your shape, you must compile the shader code, add them to a
// OpenGL ES program
// object and then link the program. Do this in your drawn object’s
// constructor, so it is only done once.
// Compiling OpenGL ES shaders and linking programs is expensive in terms of CPU
// cycles and processing time,
// so you should avoid doing this more than once. If you do not know the content
// of your shaders at runtime,
// you should build your code such that they only get created once and then
// cached for later use.
// */
// public Square() {
// // initialize vertex byte buffer for shape coordinates
// ByteBuffer bb = ByteBuffer.allocateDirect(
// // (# of coordinate values * 4 bytes per float)
// squareCoords.length * 4);
// bb.order(ByteOrder.nativeOrder());
// vertexBuffer = bb.asFloatBuffer();
// vertexBuffer.put(squareCoords);
// vertexBuffer.position(0);
//
// // initialize byte buffer for the draw list
// ByteBuffer dlb = ByteBuffer.allocateDirect(
// // (# of coordinate values * 2 bytes per short)
// drawOrder.length * 2);
// dlb.order(ByteOrder.nativeOrder());
// drawListBuffer = dlb.asShortBuffer();
// drawListBuffer.put(drawOrder);
// drawListBuffer.position(0);
//
//
// int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
// int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
// fragmentShaderCode);
//
// mProgram = GLES20.glCreateProgram(); // create empty OpenGL ES Program
// GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader to
// program
// GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader
// to program
// GLES20.glLinkProgram(mProgram); // creates OpenGL ES program executables
//
//
// }
//
// /*
// At this point, you are ready to add the actual calls that draw your shape.
// Drawing shapes with
// OpenGL ES requires that you specify several parameters to tell the rendering
// pipeline what you
// want to draw and how to draw it. Since drawing options can vary by shape,
// it's a good idea to
// have your shape classes contain their own drawing logic.
//
// Create a draw() method for drawing the shape. This code sets the position and
// color values to
// the shape’s vertex shader and fragment shader, and then executes the
// drawing function.
// */
// public void draw() {
// // Add program to OpenGL ES environment
// GLES20.glUseProgram(mProgram);
//
// // get handle to vertex shader's vPosition member
// mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
//
// // Enable a handle to the triangle vertices
// GLES20.glEnableVertexAttribArray(mPositionHandle);
//
// // Prepare the triangle coordinate data
// GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
// GLES20.GL_FLOAT, false,
// vertexStride, vertexBuffer);
//
// // get handle to fragment shader's vColor member
// mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
//
// // Set color for drawing the triangle
// GLES20.glUniform4fv(mColorHandle, 1, color, 0);
//
// // Draw the triangle
// GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
//
// // Disable vertex array
// GLES20.glDisableVertexAttribArray(mPositionHandle);
// }
//
//
// public static int loadShader(int type, String shaderCode){
//
// // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
// // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
// int shader = GLES20.glCreateShader(type);
//
// // add the source code to the shader and compile it
// GLES20.glShaderSource(shader, shaderCode);
// GLES20.glCompileShader(shader);
//
// return shader;
// }
// }