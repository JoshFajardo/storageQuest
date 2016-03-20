package database;

import java.util.List;
import models.Part;

public interface PartTableGateway {
	
	public abstract Part fetchPart(long id) throws GatewayException;
	public abstract void deletePart(long id) throws GatewayException;
	public abstract long insertPart(Part p) throws GatewayException;
	public abstract void savePart(Part p) throws GatewayException;
	public abstract List<Part> fetchParts() throws GatewayException;
	
	public abstract void close();
	public abstract List<Part> getPartsByWarehouseId(long id) throws GatewayException;

}
