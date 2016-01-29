package models;

import java.util.Observable;

public class Warehouse extends Observable{

	//initialized at one
	private static long nextId = 1;
	
	// id for the ware houses
	private long id;
	
	private String warehouseName;
	
	private String address;
	
	private String city;
	
	private String state;
	
	private String zip;
	
	// max number of units that can be stored at the warehouse
	private int storageCapacity;
	
	public Warehouse(){
		id = nextId++;
		warehouseName = "";
		address = "";
		city = "";
		state = "";
		zip = "";
		storageCapacity = 0;
	}
	
	public Warehouse (String wn, String ad, String ct, String st, String zp, int sc){
		this ();
		if(!validWarehouseName(wn))
			throw new IllegalArgumentException("Invalid warehouse name!");
		if(!validAddress(ad))
			throw new IllegalArgumentException("Invalid address!");
		if(!validCity(ct))
			throw new IllegalArgumentException("Invalid city!");
		if(!validState(st))
			throw new IllegalArgumentException("Invalid State!");
		
		
	}

	public boolean validState(String st) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean validCity(String ct) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean validAddress(String ad) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean validWarehouseName(String wn) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
