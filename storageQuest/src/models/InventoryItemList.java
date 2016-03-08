package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import database.GatewayException;
import database.InventoryTableGateway;


public class InventoryItemList extends Observable implements Observer {

	//list of the inventory items
	private List<InventoryItem> myList;
	
	//hash map of the inventory items
	private HashMap<Long, InventoryItem> myIdMap;
	
	//used for updating and adding new records
	private ArrayList<InventoryItem> newRecords;
	
	//database connection
	private InventoryTableGateway gateway;
	
	private boolean dontNotify;
	
	public InventoryItemList(){
		myList = new ArrayList<InventoryItem>();
		myIdMap = new HashMap<Long, InventoryItem>();
		dontNotify = false;
		newRecords = new ArrayList<InventoryItem>();
	}
	
	
	
	public void loadFromGateway() {
		//fetch list of objects from the database
		List<InventoryItem> inventory = null;
		try {
			inventory = gateway.fetchInventory();
		} catch (GatewayException e) {
			e.printStackTrace();
			return;
		}
		
		
		dontNotify = true;
		
		//any inventory in the list that is NOT in the db needs to be removed from our list
		for(int j = myList.size() - 1; j >= 0; j--) {
			InventoryItem i = myList.get(j);
			boolean removeInventory = true;
			//don't remove a recently Added record that hasn't been saved yet
			if(i.getId() == InventoryItem.INVALID_ID) {
				removeInventory = false;
			} else {
				for(InventoryItem iCheck : inventory) {
					if(iCheck.getId() == i.getId()) {
						removeInventory = false;
						break;
					}
				}
			}
			//w not found in db warehouse array so delete it
			if(removeInventory)
				removeInventoryFromList(i);
		}
		
		//for each object in the list, check if its in the hashmap
		for(InventoryItem i : inventory) {
			if(!myIdMap.containsKey(i.getId())) {
				addInventoryToList(i);
			}
		}
		
		this.notifyObservers();

		//turn this off
		dontNotify = false;
	}
	
	
	public void addInventoryToList(InventoryItem i){
		myList.add(i);
		i.setGateway(this.gateway);
		i.addObserver(this);
		//adds record to the identity map
		myIdMap.put(i.getId(),i);
		
		//tells all observers of this list to update
		this.setChanged();
		if(!dontNotify)
			this.notifyObservers();
		
	}
	
	public InventoryItem removeInventoryFromList(InventoryItem i){
		if(myList.contains(i)){
			myList.remove(i);
			
			myIdMap.remove(i.getId());
			
			this.setChanged();
			if(!dontNotify)
				this.notifyObservers();
			
			return i;
		}
		return null;
		
	}
	
	
	//Accessors
	//@return
	
	public List<InventoryItem> getList(){
		return myList;
	}
	
	public void setList(List<InventoryItem> myList){
		this.myList = myList;
	}
	
	public InventoryTableGateway getGateway(){
		return gateway;
	}
	
	public void addToNewRecords(InventoryItem i){
		newRecords.add(i);
	}
	
	@Override
	public void update(Observable o, Object arg){
		
		InventoryItem i = (InventoryItem) o;
		if(newRecords.contains(i)){
			myIdMap.remove(InventoryItem.INVALID_ID);
			myIdMap.put(i.getId(), i);
			newRecords.remove(i);
		}
		setChanged();
		notifyObservers();
	}
}
