package controller;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import models.Warehouse;
import models.WarehouseList;
/*later will need to import the models and views*/

/* CS 4743 Assignment 1 by Joshua Fajardo
 * 
 * this starts the whole thing
 */
public class Launcher {

	public static void createAndShowGUI(){
		
		WarehouseList warehouseList = new WarehouseList();
		
		warehouseList.addWarehouseToList(new Warehouse("Dunder Mifflin",
				"123 Paper St.",
				"Scanton",
				"Pennsylvania",
				"18504",
				100));
		
		
		
		
	}
	
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
