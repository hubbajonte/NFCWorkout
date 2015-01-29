package com.areva.trainingnfc;
/*
 * Klass som skapar en inloggning och lagrar denna i vår databas
 * Jonathan Arevalo Garay
 * 
 * */
import com.areva.trainingnfc.Models.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class CreateLogin extends Activity {
	//Databasattribut
	MyDatabase db;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_login);
		//Skapar en ny anslutning/instans utav vår databas
		db = new MyDatabase(this);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_login, menu);
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
	
	//När knappen trycks
	public void createProfile (View view) {
		//Hämtar alla vyer och sparar informationen
		EditText name = (EditText)findViewById(R.id.editName);
		EditText weight = (EditText)findViewById(R.id.editWeight);
		EditText length = (EditText)findViewById(R.id.editLength);
		EditText email = (EditText)findViewById(R.id.editEmail);
		EditText homeGym = (EditText)findViewById(R.id.editGym);
		EditText password = (EditText)findViewById(R.id.editPassword);
		
		//Anropar metoden för att skapa en ny användare, skickar med de 
		//Sparade uppgifterna
		db.addUser(new User (Integer.parseInt(weight.getText().toString()),
				Integer.parseInt(length.getText().toString()),
				name.getText().toString(),
				password.getText().toString(),
				email.getText().toString(),
				homeGym.getText().toString()));
		//Tar oss vidare till loginskärmen
		Intent loginActivity = new Intent (this, Login.class);
		startActivity(loginActivity);

	}




}
