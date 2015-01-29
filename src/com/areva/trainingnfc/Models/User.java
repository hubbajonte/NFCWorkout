package com.areva.trainingnfc.Models;

/*
 * Modellklass för användarna
 * Jonathan Arevalo Garay 9205194856
 * 
 * */
public class User {
	
	//Attribut
	int _id;
	int _weight;
	int _length;
	String _name;
	String _password;
	String _email;
	String _homeGym;
	
	public User(){
		
	}

	public User(int _id, int _weight, int _length, String _name,
			String _password, String _email, String _homeGym) {
		this._id = _id;
		this._weight = _weight;
		this._length = _length;
		this._name = _name;
		this._password = _password;
		this._email = _email;
		this._homeGym = _homeGym;
	}

	public User(int _weight, int _length, String _name,
			String _password, String _email, String _homeGym) {
		this._weight = _weight;
		this._length = _length;
		this._name = _name;
		this._password = _password;
		this._email = _email;
		this._homeGym = _homeGym;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int get_weight() {
		return _weight;
	}

	public void set_weight(int _weight) {
		this._weight = _weight;
	}

	public int get_length() {
		return _length;
	}

	public void set_length(int _length) {
		this._length = _length;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_password() {
		return _password;
	}

	public void set_password(String _password) {
		this._password = _password;
	}

	public String get_email() {
		return _email;
	}

	public void set_email(String _email) {
		this._email = _email;
	}

	public String get_homeGym() {
		return _homeGym;
	}

	public void set_homeGym(String _homeGym) {
		this._homeGym = _homeGym;
	}
	


	
}
