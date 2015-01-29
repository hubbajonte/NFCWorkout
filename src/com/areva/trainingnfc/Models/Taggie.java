package com.areva.trainingnfc.Models;

/*
 * Modellklass för taggar
 * Jonathan Arevalo Garay 9205194856
 * 
 * */
public class Taggie {
	
	//Attribut
	int _id;
	String _info;
	
	public Taggie () {
		
	}

	public Taggie(int _id, String _info) {
		this._id = _id;
		this._info = _info;
	}

	public Taggie(String _info) {
		this._info = _info;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_info() {
		return _info;
	}

	public void set_info(String _info) {
		this._info = _info;
	}

	@Override
	public String toString() {
		return "Aktivitetsinfo: " + _info;
	}
	
	
	
	

}
