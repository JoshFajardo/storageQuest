package reports;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import database.GatewayException;
import models.Part;
import models.Warehouse;
import models.WarehousePart;

public class WarehouseReportExcel extends ReportMaster {
	
	private StringBuilder doc;
	
	public WarehouseReportExcel(ReportGateway gw){
		super(gw);
		
		
		BasicConfigurator.configure();
		
		doc = null;
		
		
	}
	
	
	@Override
	public void generateReport() throws ReportException{
		
		List<WarehousePart> records = null;
		try{
			records = gateway.fetchWarehouseAndParts();
		}catch(GatewayException e){
			throw new ReportException("Error in report generation: "+e.getMessage());
		}
		
		
		doc = new StringBuilder();
		
		doc.append("Warehouse Inventory Summary\n\n");
		
		doc.append("Warehouse Name\t");
		doc.append("Part Number\t");
		doc.append("Part Name\t");
		doc.append("Quantity\t");
		doc.append("Unit\n");
		
		for(WarehousePart wp: records){
			Warehouse w = wp.getOwner();
			Part p = wp.getPart();
			
			doc.append(w.getWarehouseName() + "\t");
			doc.append("" + p.getPartNumber()+"\t");
			doc.append(""+p.getPartName()+"\t");
			doc.append(""+wp.getQuantity()+"\t");
			doc.append(""+p.getUnitQuanitity()+"\n");
		}
		
	}
	
	
	@Override
	public void outputReportToFile(String fileName)throws ReportException{
		
		try(PrintWriter out = new PrintWriter(fileName)){
			out.print(doc.toString());
		}catch(IOException e){
			throw new ReportException("Error in report save to file: "+ e.getMessage());
		}
	}
	
	@Override
	public void close(){
		super.close();
	}

}
