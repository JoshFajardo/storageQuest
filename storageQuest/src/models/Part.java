package models;

import java.util.Observable;

import database.PartTableGateway;
import database.GatewayException;

import models.Warehouse;

public class Part extends Observable {

	
	public static final String ERRORMSG_INVALID_ID = "Invalid id!";
	public static final String ERRORMSG_INVALID_PART_NAME= "Invalid part name!";
	public static final String ERRORMSG_INVALID_PART_NUMBER = "Invalid part number!";
	public static final String ERRORMSG_INVALID_VENDOR = "Invalid vendor!";
	public static final String ERRORMSG_INVALID_UNIT_QUANITITY = "Invalid unit of quantity!";
	public static final String ERRORMSG_INVALID_VENDOR_PART_NUMBER = "Invalid vendor part number!";
	public static final String DEFAULT_EMPTY_PART_NAME = "Unknown";
	
	public static final int INVALID_ID = 0;
	
	private long id;
	//alphanumeric max length of 255, can't be blank or null
	private String partName;
	//alphanumeric max length of  20, unique, can't be blank or null
	private String partNumber;
	//alphanumeric max length of 255, can't be blank or null
	private String vendor;
	// can only be "Linear Ft." or "Pieces"
	private String unitQuanitity;
	//alphanumeric  max length of 255, can't be null
	private String vendorPartNum;
	
	private PartTableGateway gateway;
	
	public Part(){
		id = INVALID_ID;
		partName = DEFAULT_EMPTY_PART_NAME;
		partNumber = "0";
		vendor = "unknown";
		unitQuanitity = "Linear Ft.";
		vendorPartNum = "Unknown";
	}
	
	public Part(long id, String pn, String pnum,String v,String uq, String vpn){
		this.id = id;
		this.partName = pn;
		this.partNumber = pnum;
		this.vendor = v;
		this.unitQuanitity = uq;
		this.vendorPartNum = vpn;
	}
	
	public void finishUpdate() throws GatewayException {
		Part orig = null;
		
		try{
			//if the id is zero, then this is a part to insert
			if(this.getId() == 0){
				//sets id to the long returned by insertPart
				this.setId(gateway.insertPart(this));
			}
			else{
				orig = gateway.fetchPart(this.getId(),false);
				
				gateway.savePart(this);
			}
		}catch(GatewayException e){
			
			if(orig != null){
				//put the set methods here
			}
			throw new GatewayException("Error trying to save");
		}
	}

	
	public void delete() throws GatewayException {
		
		if(this.getId() == 0)
			return;
			try{
				gateway.deletePart(this.getId());				
			}catch (GatewayException e){
				throw new GatewayException(e.getMessage());
			}
		}
	
	public void partAssociation() throws GatewayException{
		if(this.getId() == 0)
			return;
		try{
			gateway.check4PartAssociation(id);
		}catch(GatewayException e){
			throw new GatewayException(e.getMessage());
		}
		
	}
	
	
	public boolean validPartName(String pn){
		if(pn == null)
			return false;
		String test = pn.trim();
		if(test.length ()>255)
			return false;
		if(test.length() == 0)
			return false;
		return true;
	}
	public boolean validPartNumber(String pnum){
		if(pnum == null)
			return false;
		String test = pnum.trim();
		if(test.length ()>20)
			return false;
		if(test.length() == 0)
			return false;
		return true;
	}
	public boolean validUnitQuanitity(String uq){
		//if(!uq.equals("Linear Ft.")|| !uq.equals("Pieces"))
			//return false;
		return true;
	}
	
	public boolean validVendor(String v){
		if(v == null)
			return false;
		String test = v.trim();
		if(test.length()>255)
			return false;
		return true;
	}
	
	public boolean validVendorPartNumber(String vpn){
		if(vpn == null)
			return false;
		String test = vpn.trim();
		if(test.length()>255)
			return false;
		return true;
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public String getPartName(){
		return partName;
	}
	
	public void setPartName(String partName){
		this.partName = partName;
		setChanged();
	}
	
	public String getPartNumber(){
		return partNumber;
	}
	
	public void setPartNumber(String partNumber){
		this.partNumber = partNumber;
		
		setChanged();
	}
	
	public String getUnitQuanitity(){
		return unitQuanitity;
	}
	
	public void setUnitQuanitity(String unitQuanitity){
		this.unitQuanitity = unitQuanitity;
		
		setChanged();
	}
	
	public String getVendor(){
		return vendor;
	}
	
	public void setVendor(String vendor){
		this.vendor = vendor;
		
		setChanged();
	}
	
	public String getVendorPartNumber(){
		return vendorPartNum;
	}
	
	public void setVendorPartNumber(String vendorPartNum){
		this.vendorPartNum = vendorPartNum;
		
		setChanged();
	}
	
	public PartTableGateway getGateway(){
		return gateway;
	}
	
	public void setGateway(PartTableGateway gateway){
		this.gateway = gateway;
	}
	
	public boolean lock()throws GatewayException{

		//if insert, check if this warehouse's full name already exists in the database
		//if so then cancel update 

		if(this.getId() == 0) {
			if(gateway.partAlreadyExists(0, this.getPartName(), this.getPartNumber()))
				throw new GatewayException(this.getPartName() + " is already in the database");
		}

		try {
		//if id is 0 then this is a new warehouse to insert, else its an update
		if(this.getId() == 0) {
		//set id to the long returned by insertwarehouse
		this.setId(gateway.insertPart(this));
		} 
		else {
		//fetch warehouse from db table in case this fails
		//try to save to the database
		gateway.savePart(this);
		}
		//if gateway ok then notify observers
		notifyObservers();
		} catch(GatewayException e) {
		//if fails then try to refetch model fields from the database
		throw new GatewayException("Error");

//			throw new GatewayException("Error trying to save the Part object!");
		}	

		return true;
		}

	public void setLock(Part p) throws GatewayException{

		gateway.fetchPart(p.getId(), true);

		}
	@Override
	public String toString(){
		String ret = " Part: "+partName;
		ret += " Part Number " + partNumber;
		ret += " Unit of Quanitity "+ unitQuanitity;
		ret += " Vendor "+ vendor;
		ret += " Vendor part number "+ vendorPartNum;
		
		
		return ret;
	}

	
}

