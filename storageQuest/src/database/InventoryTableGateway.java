package database;

import java.util.List;

import models.InventoryItem;
import models.Part;
import models.Warehouse;

public interface InventoryTableGateway {
	public abstract InventoryItem fetchInventory(long id) throws GatewayException;
	
	
	public abstract List<InventoryItem> fetchInventory() throws GatewayException;

}
