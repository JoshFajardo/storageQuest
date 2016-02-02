package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import controller.MDIChild;
import controller.MDIParent;
import controller.MenuCommands;
import controller.WarehouseListController;
import models.Warehouse;


public class WarehouseListView extends MDIChild{

	private JList<Warehouse> listWarehouse;
	private WarehouseListController myList;
	
	
	private Warehouse selectedModel;
	
	public WarehouseListView(String title, WarehouseListController list, MDIParent m){
		super(title, m);
		
		myList = list;
		listWarehouse = new JList<Warehouse>(myList);
		//uses the cell renderer instead of the toString
		listWarehouse.setCellRenderer(new WarehouseListCellRenderer());
		listWarehouse.setPreferredSize(new Dimension(200,200));
		//event handler for a double click of the mouse
		listWarehouse.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent evt){
				if(evt.getClickCount() == 2){
					int index = listWarehouse.locationToIndex(evt.getPoint());
					
					selectedModel = myList.getElementAt(index);
					
					
					openDetailView();
				}
			}
		});
		this.add(new JScrollPane(listWarehouse));
		
		this.setPreferredSize(new Dimension(240,200));
		
		
		
	}
	
	public void openDetailView(){
		parent.doCommand(MenuCommands.SHOW_DETAIL_WAREHOUSE,this);
	}
	
	public Warehouse getSelectedWarehouse(){
		return selectedModel;
	}
	
	protected void childClosing(){
		
		super.childClosing();
		
		myList.unregisterAsObserver();
	}
			
}
	

