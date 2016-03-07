package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import database.GatewayException;
import database.InventoryItemTableGateway;

public class InventoryItem extends Observable /*implements Observer*/ {

	public static final String ERRORMSG_INVALID_ID = "Invalid Id!";
	public static final String ERRORMSG_INVALID_WAREHOUSE_ID = "Invalid Warehouse Id";
	public static final String ERRORMSG_INVALID_PART_ID = "Invalid Part id";
	public static final String ERRORMSG_INVALID_QUANTITY = "Invalid quantity!";
	
	public static final int INVALID_ID = 0;
	
	private long id;
	
	private long warehouseId;
	
	private long partId;
	
	private double quantity;
	
	private InventoryItemTableGateway gateway;
	
	private List<InventoryItem> myInventory;
	
	public InventoryItem(){
		
		id = INVALID_ID;
		warehouseId = INVALID_ID;
		partId = INVALID_ID;
		quantity = 0.0;
		
		
	}
	
	public InventoryItem (long id, long wId, long pId, double q){
		this();
		if(id<1)
			throw new IllegalArgumentException(ERRORMSG_INVALID_ID);
		if(wId<1)
			throw new IllegalArgumentException(ERRORMSG_INVALID_WAREHOUSE_ID);
		if(pId<1)
			throw new IllegalArgumentException(ERRORMSG_INVALID_PART_ID);
		
		if(!validQuantity(q))
			throw new IllegalArgumentException(ERRORMSG_INVALID_QUANTITY);
		setId(id);
		setWID(wId);
		setPID(pId);
		
		quantity = q;
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getWID(){
		return warehouseId;
	}
	
	public void setWID(long warehouseId){
		this.warehouseId = warehouseId;
	}
	
	public long getPID(){
		return partId;
	}
	
	public void setPID(long partId){
		this.partId = partId;
	}
	
	public double getQuantity(){
		return quantity;
	}
	
	public void setQuantity(double quantity){
		this.quantity = quantity;
	}
	
	public boolean validQuantity(double q){
		if(q < 0.0)
			return false;
		
		return true;
	}
	
	
	
	
	
	
	
	
}
