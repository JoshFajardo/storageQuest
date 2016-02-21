package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import database.GatewayException;
import database.WarehouseTableGateway;

public class WarehouseList extends Observable implements Observer {

	private List<Warehouse> myList;
	
	private HashMap<Long, Warehouse> myIdMap;
	

	private ArrayList<Warehouse> newRecords;

	//database connection
	private WarehouseTableGateway gateway;
	

	private boolean dontNotify;
	
	public WarehouseList() {
		myList = new ArrayList<Warehouse>();
		myIdMap = new HashMap<Long, Warehouse>();
		dontNotify = false;
		newRecords = new ArrayList<Warehouse>();
	}
	

	public void loadFromGateway() {
		//fetch list of objects from the database
		List<Warehouse> warehouse = null;
		try {
			warehouse = gateway.fetchWarehouse();
		} catch (GatewayException e) {
			e.printStackTrace();
			return;
		}
		
		
		dontNotify = true;
		
		//any warehouse in the list that is NOT in the db needs to be removed from our list
		for(int i = myList.size() - 1; i >= 0; i--) {
			Warehouse w = myList.get(i);
			boolean removeWarehouse = true;
			//don't remove a recently Added record that hasn't been saved yet
			if(w.getId() == Warehouse.INVALID_ID) {
				removeWarehouse = false;
			} else {
				for(Warehouse wCheck : warehouse) {
					if(wCheck.getId() == w.getId()) {
						removeWarehouse = false;
						break;
					}
				}
			}
			//w not found in db warehouse array so delete it
			if(removeWarehouse)
				removeWarehouseFromList(w);
		}
		
		//for each object in the list, check if its in the hashmap
		for(Warehouse w : warehouse) {
			if(!myIdMap.containsKey(w.getId())) {
				addWarehouseToList(w);
			}
		}
		
		this.notifyObservers();

		//turn this off
		dontNotify = false;
	}
	
	
	
	public void addWarehouseToList(Warehouse w) {
		myList.add(w);
		w.setGateway(this.gateway);
		w.addObserver(this);
		//add record to identity map
		myIdMap.put(w.getId(), w);

		//tell all observers of this list to update
		this.setChanged();
		if(!dontNotify)
			this.notifyObservers();
	}

    //removes a warehouse from the list
	public Warehouse removeWarehouseFromList(Warehouse w) {
		if(myList.contains(w)) {
			myList.remove(w);
			//removes from hash map
			myIdMap.remove(w.getId());

			//tell all observers of this list to update
			this.setChanged();
			if(!dontNotify)
				this.notifyObservers();

			return w;
		}
		return null;
	}
	
	/**
	 * Accessors
	 * @return
	 */
	public List<Warehouse> getList() {
		return myList;
	}

	public void setList(List<Warehouse> myList) {
		this.myList = myList;
	}

	public WarehouseTableGateway getGateway() {
		return gateway;
	}

	public void setGateway(WarehouseTableGateway gateway) {
		this.gateway = gateway;
	}

	
	public void addToNewRecords(Warehouse w) {
		newRecords.add(w);
	}


	@Override
	public void update(Observable o, Object arg) {

		Warehouse w = (Warehouse) o;
		if(newRecords.contains(w)) {
			myIdMap.remove(Warehouse.INVALID_ID);
			myIdMap.put(w.getId(), w);
			newRecords.remove(w);
		}

		setChanged();
		notifyObservers();
	}
}
