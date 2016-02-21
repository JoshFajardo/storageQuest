package database;

import java.util.List;

import models.PartList;
import models.Warehouse;
import models.WarehousePart;


// does the methods for the warehouse database
public interface WarehouseTableGateway {
	public abstract Warehouse fetchWarehouse(long id) throws GatewayException;
	public abstract boolean warehouseAlreadyExists(long id, String wn) throws GatewayException;
	public abstract void deleteWarehouse(long id) throws GatewayException;
	public abstract long insertWarehouse(Warehouse w) throws GatewayException;
	public abstract void saveWarehouse(Warehouse w) throws GatewayException;
	public abstract List<Warehouse> fetchWarehouse() throws GatewayException;
	
	public abstract List<WarehousePart> fetchWarehouseParts (Warehouse w, PartList pList) throws GatewayException;
	public abstract void saveWarehousePart(WarehousePart wp) throws GatewayException;
	public abstract void insertWarehousePart(WarehousePart wp) throws GatewayException;
	public abstract void deleteWarehousePart(WarehousePart wp) throws GatewayException;
	
	public abstract void close();

}
