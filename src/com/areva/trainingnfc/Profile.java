package com.areva.trainingnfc;
/*
 * Klass som skapar en inloggning och lagrar denna i vår databas
 * Jonathan Arevalo Garay
 * 
 * */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Profile extends Activity {
	//Attribut, inklusive databas
	TextView name;
	TextView weight;
	TextView length;
	TextView email;
	TextView gym;
	MyDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		//Skapar en ny anslutning/instans utav vår databas
		db = new MyDatabase(this);
		//Hämtar textvyerna och lagrar dessa
		name = (TextView)findViewById(R.id.txtName);
		weight = (TextView)findViewById(R.id.txtWeight);
		length = (TextView)findViewById(R.id.txtLength);
		email = (TextView)findViewById(R.id.txtEmail);
		gym = (TextView)findViewById(R.id.txtGym);
		
		//Hämtar emailen från förregående aktivitet
		Bundle extras = getIntent().getExtras();
		String strUsername = extras.getString("Email");
		//Sätter texten i våra textvyer, texten hämtas från databasen
		//TODO: Här kan man använda en listview istället
		name.setText(db.getName(strUsername));
		weight.setText(db.getWeight(strUsername));
		length.setText(db.getLength(strUsername));
		email.setText(db.getEmail(strUsername));
		gym.setText(db.getGym(strUsername));
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		//Om vi vill redigera uppgifterna
		if (id == R.id.action_editProfile){
			//Hämta emailen
			String sendEmail = email.getText().toString();
			//Skicka med emailen i en intent till klassen
			Intent profileActivity = new Intent (this, EditProfile.class);
			profileActivity.putExtra("Email", sendEmail);
			startActivity(profileActivity);
		}
		//Om vi vill gå till skannern
		if (id == R.id.action_scan){
			//Starta en ny intent
			Intent scanActivity = new Intent (this, Scanning.class);
			startActivity(scanActivity);
		}
		if (id == R.id.action_about){
			//Starta en ny intent
			Intent aboutActivity = new Intent (this, About.class);
			startActivity(aboutActivity);
		}
		return super.onOptionsItemSelected(item);
	}
	
}
