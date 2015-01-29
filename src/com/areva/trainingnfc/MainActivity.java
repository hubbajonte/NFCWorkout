package com.areva.trainingnfc;
/*
 * Vår huvudaktivitet, hanterar två aktiviteter, antingen loggar man in eller skapar en ny inloggning
 * Jonathan Arevalo Garay 9205194856
 * 
 * */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Kod för att gömma status och actionbar
	    View decorView = getWindow().getDecorView();
	    // Hide the status bar.
	    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
	    decorView.setSystemUiVisibility(uiOptions);
	    getActionBar().hide();
	}

	//Vår inloggningssida
	public void LoginPage (View view) {
		//Intent till Login
		Intent profileActivity = new Intent (this, Login.class);
		startActivity(profileActivity);
	}
	//Vår registreringssida
	public void CreateLogin (View view) {
		//Intent till CreateLogin
		Intent profileActivity = new Intent (this, CreateLogin.class);
		startActivity(profileActivity);
	}
	


}

