package reports;

import java.util.List;
import database.GatewayException;
import models.WarehousePart;

public interface ReportGateway {
	public abstract List<WarehousePart> fetchWarehouseAndParts() throws GatewayException;
	public abstract void close();
}
