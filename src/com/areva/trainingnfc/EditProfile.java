package com.areva.trainingnfc;
/*
 * Klass som redigerar profilen, dvs uppdaterar vår databas
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

public class EditProfile extends Activity {
	//Attribut, inklusive databas
	MyDatabase db;
	EditText name;
	EditText weight;
	EditText length;
	EditText email;
	EditText homeGym;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		//Skapar en ny anslutning/instans utav vår databas
		db = new MyDatabase(this);
		//Hämtar information från vyerna
		name = (EditText)findViewById(R.id.updateName);
		weight = (EditText)findViewById(R.id.updateWeight);
		length = (EditText)findViewById(R.id.updateLength);
		email = (EditText)findViewById(R.id.updateEmail);
		homeGym = (EditText)findViewById(R.id.updateGym);
		//Hämtar information från föregående skärm 
		Bundle extras = getIntent().getExtras();
		//Sparar informationen i en sträng
		String strUsername = extras.getString("Email");
		//Hämtar information om användaren och uppdaterar vyerna
		name.setText(db.getName(strUsername));
		weight.setText(db.getWeight(strUsername));
		length.setText(db.getLength(strUsername));
		email.setText(db.getEmail(strUsername));
		homeGym.setText(db.getGym(strUsername));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_profile, menu);
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
	
	//Metod för att uppdatera vår profil
	public void updateProfile (View view) {
		//Anropar metoden som ska uppdatera vår användare, skickar med den data som finns skriven i vyerna
		db.updateUserSecond(name.getText().toString()
				, email.getText().toString(),
				Integer.parseInt(weight.getText().toString()), 
				Integer.parseInt(length.getText().toString()), 
				homeGym.getText().toString());
		//Skickar oss vidare till Profilen där vi skickar med data för att uppdatera vår vy
		Intent profileActivity = new Intent (this, Profile.class);
		profileActivity.putExtra("Email", email.getText().toString());
		startActivity(profileActivity);
	}

	

}
