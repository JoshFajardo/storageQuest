package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import controller.MDIChild;
import controller.MDIParent;
import controller.MenuCommands;
import controller.WarehouseListController;
import models.Warehouse;
import models.WarehouseList;


public class WarehouseListView extends MDIChild{

	private JList<Warehouse> listWarehouse;
	private WarehouseListController myList;
	Warehouse warehouse = new Warehouse();
	
	
	private Warehouse selectedModel;
	private WarehouseList listNew;
	
	public WarehouseListView(String title, WarehouseListController list, 
			MDIParent m,WarehouseList warehouselistNew) {
		super(title, m);
		list.setMyListView(this);
		JPanel panel = new JPanel();
		
		this.listNew = warehouselistNew;
		myList = list;
		
		
		listWarehouse = new JList<Warehouse>(myList);
		//uses the cell renderer instead of the toString
		listWarehouse.setCellRenderer(new WarehouseListCellRenderer());
		listWarehouse.setPreferredSize(new Dimension(200,300));
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
		
		this.setPreferredSize(new Dimension(340,200));
		
		//panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JButton deleteButton = new JButton("Delete!");
		deleteButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int index = listWarehouse.getSelectedIndex();
				if(index < 0){
					parent.displayChildMessage("No warehouse selected!");
					return;
				}
				warehouse = myList.getElementAt(index);
				
				int test = JOptionPane.showConfirmDialog(parent, "Do you want to delete " + warehouse.getFullName()+"?", "Delete", JOptionPane.YES_NO_OPTION);
				if(test == JOptionPane.YES_OPTION){
					listNew.removeWarehouseFromList(warehouse);
					if(index == myList.getSize())
						index--;
					listWarehouse.setSelectedIndex(index);
				
					listWarehouse.updateUI();
				}
			}
		});
		panel.add(deleteButton);
		this.add(panel,BorderLayout.NORTH);
		
		
		panel.setLayout(new FlowLayout());
		JButton AddButton = new JButton("Add New!");
		AddButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int index = myList.getSize();
				//decrement because index and size can't be the same, throw exception
				index--;
				warehouse = myList.getElementAt(index);
				listNew.addWarehouseToList(new Warehouse("New_Entry " +index ,
						"<Address>",
						"<State>",
						"<City>",
						"00000",
						0));
				listWarehouse.updateUI();
			}
		});
		panel.add(AddButton);
		this.add(panel,BorderLayout.EAST);
		
		
	}
	
	public void openDetailView(){
		parent.doCommand(MenuCommands.SHOW_DETAIL_WAREHOUSE,this);
	}
	
	public Warehouse getSelectedWarehouse(){
		return selectedModel;
	}
	/*
	public int getSize(WarehouseList List){
		return List.getListSize();
	}
	*/
	protected void childClosing(){
		
		super.childClosing();
		
		myList.unregisterAsObserver();
	}
			
}
	

