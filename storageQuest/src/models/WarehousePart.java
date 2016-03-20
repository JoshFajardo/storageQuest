package models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import database.GatewayException;

//the model for the relationship between a part and a warehouse
public class WarehousePart extends Observable implements Observer {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	public static final String ERRORMSG_INVALID_OWNER_STARTDATE = "Invalid owner start date";
	
	//warehouse that has the part
	private Warehouse owner;
	// part in the warehouse
	private Part part;
	
	private Date ownerStartDate;
	
	private WarehousePart quantity; 
	
	public WarehousePart(Warehouse w, Part p){
		owner = w;
		part = p;
		
		ownerStartDate = new Date();
	}
	
	public Warehouse getOwner(){
		return owner;
	}
	public void setOwner(Warehouse owner){
		this.owner = owner;
	}
	
	public Part getPart(){
		return part;
	}
	public void setPart(Part part){
		this.part = part;
	}
	
	public WarehousePart getQuantity(){
		return quantity;
	}
	public void setQuantity(WarehousePart quantity){
		this.quantity = quantity;
	}
	
	public Date getOwnerStartDate(){
		return ownerStartDate;
	}
	public void setOwnerStartDate(Date ownerStartDate) {
		this.ownerStartDate = ownerStartDate;
		
		setChanged();
	}

	public void finishUpdate() throws GatewayException {
		//save through owner's gateway
		this.getOwner().updateMyPart(this);
		
		notifyObservers();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		//if observable is Dog then need to notify Person observer
		if(o instanceof Part) {
			this.setChanged();
			this.notifyObservers();
		}
		
	}
}
