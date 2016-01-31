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
	
	public Warehouse (String wn, String ad, String ct, String st, String zip, int sc){
		this ();
		if(!validWarehouseName(wn))
			throw new IllegalArgumentException("Invalid warehouse name!");
		if(!validAddress(ad))
			throw new IllegalArgumentException("Invalid address!");
		if(!validCity(ct))
			throw new IllegalArgumentException("Invalid city!");
		if(!validState(st))
			throw new IllegalArgumentException("Invalid State!");
		if(!validZip(zip))
			throw new IllegalArgumentException("Invalid Zip!");
		if(!validStorageCapacity(sc))
			throw new IllegalArgumentException("Invalid Storage Capacity!");
		warehouseName = wn;
		address = ad;
		city = ct;
		state = st;
		this.zip = zip;
		storageCapacity = sc;
			
	}
	
	public long getId(){
		return id;
	}
	
	public String getWarehouseName(){
		return warehouseName;
	}
	
	public boolean validWarehouseName(String wn) {
		if (wn == null || wn.isEmpty())
			return false;
		if (wn.length() > 255)
			return false;
		return true;
	}

	public void setWarehouseName(String warehouseName){
		if(!validWarehouseName(warehouseName))
			throw new IllegalArgumentException("Invalid warehouse name");
		this.warehouseName = warehouseName;
		//lets the observer know of the change
		setChanged();
	}
	
	public String getAddress(){
		return address;
	}
	public boolean validAddress(String ad) {
		if (ad == null || ad.isEmpty())
			return false;
		if (ad.length()> 255)
			return false;
		return true;
	}
	
	public void setAddress(String address){
		if(!validAddress(address))
			throw new IllegalArgumentException("Invalid address!");
		this.address = address;
		//observer notification
		setChanged();
		
	}
	
	public String getCity() {
		return city;
	}
	
	public boolean validCity(String ct) {
		if ( ct == null || ct == "")
			return false;
		if (ct.length()> 100)
			return false;
		return true;
	}
	
	public void setCity(String city){
		if(!validCity(city))
			throw new IllegalArgumentException("Invalid city!");
		this.city = city;
		//observer notification
		setChanged();
	}
	
	public String getState(){
		return state;
	}
	
	public boolean validState(String st) {
		if ( st == null || st == "")
			return false;
		if (st.length()> 50)
			return false;
		return true;
	}
	
	public void setState(String state){
		if(!validState(state))
			throw new IllegalArgumentException("Invalid state!");
		this.state = state;
		//observer notification
		setChanged();
	}
	
	public String getZip(){
		return zip;
	}
	
	public boolean validZip(String zip) {
		if (zip.contains("[a-zA-z]+"))
			return false;
		if (zip.length() != 5)
			return false;
		return true;
	}
	
	public void setZip(String zip){
		if(!validZip(zip))
			throw new IllegalArgumentException("Invalid zip!");
		this.zip = zip;
		//observer notification
		setChanged();
	}
	
	public Integer getStorageCapacity(){
		return storageCapacity;
	}
	
	public boolean validStorageCapacity(Integer sc) {
		if (sc < 0)
			return false;
		return true;
	}

	public void setStorageCapacity(Integer storageCapacity){
		if(!validStorageCapacity(storageCapacity))
			throw new IllegalArgumentException("Invalid storage capacity!");
		this.storageCapacity = storageCapacity;
		//observer notification
		setChanged();
	}
	

	public void finishUpdate() {
		notifyObservers();
	}
	
	
	
	
	@Override
	public String toString(){
		String fullName = getWarehouseName();
		
		fullName += " Address: " + address;
		fullName += " City: " + city;
		fullName += " State: " + state;
		fullName += " Zip: " + zip;
		fullName += " Storage Capacity: " + storageCapacity;
		
		return fullName;
		
	}

	

	

	
	
	
}
