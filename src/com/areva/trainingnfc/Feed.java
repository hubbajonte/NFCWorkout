package com.areva.trainingnfc;
/*
 * Klass som visar upp all information som lagrats om taggarna i vår databas.
 * Jonathan Arevalo Garay 9205194856
 * 
 * */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Feed extends Activity {

	//Attribut, listvy och databas
	private ListView tagList;
	MyDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);
		//Skapar vår anslutning/instans utav databasen
		db = new MyDatabase(this);
		//Arraylista där vi lagrar våra taggar, dessa får vi med hjälp av metoden getAllTags
		ArrayList arrayLTags = (ArrayList) db.getAllTags();
		//ArrayAdapter för vår lista, enskild item och textvy
		ArrayAdapter adapterTags = new ArrayAdapter (this,R.layout.list_item, R.id.info,arrayLTags);
		//Hämtar vår listVy
		tagList = (ListView)findViewById(R.id.tagList);
		//Fyller listan med adapterns information, adaptern agerar som en brygga
		tagList.setAdapter(adapterTags);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed, menu);
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



}
