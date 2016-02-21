package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import database.PartTableGateway;
import database.GatewayException;
import database.WarehouseTableGateway;

public class PartList extends Observable implements Observer {
	
	//list of part objects
	private List<Part> myList;
	//map for determining if part is in the list
	private HashMap<Long, Part> myIdMap;
	//new records to know when to update the map
	private ArrayList<Part> newRecords;
	//connectin for the part list
	private PartTableGateway gateway;
	
	
	private boolean dontNotify;
	
	public PartList(){
		myList = new ArrayList<Part>();
		myIdMap = new HashMap<Long, Part>();
		dontNotify = false;
		newRecords = new ArrayList<Part>();
	}
	
	public void loadFromGateway(){
		
		List<Part> parts = null;
		try{
			parts = gateway.fetchParts();
		}catch (GatewayException e){
			e.printStackTrace();
			return;
		}
		
		dontNotify = true;
		
		//warehouse not in the database needs to be removed
		for(int i = myList.size() -1; i >= 0; i--){
			Part p = myList.get(i);
			boolean removeRecord = true;
			//don't remove a recently added record that hasn't been saved yet
			if(p.getId() == Part.INVALID_ID){
				removeRecord = false;
			}else{
				for(Part pCheck : parts){
					if(pCheck.getId() == p.getId()){
						removeRecord = false;
						break;
					}
				}
			}
			
			//warehouse not found in db warehouse array, so delete
			if(removeRecord)
				removePartFromList(p);
		}
		
		for(Part p : parts){
			if(!myIdMap.containsKey(p.getId())){
				addPartToList(p);
			}
		}
		// tell observers of this list to update
		this.notifyObservers();
		
		dontNotify = false;
		
	}
	public Part findById(long id) {
		//checks the identity map
		if(myIdMap.containsKey(new Long(id)))
			return myIdMap.get(new Long(id));
		return null;
	}
	
	public void addPartToList(Part p) {
		myList.add(p);
		p.setGateway(this.gateway);
		p.addObserver(this);

		//adds part to the identity map
		myIdMap.put(p.getId(), p);


		this.setChanged();
		if(!dontNotify)
			this.notifyObservers();
	}


	public Part removePartFromList(Part p) {
		if(myList.contains(p)) {
			myList.remove(p);
			// removes part from hash map
			myIdMap.remove(p.getId());

			//tell all observers of this list to update
			this.setChanged();
			if(!dontNotify)
				this.notifyObservers();

			return p;
		}
		return null;
	}
	
// these are accessor methods
	
	public List<Part> getList() {
		return myList;
	}

	public void setList(List<Part> myList) {
		this.myList = myList;
	}

	public PartTableGateway getGateway() {
		return gateway;
	}

	public void setGateway(PartTableGateway gateway) {
		this.gateway = gateway;
	}

	//adds a part w/ invalid id to the list of new records
	public void addToNewRecords(Part p) {
		newRecords.add(p);
	}
	

	@Override
	public void update(Observable o, Object arg) {

		Part p = (Part) o;
		if(newRecords.contains(p)) {
			myIdMap.remove(Part.INVALID_ID);
			myIdMap.put(p.getId(), p);
			newRecords.remove(p);
		}
		
		this.setChanged();
		notifyObservers();
	}

}
