package reports;

import java.awt.Color;
import java.io.IOException;
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

public class WarehouseReportPDF extends ReportMaster{

	private PDDocument doc;
	
	public WarehouseReportPDF(ReportGateway gw){
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
		
		//prep work for the first page of the report
		doc = new PDDocument();
		PDPage page1 = new PDPage();
		PDRectangle rect = page1.getMediaBox();
		doc.addPage(page1);
		
		
		
		PDPage page2 = new PDPage();
		PDRectangle rect2 = page2.getMediaBox();
		doc.addPage(page2);
		
		//content stream for the first page
		PDPageContentStream content = null;
		//content for second page
		PDPageContentStream content2 = null;
		
		//fonts, ill probably change to see
		PDFont fontPlain = PDType1Font.HELVETICA;
		PDFont fontBold = PDType1Font.HELVETICA_BOLD;
		PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
		PDFont fontMono = PDType1Font.COURIER;
		PDFont fontMonoBold = PDType1Font.COURIER_BOLD;
		
		float margin = 20;
		
		try{
			content = new PDPageContentStream(doc,page1);
			
			
			
			//header for page 1
			content.setNonStrokingColor(Color.CYAN);
			content.setStrokingColor(Color.BLACK);
			float bottomY = rect.getHeight() - margin - 50;
			float headerEndX = rect.getWidth() - margin * 2;
			content.addRect(margin, bottomY, headerEndX, 50);
			content.fillAndStroke();
			
			
			//resets the non stroking color
			content.setNonStrokingColor(Color.BLACK);
			
			
			
			//report title
			content.setFont(fontBold, 24);
			content.beginText();
			content.newLineAtOffset(margin + 15, bottomY + 15);
			content.showText("Warehouse Inventory Summary");
			content.endText();
			
			
			//where the text after the header should go
			content.setFont(fontMonoBold, 12);
			float dataY = rect.getHeight() - margin - 50 - 25 - fontMonoBold.getHeight(12);
			
		
			
			//colum layout, this might require tweaking
			//warehouse Name    Part #     Part Name            Quantity      Unit
			float colX_0 = margin +15;//warehouse name
			float colX_1 = colX_0 +130;//Part number
			float colX_2 = colX_1 +90;//Part Name
			float colX_3 = colX_2 +180;//quantity
			float colX_4 = colX_3 +70; //unit
			
			//print the colum texts
			content.beginText();
			content.newLineAtOffset(colX_0, dataY);
			content.showText("Warehouse Name");
			content.endText();
			content.beginText();
			content.newLineAtOffset(colX_1, dataY);
			content.showText("Part Number");
			content.endText();
			content.beginText();
			content.newLineAtOffset(colX_2, dataY);
			content.showText("Part Name");
			content.endText();
			content.beginText();
			content.newLineAtOffset(colX_3, dataY);
			content.showText("Quantity");
			content.endText();
			content.beginText();
			content.newLineAtOffset(colX_4, dataY);
			content.showText("Unit");
			content.endText();
			
			
			
			
			
			
			// print the report rows
			content.setFont(fontMono, 12);
			
			int counter = 1;
			
			for(WarehousePart wp : records){
			//the offset for the current row
				float offset = dataY - (counter * (fontMono.getHeight(12) + 15));

				Warehouse w = wp.getOwner();
				Part p = wp.getPart();
				
				content.beginText();
				content.newLineAtOffset(colX_0, offset);
				content.showText(w.getWarehouseName());
				content.endText();
				content.beginText();
				content.newLineAtOffset(colX_1, offset);
				content.showText("" + p.getPartNumber());
				content.endText();
				content.beginText();
				content.newLineAtOffset(colX_2, offset);
				content.showText("" + p.getPartName());
				content.endText();
				content.beginText();
				content.newLineAtOffset(colX_3, offset);
				content.showText("" + wp.getQuantity());
				content.endText();
				content.beginText();
				content.newLineAtOffset(colX_4, offset);
				content.showText("" + p.getUnitQuanitity());
				content.endText();


				counter ++;
				if(counter >40){
					content.close();
					break;
				}
					
				
			}
			
			content = new PDPageContentStream(doc,page2);
            content.setFont(fontMono, 12);
			
			int counter2 = 1;
			
			for(WarehousePart wp : records.subList(40, records.size())){
				//the offset for the current row
				float offset = dataY - (counter2 * (fontMono.getHeight(12) + 15));

				Warehouse w = wp.getOwner();
				Part p = wp.getPart();
				
				content.beginText();
				content.newLineAtOffset(colX_0, offset);
				content.showText(w.getWarehouseName());
				content.endText();
				content.beginText();
				content.newLineAtOffset(colX_1, offset);
				content.showText("" + p.getPartNumber());
				content.endText();
				content.beginText();
				content.newLineAtOffset(colX_2, offset);
				content.showText("" + p.getPartName());
				content.endText();
				content.beginText();
				content.newLineAtOffset(colX_3, offset);
				content.showText("" + wp.getQuantity());
				content.endText();
				content.beginText();
				content.newLineAtOffset(colX_4, offset);
				content.showText("" + p.getUnitQuanitity());
				content.endText();

				
				counter2 ++;
			}
	
			
		}catch(IOException e){
			throw new ReportException("Error in report generation: " +e.getMessage());
		}finally{
			
			try{
				content.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void outputReportToFile(String fileName)throws ReportException{
		
		try{
			doc.save(fileName);
		}catch(IOException e){
			throw new ReportException("Error in report save to file: "+e.getMessage());
		}
	}
	

	@Override
	public void close(){
		super.close();
		try{
			doc.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
}
