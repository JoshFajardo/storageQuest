package reports;

import java.sql.Connection;

import database.GatewayException;

public abstract class ReportMaster {

	// database connection
	protected ReportGateway gateway;
	
	public ReportMaster(ReportGateway gw){
		gateway = gw;
	}
	
	
	//clean up
	public void close(){
		gateway.close();
	}
	
	//methods for specific report output
	
	public abstract void generateReport() throws ReportException;
	public abstract void outputReportToFile(String fileName) throws ReportException;
}
