package com.ma.schiffeversenken.view;

import com.ma.schiffeversenken.view.opengl.MyGLSurfaceView;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;


public class OpenGLES20Activity extends Activity {
	private GLSurfaceView mGLView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity
		mGLView = new MyGLSurfaceView(this);
		setContentView(mGLView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// The following call pauses the rendering thread.
		// If your OpenGL application is memory intensive,
		// you should consider de-allocating objects that
		// consume significant memory here.
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The following call resumes a paused rendering thread.
		// If you de-allocated graphic objects for onPause()
		// this is a good place to re-allocate them.
		mGLView.onResume();
	}
}

// package com.ma.schiffeversenken.view;
//
// import android.app.ActionBar.LayoutParams;
// import android.app.Activity;
// import android.content.Intent;
// import android.opengl.GLSurfaceView;
// import android.os.Bundle;
// import android.view.Menu;
// import android.view.MenuItem;
// import android.view.View;
// import android.view.View.OnClickListener;
// import android.widget.Button;
// import android.widget.LinearLayout;
// import android.widget.TextView;
// import com.ma.schiffeversenken.R;
//
// public class OpenGLES20Activity extends Activity {
// // Basic GL surface View
// private MyGLSurfaceView mGLView;
//
// GLRenderer renderer = new MyGLRenderer();
//
// /*
// * Android applications that use OpenGL ES have activities just like any other
// application that
// * has a user interface. The main difference from other applications is what
// you put in the layout
// * for your activity. While in many applications you might use TextView,
// Button and ListView, in an
// * app that uses OpenGL ES, you can also add a GLSurfaceView.
// * @see android.app.Activity#onCreate(android.os.Bundle)
// */
// @Override
// protected void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
//
// // Create a GLSurfaceView instance and set it
// // as the ContentView for this Activity.
// mGLView = new MyGLSurfaceView(this);
// setContentView(mGLView);
//
//
//
// // Button resetButton = new Button(this);
// // TextView tv = new TextView(this);
// // LinearLayout ll = new LinearLayout(this);
// // resetButton.setText("Reset Scene");
// // tv.setText("Reset: ");
// // ll.addView(tv);
// // ll.addView(resetButton);
// // LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
// // LayoutParams.MATCH_PARENT);
// // // linearlayout and l
// // addContentView(ll, lp);
// // resetButton.setOnClickListener(new OnClickListener() {
// // public void onClick(View v) {
// // // what happened when i klick the Reset Button
// // }
// // });
// }
//
// private void createButtons(Button button, int id, String text, final Class c)
// {
// /*
// * Buttons erstellen
// */
// Button startSpielButton = (Button) findViewById(id);
// startSpielButton.setText(text);
// startSpielButton.setOnClickListener(new View.OnClickListener() {
// @Override
// public void onClick(View v) {
// try {
// Intent intent = new Intent(OpenGLES20Activity.this, c);
// startActivity(intent);
// } catch (Exception ex) {
// ex.printStackTrace();
// }
// }
// });
// }
//
// @Override
// public boolean onCreateOptionsMenu(Menu menu) {
// // Inflate the menu; this adds items to the action bar if it is present.
// getMenuInflater().inflate(R.menu.spielermodus, menu);
// return true;
// }
//
// @Override
// public boolean onOptionsItemSelected(MenuItem item) {
// // Handle action bar item clicks here. The action bar will
// // automatically handle clicks on the Home/Up button, so long
// // as you specify a parent activity in AndroidManifest.xml.
// int id = item.getItemId();
// if (id == R.id.action_settings) {
// return true;
// }
// return super.onOptionsItemSelected(item);
// }
// }