package com.areva.trainingnfc;
/*
 * Klass som hanterar inloggningen, hämtar data från databasen
 * Jonathan Arevalo Garay 9205194856
 * 
 * */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	//Attribut, databas
	MyDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//Skapar en ny anslutning/instans utav vår databas
		db = new MyDatabase(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_about) {
			Intent aboutActivity = new Intent (this, About.class);
			startActivity(aboutActivity);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//Hantera login knappen
	//OBS: Lösenordet krypteras ej i denna version: 1.0
	public void btnLogin (View view) {
		//Hämtar våra vyer och läser av dessa
		EditText username = (EditText)findViewById(R.id.editUsername);
		EditText password = (EditText)findViewById(R.id.editPassword);
		//Hämtar avlästa värden och sparar dessa
		String strUsername = username.getText().toString();
		String strPassword = password.getText().toString();
		//Skickar med emailen för att hämta det lagrade lösenordet
		String storedPassword = db.loginUser(strUsername);
		//Skapar en ny intent där vi skickar med emailen
		Intent profileActivity = new Intent (this, Profile.class);
		profileActivity.putExtra("Email", strUsername);
		//Om lösenordet stämmer
		if (strPassword.equals(storedPassword)){
			//Starta aktiviteten
			startActivity(profileActivity);

		}
		//Annars, skriv ut att det är felaktigt inmatade uppgifter
		//TODO: Lägg till en räknare som räknar antalet försök
		else {
			Toast.makeText(this, "Du har tyvärr skrivit in fel uppgifter. Försök igen.", Toast.LENGTH_LONG).show();
		}
	}

}
