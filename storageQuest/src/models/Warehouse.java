package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import database.GatewayException;
import database.WarehouseTableGateway;

public class Warehouse extends Observable implements Observer{

	//initialized at one
	//private static long nextId = 1;
	public static final String ERRORMSG_INVALID_ID = "Invalid id!";
	public static final String ERRORMSG_INVALID_WAREHOUSENAME = "Invalid Warehouse Name!";
	public static final String ERRORMSG_INVALID_ADDRESS = "Invalid address!";
	public static final String ERRORMSG_INVALID_CITY = "Invalid city!";
	public static final String ERRORMSG_INVALID_STATE = "Invalid state!";
	public static final String ERRORMSG_INVALID_ZIP = "Invalid zip!";
	public static final String ERRORMSG_INVALID_STORAGECAP = "Invalid storage capacity!";
	
	public static final int INVALID_ID = 0;
	
	// id for the ware houses
	private long id;
	
	private String warehouseName;
	
	private String address;
	
	private String city;
	
	private String state;
	
	private String zip;
	
	// max number of units that can be stored at the warehouse
	private int storageCapacity;
	
	private WarehouseTableGateway gateway;
	
	private List<WarehousePart> myParts;
	
	public Warehouse(){
		//id = nextId++;
		id = INVALID_ID;
		warehouseName = "";
		address = "";
		city = "";
		state = "";
		zip = "";
		storageCapacity = 0;
		myParts = new ArrayList<WarehousePart>();
	}
	
	public Warehouse (String wn, String ad, String ct, String st, String zip){
		this ();
		if(!validWarehouseName(wn))
			throw new IllegalArgumentException(ERRORMSG_INVALID_WAREHOUSENAME);
		if(!validAddress(ad))
			throw new IllegalArgumentException(ERRORMSG_INVALID_ADDRESS);
		if(!validCity(ct))
			throw new IllegalArgumentException(ERRORMSG_INVALID_CITY);
		if(!validState(st))
			throw new IllegalArgumentException(ERRORMSG_INVALID_STATE);
		if(!validZip(zip))
			throw new IllegalArgumentException(ERRORMSG_INVALID_ZIP);
		
		warehouseName = wn;
		address = ad;
		city = ct;
		state = st;
		this.zip = zip;
		
			
	}
	public Warehouse(String wn, String ad, String ct, String st, String zip, int sc){
		this(wn,ad,ct,st,zip);
		this.storageCapacity = sc;
	}
	
	public Warehouse(long id, String wn, String ad, String ct, String st, String zip, int sc){
		this(wn,ad,ct,st,zip,sc);
		if(id<1)
			throw new IllegalArgumentException(ERRORMSG_INVALID_ID);
		setId(id);
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
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
	
	public String getFullName(){
		String ret = warehouseName;
		if(ret.trim().length() == 0)
			ret = "unknown";
		return ret;
	}
	
	public boolean warehouseAlreadyExists(long id, String wn){
		
		try{
			return gateway.warehouseAlreadyExists(id, wn);
		}catch (GatewayException e){
			return true;
		}
	}
	
	

	public void finishUpdate() throws GatewayException{
		Warehouse orig = null;
		//checking if the warehouse is already in the database
		if(this.getId() == 0){
			if(gateway.warehouseAlreadyExists(0, this.getWarehouseName()))
				throw new GatewayException(this.getWarehouseName() + "  is already in the database");
			
		}
		try{
			//if the id is 0, then this is a new warehouse,else it updates
			if(this.getId() == 0){
				this.setId(gateway.insertWarehouse(this));
			}
			else{
				// gets the warehouse in case it fails
				orig = gateway.fetchWarehouse(this.getId());
				
				gateway.saveWarehouse(this);
			}
			notifyObservers();
			
		}catch(GatewayException e){
			//if it fails, then it tries to re fetch the model fields
			if(orig !=null){
				this.setWarehouseName(orig.getWarehouseName());
				this.setAddress(orig.getAddress());
				this.setCity(orig.getCity());
				this.setState(orig.getState());
				this.setZip(orig.getZip());
				this.setStorageCapacity(orig.getStorageCapacity());
			}
			throw new GatewayException("Error trying to save the warehouse object");
			
		}			
		
	}
	
	public void delete() throws GatewayException {
		//if id is 0 then nothing has to be done in the gateway
		if(this.getId() == 0)
			return;
		try{
			gateway.deleteWarehouse(this.getId());
		}catch(GatewayException e){
			throw new GatewayException(e.getMessage());
		}
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

	public WarehouseTableGateway getGateway(){
		return gateway;
	}
	
	public void setGateway(WarehouseTableGateway gateway){
		this.gateway = gateway;
	}
	
	public List<WarehousePart> getMyParts(){
		return myParts;
	}
	
	public void fetchMyParts(WarehouseList wList, PartList pList){
		try{
			
			List<WarehousePart> parts = gateway.fetchWarehouseParts(this, pList);
			
			for(WarehousePart wp : parts){
				if(!partExistsInMyList(wp.getPart())){
					
					wp.addObserver(this);
					
					wp.getPart().addObserver(wp);
					myParts.add(wp);
				}
				
			}
		}catch (GatewayException e){
			e.printStackTrace();
		}
	}
	//this just checks for the part, so it can be used in fetchMyParts()
	public boolean partExistsInMyList(Part p){
		for(WarehousePart wp: myParts){
			if(wp.getPart() == p)
				return true;
		}
		return false;
	}
	
	public void updateMyPart(WarehousePart wp) throws GatewayException {
		gateway.saveWarehousePart(wp);
	}
	
	
	public boolean addPart(Part p){
		
		if(partExistsInMyList(p))
			return false;
		WarehousePart wp = new WarehousePart(this, p);
		wp.addObserver(this);
		
		p.addObserver(wp);
		
		try{
			gateway.insertWarehousePart(wp);
		}catch(GatewayException e){
			e.printStackTrace();
			return false;
		}
		//adds to the list
		myParts.add(wp);
		
		this.setChanged();
		this.notifyObservers();
		return true;
	}
	
	public void deletePart(WarehousePart wp){
		try{
			gateway.deleteWarehousePart(wp);
		}catch (GatewayException e){
			e.printStackTrace();
			return;
		}
		
		wp.deleteObserver(wp);
		wp.getPart().deleteObserver(wp);
		
		myParts.remove(wp);
		
		this.setChanged();
		this.notifyObservers();
	}
	
	@Override
	public void update(Observable o, Object arg){
		
		if(o instanceof WarehousePart){
			this.setChanged();
			this.notifyObservers();
		}
	}

	
}
