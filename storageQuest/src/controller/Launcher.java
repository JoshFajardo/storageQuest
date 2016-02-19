package controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import database.PartTableGateway;
import database.PartTableGatewayMySQL;
import database.GatewayException;
import database.WarehouseTableGateway;
import database.WarehouseTableGatewayMYSQL;

import models.PartList;
import models.WarehouseList;


/* 
 * 
 * this starts the whole thing
 */
public class Launcher {

	public static void createAndShowGUI(){
		
		WarehouseTableGateway wtg = null;
		//PartTableGateway ptg = null;
		try{
			wtg = new WarehouseTableGatewayMySQL();
		}catch (GatewayException e){
			JOptionPane.showMessageDialog(null, "Database isn't working. Figures.", "Oops!",JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		WarehouseList warehouseList = new WarehouseList();
		
		/*warehouseList.addWarehouseToList(new Warehouse("Dunder Mifflin",
				"123 Paper St.",
				"Scanton",
				"Pennsylvania",
				"18504",
				100));
		*/
		MDIParent appFrame = new MDIParent("Cabinetron Warehouse System",warehouseList);
		
		appFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		appFrame.setSize(640,640);
		
		appFrame.setVisible(true);
		
	}
	
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
