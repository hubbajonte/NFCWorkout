package com.areva.trainingnfc;
/*
 * Klass som skapar och hanterar allt som har med vår databas att göra
 * Jonathan Arevalo Garay 9205194856
 * 
 * */
import java.util.ArrayList;
import java.util.List;
import com.areva.trainingnfc.Models.Taggie;
import com.areva.trainingnfc.Models.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class MyDatabase extends SQLiteOpenHelper{
	
	//Statiska variabler
	//Databasversion
	private static final int DATABASE_VERSION = 1;
	//Databasnamn
	private static final String DATABASE_NAME = "userSettings";
	//Tabell för användarna
	private static final String TABLE_USERS = "user"; 
	//Tabell för NFC-taggarna
	private static final String TABLE_TAGS ="tag";
	
	//Kolumnnamn för användarna
	private static final String USER_ID = "id";
	private static final String USER_NAME = "name";
	private static final String USER_EMAIL = "email";
	private static final String USER_WEIGHT = "weight";
	private static final String USER_PASSWORD = "password";
	private static final String USER_LENGTH = "length";
	private static final String USER_HOMEGYM = "homeGym";
	
	//Kolumnnamn för taggarna
	private static final String TAG_ID ="id";
	private static final String TAG_INFO= "tagInfo";
	
	//Konstruktor, skapar databasen
	public MyDatabase (Context context) {
		super(context, DATABASE_NAME, null,DATABASE_VERSION);
	}
	
	//Här skapar vi våran databas med tillhörande tabeller
	public void onCreate (SQLiteDatabase db) {
		String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" + USER_ID + " INTEGER PRIMARY KEY," 
				+ USER_NAME + " TEXT,"
				+ USER_EMAIL + " TEXT,"
				+ USER_WEIGHT + " INTEGER,"
				+ USER_LENGTH + " INTEGER,"
				+ USER_PASSWORD + " TEXT,"
				+ USER_HOMEGYM + " TEXT" + ")";
		//Skapandet av användartabellen
		db.execSQL(CREATE_USERS_TABLE);
		String CREATE_TAGS_TABLE = "CREATE TABLE " + TABLE_TAGS + "(" + TAG_ID + " INTEGER PRIMARY KEY,"
				+ TAG_INFO + " TEXT" + ")";
		//Skapandet av taggtabellen
		db.execSQL(CREATE_TAGS_TABLE);
	}
	
	//För att uppgradera databasen
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		//Droppa de gamla tabellerna om de finns
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
		//Skapa tabellerna igen
		onCreate(db);
	}
	
	//Skapa en ny användare
	public void addUser (User user) {
		//Gör databasen skrivbar
		SQLiteDatabase db = this.getWritableDatabase();
		//ContentValues används för att lagra flera värden i samma behållare
		ContentValues values = new ContentValues();
		//Lagrar uppgifter
		values.put(USER_NAME, user.get_name());
		values.put(USER_EMAIL, user.get_email());
		values.put(USER_WEIGHT, user.get_weight());
		values.put(USER_LENGTH, user.get_length());
		values.put(USER_PASSWORD, user.get_password());
		values.put(USER_HOMEGYM, user.get_homeGym());
		//Skapa raden
		db.insert(TABLE_USERS, null, values);
		db.close();//Stänger åtkomsten
	}
	
	//Som ovan, fast för taggarna
	public void addTag (Taggie tag) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TAG_INFO, tag.get_info());
		db.insert(TABLE_TAGS, null, values);
		db.close();
	}
	
	//Hämta en enskild tagg
	public Taggie getTag (int id) {
		//Gör databasen läsbar
		SQLiteDatabase db = this.getReadableDatabase();
		//Cursor läser av resultaten av en förfrågan, den pekar mot önskad rad i tabellen
		//Här hämtar vi id och info
		Cursor cursor = db.query(TABLE_TAGS, new String [] {TAG_ID, TAG_INFO}, TAG_ID + "=?",
				new String [] {(String.valueOf(id))}, null, null, null, null);
		//Om det finns en rad, gå till första
		if (cursor != null) {
			cursor.moveToFirst();
		}
		//Hämta objektet utav den hämtade taggen (angivna id:t)
		Taggie tag = new Taggie (Integer.parseInt(cursor.getString(0)), cursor.getString(1));
		//Returnera taggen
		return tag;
	}
	
	//Som ovan fast nu hämtar vi ALLA taggar istället
	public List<Taggie> getAllTags (){
		//Skapar en lista utav taggar
		List<Taggie> tagList = new ArrayList<Taggie>();
		//Select-sträng för att hämta alla inlägg
		String selectQuery = "SELECT * FROM " + TABLE_TAGS;
		//Skrivbar databas
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor som vi pekar mot selectfrågan
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Hämta första, fortsätt sedan till de resterande inläggen, avsluta när det inte finns fler inlägg
        if (cursor.moveToFirst()) {
        	do {
        		Taggie tag = new Taggie();
        		tag.set_id(Integer.parseInt(cursor.getString(0)));
        		tag.set_info(cursor.getString(1));
        		tagList.add(tag);
        	} while (cursor.moveToNext());
        }
        //Returnera listan
        return tagList;
	}
	
	//Uppdatera en tag 
	//TODO: Ta bort? Kommer förmodligen inte användas
	public int updateTag (Taggie tag) {
		//Skrivbar databas
		SQLiteDatabase db = this.getWritableDatabase();
		//Värden i ContentValues
		ContentValues values = new ContentValues();
		values.put(TAG_INFO, tag.get_info());
		//Returnera en uppdaterad rad
		return db.update(TABLE_TAGS, values, TAG_ID + " = ?", new String [] {String.valueOf(tag.get_id())});
	}
	
	//Ta bort en tag
	public void deleteTag (Taggie tag) {
		//Skrivbar
	    SQLiteDatabase db = this.getWritableDatabase();
	    //Ta bort önskad tag
	    db.delete(TABLE_TAGS, TAG_ID + " = ?",
	            new String[] { String.valueOf(tag.get_id()) });
	    db.close();
	}
	
	//Hämta en enskild användare, som taggens version
	public User getUser (int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_USERS, new String[] {USER_ID,USER_NAME, USER_EMAIL,USER_WEIGHT, USER_LENGTH, USER_PASSWORD, USER_HOMEGYM },
		USER_ID + "=?",
		new String[] {String.valueOf(id)}, null, null, null,null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		User user = new User (Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), cursor.getString(1), 
				cursor.getString(2), cursor.getString(5), cursor.getString(6));
		return user;
	}
	
	//TVÅ MEDOTER FÖR ATT HÄMTA ALLA ANVÄNDARE OCH EN FÖR ATT RÄKNA DOM
	
	//Uppdatera en användare, som taggens version
	public int updateUser (User user) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(USER_NAME, user.get_name());
		values.put(USER_EMAIL, user.get_email());
		values.put(USER_PASSWORD, user.get_password());
		values.put(USER_WEIGHT, user.get_weight());
		values.put(USER_LENGTH, user.get_length());
		values.put(USER_HOMEGYM, user.get_homeGym());
		
		return db.update(TABLE_USERS,values, USER_ID + " = ?", new String [] {String.valueOf(user.get_id())});
	}
	//Alternativ metod för att uppdatera en användare om vi inte vill/kan jobba med objekt
	public void updateUserSecond (String name, String email, int weight, int length, String homeGym) {
		//Skrivbar version
		SQLiteDatabase db = this.getWritableDatabase();
		//ContentValues, flera värden
		ContentValues values = new ContentValues();
		//De värden vi vill ändra, notera att id eller lösenord inte ändras
		values.put(USER_NAME, name);
		values.put(USER_EMAIL, email);
		values.put(USER_WEIGHT, weight);
		values.put(USER_LENGTH, length);
		values.put(USER_HOMEGYM, homeGym);
		//Uppdatera databasen
		db.update(TABLE_USERS, values, USER_EMAIL + "=?", new String [] {email});
	}
	
	//Ta bort en användare, som taggen
	public void deleteUser (User user) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_USERS, USER_ID + " = ?",
	            new String[] { String.valueOf(user.get_id()) });
	    db.close();
	}
	
	/*
	 * Nedan följer ett par get-metoder för att hämta enstaka rader om användarna
	 * Vi använder oss utav emailen som en unik identifierare
	 * */
	
	//Hämtar namnet
	public String getName (String email){
		//Skrivbar databas
		SQLiteDatabase db = this.getReadableDatabase();
		//Pekar vår cursor mot emailen
		Cursor cursor = db.query(TABLE_USERS, null, USER_EMAIL + "=?", new String [] {email}, null, null, null);
		//Om emailen inte finns i databasen
		if(cursor.getCount() <1){
			cursor.close();
			return "Existerar inte";
			
		}
		//Om emailen finns, peka cursor mot första sanna inlägget
		else {
			cursor.moveToFirst();
			//Hämta namnet
			String id = cursor.getString(cursor.getColumnIndex(USER_NAME));
			cursor.close();
			return id;
		}

	}
	//Samma som ovan fast med vikten
	public String getWeight (String email){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_USERS, null, USER_EMAIL + "=?", new String [] {email}, null, null, null);
		if(cursor.getCount() <1){
			cursor.close();
			return "Existerar inte";
			
		}
		else {
			cursor.moveToFirst();
			String id = cursor.getString(cursor.getColumnIndex(USER_WEIGHT));
			cursor.close();
			return id;
		}

	}
	//Samma som ovan fast med längden
	public String getLength (String email){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_USERS, null, USER_EMAIL + "=?", new String [] {email}, null, null, null);
		if(cursor.getCount() <1){
			cursor.close();
			return "Existerar inte";
			
		}
		else {
			cursor.moveToFirst();
			String id = cursor.getString(cursor.getColumnIndex(USER_LENGTH));
			cursor.close();
			return id;
		}

	}
	//Samma som ovan fast med emailen
	public String getEmail (String email){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_USERS, null, USER_EMAIL + "=?", new String [] {email}, null, null, null);
		if(cursor.getCount() <1){
			cursor.close();
			return "Existerar inte";
			
		}
		else {
			cursor.moveToFirst();
			String id = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
			cursor.close();
			return id;
		}

	}
	//Samma som ovan fast med gymmet
	public String getGym (String email){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_USERS, null, USER_EMAIL + "=?", new String [] {email}, null, null, null);
		if(cursor.getCount() <1){
			cursor.close();
			return "Existerar inte";
			
		}
		else {
			cursor.moveToFirst();
			String id = cursor.getString(cursor.getColumnIndex(USER_HOMEGYM));
			cursor.close();
			return id;
		}

	}
	/*
	 * Metod som hanterar vår inloggning
	 * Denna är lik ovanstående get metoder
	 * OBS: Vi lagrar för tillfället inte lösenorden i krypterade varianter
	 * */
	public String loginUser (String email) {
		//Skrivbar
		SQLiteDatabase db = this.getReadableDatabase();
		//Peka cursor mot emailen
		Cursor cursor = db.query(TABLE_USERS, null, USER_EMAIL + " =?", new String [] {email}, null, null, null);
		//Om det inte finns en email
		if(cursor.getCount()<1){
			cursor.close();
			return "Existerar inte";
		}
		//Om det finns en email, hämta första sanna lösenord
			else {
				cursor.moveToFirst();
				//Lagra lösenordet
				String password = cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
				cursor.close();
				//Returnera
				return password;
			}
		}
	}
